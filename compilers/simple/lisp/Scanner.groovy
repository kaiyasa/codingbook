
class Scanner {
  Scanner(String text = "") {
    this(new Cursor(text))
  }

  Scanner(Cursor cursor) {
    this.cursor = cursor
  }

  void skipWS() {
    while (cursor.hasInput())
      if (Character.isWhitespace(cursor.peek()))
        cursor.next()
      else
        return
  }

  String primary() {
    StringBuilder result = new StringBuilder()

    while (cursor.hasInput() && !Character.isWhitespace(cursor.peek()))
      result.append(match())

    return result.toString()
  }

  String word() {
    skipWS()
    while (cursor.hasInput()) {
      char ch = cursor.peek()
      if (ch == '(' || ch == ')')
        return match()

      return primary()
    }
    return null
  }

  char match() {
    char ch = cursor.peek()
    cursor.next()
    return ch
  }

  static void main(String... args) {
    def sut
    def scanner = { text = "" -> sut = new Scanner(text) }

    println "Testing ${Scanner.name}"

    assert null == scanner().word()
    assert "a" == scanner("a").word()
    assert "ab" == scanner(" ab").word()

    assert "a" == scanner("a b").word()
    assert "b" == sut.word()
    assert null == sut.word()

    assert "(" == scanner("(a )  ").word()
    assert "a" == sut.word()
    assert ")" == sut.word()
    assert null == sut.word()
  }

  Cursor cursor
}
