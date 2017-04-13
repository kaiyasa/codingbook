
int fib(int n) {
    if (n < 0)
        throw new IllegalArgumentException()

    if (n < 2)
        return n

    int a = 0, b = 1, r
    (2 .. n).each {
        r = a + b
        a = b
        b = r
    }
    return r
}

int n = args[0] as int
println "fib($n) = ${fib(n)}"
