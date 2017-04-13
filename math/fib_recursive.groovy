
int fib(int n) {
    if (n < 0)
        throw new IllegalArgumentException()
    return (n < 2) ? n : fib(n - 1) + fib(n - 2)
}

int n = args[0] as int
println "fib($n) = ${fib(n)}"
