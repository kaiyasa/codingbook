
class Cursor {
  Cursor(String text) {
    data = text
    n = 0
  }

  boolean hasInput() {
    return n <= data.size() - 1
  }

  Cursor next() {
    if (hasInput())
        ++n
    return this
  }

  char peek() {
    return data[n]
  }

  String data
  int n

  static void main(String... args) {
    def cursor = { text = "" ->
        return new Cursor(text)
    }

    println "Testing ${Cursor.name}"
    assert false == cursor().hasInput()
    assert true  == cursor("a").hasInput()
    assert 'a'   == cursor("a").peek()
    assert false == cursor("a").next().hasInput()
  }
}
