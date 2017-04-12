
int lcd(int m, int n) {
    if (m == 0 || n == 0)
        throw new IllegalArgumentException()

    int r = m % n
    if (r > 1)
        return lcd(n, r)
    return (r == 1) ? 1 : n
}

println "LCD = ${lcd (args[0] as int, args[1] as int)}"
