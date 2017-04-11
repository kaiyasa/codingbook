
class Parser {
    //===----------------------------------------------------------------------===//
    // Lexer
    //===----------------------------------------------------------------------===//
    
    // The lexer returns tokens [0-255] if it is an unknown character, otherwise one
    // of these for known things.
    enum Token {
      tok_eof = -1,
    
      // commands
      tok_def = -2,
      tok_extern = -3,
    
      // primary
      tok_identifier = -4,
      tok_number = -5
    };
    
    String IdentifierStr; // Filled in if tok_identifier
    double NumVal;             // Filled in if tok_number
    
    /// gettok - Return the next token from standard input.
    int gettok() {
      int LastChar = ' ';
    
      // Skip any whitespace.
      while (isspace(LastChar))
        LastChar = getchar();
    
      if (isalpha(LastChar)) { // identifier: [a-zA-Z][a-zA-Z0-9]*
        IdentifierStr = LastChar;
        while (isalnum((LastChar = getchar())))
          IdentifierStr += LastChar;
    
        if (IdentifierStr == "def")
          return tok_def;
        if (IdentifierStr == "extern")
          return tok_extern;
        return tok_identifier;
      }
    
      if (isdigit(LastChar) || LastChar == '.') { // Number: [0-9.]+
        String NumStr;
        do {
          NumStr += LastChar;
          LastChar = getchar();
        } while (isdigit(LastChar) || LastChar == '.');
    
        NumVal = strtod(NumStr.c_str(), null);
        return tok_number;
      }
    
      if (LastChar == '#') {
        // Comment until end of line.
        do
          LastChar = getchar();
        while (LastChar != EOF && LastChar != '\n' && LastChar != '\r');
    
        if (LastChar != EOF)
          return gettok();
      }
    
      // Check for end of file.  Don't eat the EOF.
      if (LastChar == EOF)
        return tok_eof;
    
      // Otherwise, just return the character as its ascii value.
      int ThisChar = LastChar;
      LastChar = getchar();
      return ThisChar;
    }
    
    //===----------------------------------------------------------------------===//
    // Abstract Syntax Tree (aka Parse Tree)
    //===----------------------------------------------------------------------===//
    
    /// ExprAST - Base class for all expression nodes.
    interface ExprAST {
    };
    
    /// NumberExprAST - Expression class for numeric literals like "1.0".
    class NumberExprAST implements ExprAST {
      private double Val;
    
      NumberExprAST(double Val) { this.Val = Val  }
    };
    
    /// VariableExprAST - Expression class for referencing a variable, like "a".
    class VariableExprAST implements ExprAST {
      private String Name;
    
      VariableExprAST(String Name) { this.Name = Name }
    };
    
    /// BinaryExprAST - Expression class for a binary operator.
    class BinaryExprAST implements ExprAST {
      private char Op;
      private ExprAST LHS, RHS;
    
      BinaryExprAST(char Op, ExprAST LHS,
                    ExprAST RHS)
          { this.Op = Op; this.LHS = LHS; this.RHS = RHS; }
    };
    
    /// CallExprAST - Expression class for function calls.
    class CallExprAST implements ExprAST {
      private String Callee;
      private List<ExprAST> Args;
    
      CallExprAST(String Callee,
                  List<ExprAST> Args)
          { this.Callee = Callee; this.Args = Args; }
    };
    
    /// PrototypeAST - This class represents the "prototype" for a function,
    /// which captures its name, and its argument names (thus implicitly the number
    /// of arguments the function takes).
    class PrototypeAST {
      private String Name;
      private List<String> Args;
    
      PrototypeAST(String Name, List<String> Args)
          { this.Name = Name; this.Args = Args; }
    
      String &getName() { return Name; }
    };
    
    /// FunctionAST - This class represents a function definition itself.
    class FunctionAST {
      private PrototypeAST Proto;
      private ExprAST Body;
    
      FunctionAST(PrototypeAST Proto,
                  ExprAST Body)
          { this.Proto = Proto; this.Body = Body; }
    };
    
    
    //===----------------------------------------------------------------------===//
    // Parser
    //===----------------------------------------------------------------------===//
    
    /// CurTok/getNextToken - Provide a simple token buffer.  CurTok is the current
    /// token the parser is looking at.  getNextToken reads another token from the
    /// lexer and updates CurTok with its results.
    int CurTok;
    int getNextToken() { return CurTok = gettok(); }
    
    /// BinopPrecedence - This holds the precedence for each binary operator that is
    /// defined.
    std::map<char, int> BinopPrecedence;
    
    /// GetTokPrecedence - Get the precedence of the pending binary operator token.
    int GetTokPrecedence() {
      if (!isascii(CurTok))
        return -1;
    
      // Make sure it's a declared binop.
      int TokPrec = BinopPrecedence[CurTok];
      if (TokPrec <= 0)
        return -1;
      return TokPrec;
    }
    
    /// LogError* - These are little helper functions for error handling.
    ExprAST LogError(String Str) {
      fprintf(stderr, "Error: %s\n", Str);
      return null;
    }
    PrototypeAST LogErrorP(String Str) {
      LogError(Str);
      return null;
    }
    
    ExprAST ParseExpression();
    
    /// numberexpr ::= number
    ExprAST ParseNumberExpr() {
      def Result = llvm::make_unique<NumberExprAST>(NumVal);
      getNextToken(); // consume the number
      return Result;
    }
    
    /// parenexpr ::= '(' expression ')'
    ExprAST ParseParenExpr() {
      getNextToken(); // eat (.
      def V = ParseExpression();
      if (!V)
        return null;
    
      if (CurTok != ')')
        return LogError("expected ')'");
      getNextToken(); // eat ).
      return V;
    }
    
    /// identifierexpr
    ///   ::= identifier
    ///   ::= identifier '(' expression* ')'
    ExprAST ParseIdentifierExpr() {
      String IdName = IdentifierStr;
    
      getNextToken(); // eat identifier.
    
      if (CurTok != '(') // Simple variable ref.
        return llvm::make_unique<VariableExprAST>(IdName);
    
      // Call.
      getNextToken(); // eat (
      List<ExprAST> Args;
      if (CurTok != ')') {
        while (true) {
          if (def Arg = ParseExpression())
            Args.push_back(Arg);
          else
            return null;
    
          if (CurTok == ')')
            break;
    
          if (CurTok != ',')
            return LogError("Expected ')' or ',' in argument list");
          getNextToken();
        }
      }
    
      // Eat the ')'.
      getNextToken();
    
      return llvm::make_unique<CallExprAST>(IdName, Args);
    }
    
    /// primary
    ///   ::= identifierexpr
    ///   ::= numberexpr
    ///   ::= parenexpr
    ExprAST ParsePrimary() {
      switch (CurTok) {
      default:
        return LogError("unknown token when expecting an expression");
      case tok_identifier:
        return ParseIdentifierExpr();
      case tok_number:
        return ParseNumberExpr();
      case '(':
        return ParseParenExpr();
      }
    }
    
    /// binoprhs
    ///   ::= ('+' primary)*
    ExprAST ParseBinOpRHS(int ExprPrec,
                                                  ExprAST LHS) {
      // If this is a binop, find its precedence.
      while (true) {
        int TokPrec = GetTokPrecedence();
    
        // If this is a binop that binds at least as tightly as the current binop,
        // consume it, otherwise we are done.
        if (TokPrec < ExprPrec)
          return LHS;
    
        // Okay, we know this is a binop.
        int BinOp = CurTok;
        getNextToken(); // eat binop
    
        // Parse the primary expression after the binary operator.
        def RHS = ParsePrimary();
        if (!RHS)
          return null;
    
        // If BinOp binds less tightly with RHS than the operator after RHS, let
        // the pending operator take RHS as its LHS.
        int NextPrec = GetTokPrecedence();
        if (TokPrec < NextPrec) {
          RHS = ParseBinOpRHS(TokPrec + 1, RHS);
          if (!RHS)
            return null;
        }
    
        // Merge LHS/RHS.
        LHS = llvm::make_unique<BinaryExprAST>(BinOp, LHS,
                                               RHS);
      }
    }
    
    /// expression
    ///   ::= primary binoprhs
    ///
    ExprAST ParseExpression() {
      def LHS = ParsePrimary();
      if (!LHS)
        return null;
    
      return ParseBinOpRHS(0, LHS);
    }
    
    /// prototype
    ///   ::= id '(' id* ')'
    PrototypeAST ParsePrototype() {
      if (CurTok != tok_identifier)
        return LogErrorP("Expected function name in prototype");
    
      String FnName = IdentifierStr;
      getNextToken();
    
      if (CurTok != '(')
        return LogErrorP("Expected '(' in prototype");
    
      List<String> ArgNames;
      while (getNextToken() == tok_identifier)
        ArgNames.push_back(IdentifierStr);
      if (CurTok != ')')
        return LogErrorP("Expected ')' in prototype");
    
      // success.
      getNextToken(); // eat ')'.
    
      return llvm::make_unique<PrototypeAST>(FnName, ArgNames);
    }
    
    /// definition ::= 'def' prototype expression
    FunctionAST ParseDefinition() {
      getNextToken(); // eat def.
      def Proto = ParsePrototype();
      if (!Proto)
        return null;
    
      if (def E = ParseExpression())
        return llvm::make_unique<FunctionAST>(Proto, E);
      return null;
    }
    
    /// toplevelexpr ::= expression
    FunctionAST ParseTopLevelExpr() {
      if (def E = ParseExpression()) {
        // Make an anonymous proto.
        def Proto = llvm::make_unique<PrototypeAST>("__anon_expr",
                                                     List<String>());
        return llvm::make_unique<FunctionAST>(Proto, E);
      }
      return null;
    }
    
    /// external ::= 'extern' prototype
    PrototypeAST ParseExtern() {
      getNextToken(); // eat extern.
      return ParsePrototype();
    }
    
    //===----------------------------------------------------------------------===//
    // Top-Level parsing
    //===----------------------------------------------------------------------===//
    
    void HandleDefinition() {
      if (ParseDefinition()) {
        fprintf(stderr, "Parsed a function definition.\n");
      } else {
        // Skip token for error recovery.
        getNextToken();
      }
    }
    
    void HandleExtern() {
      if (ParseExtern()) {
        fprintf(stderr, "Parsed an extern\n");
      } else {
        // Skip token for error recovery.
        getNextToken();
      }
    }
    
    void HandleTopLevelExpression() {
      // Evaluate a top-level expression into an anonymous function.
      if (ParseTopLevelExpr()) {
        fprintf(stderr, "Parsed a top-level expr\n");
      } else {
        // Skip token for error recovery.
        getNextToken();
      }
    }
    
    /// top ::= definition | external | expression | ';'
    void MainLoop() {
      while (true) {
        fprintf(stderr, "ready> ");
        switch (CurTok) {
        case tok_eof:
          return;
        case ';': // ignore top-level semicolons.
          getNextToken();
          break;
        case tok_def:
          HandleDefinition();
          break;
        case tok_extern:
          HandleExtern();
          break;
        default:
          HandleTopLevelExpression();
          break;
        }
      }
    }
    
    //===----------------------------------------------------------------------===//
    // Main driver code.
    //===----------------------------------------------------------------------===//
    
    int main() {
      // Install standard binary operators.
      // 1 is lowest precedence.
      BinopPrecedence['<'] = 10;
      BinopPrecedence['+'] = 20;
      BinopPrecedence['-'] = 20;
      BinopPrecedence['*'] = 40; // highest.
    
      // Prime the first token.
      fprintf(stderr, "ready> ");
      getNextToken();
    
      // Run the main "interpreter loop" now.
      MainLoop();
    
      return 0;
    }
    
}
