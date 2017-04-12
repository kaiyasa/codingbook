
int lcd(int m, int n) {
    if (m == 0 || n == 0)
        throw new IllegalArgumentException()

    int r = m
    while (r > 1) {
      m = n
      n = r
      r = m % n
    }
    return (r == 1) ? 1 : n
}

println "LCD = ${lcd (args[0] as int, args[1] as int)}"
