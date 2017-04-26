
class Interpreter {
  Interpreter(String text) {
    this.scanner = new Scanner(text)
  }

  def evaluate() {
    next()
    def prg = expr()
  }

  boolean match(String seek) {
      boolean result = seek == current

      if (result)
          token()
      return result
  }

  def expr() {
    boolean close = false

    if (match("(")) {
      close = true
      expr()
    }

    sexpr()

    if (close)
      match(")")
  }

  String next() {
      current = scanner.word()
  }

  String current
  Scanner scanner
}
