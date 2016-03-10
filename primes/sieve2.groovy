
/*
 * Sieve of Eratosthenes
 *   Sieve of Eratosthenes
 *
 *  More conceptual than optimized (due to use of general purpose Lists)
 */

class SieveOfEratosthenes {
    static void main(String[] args) {
        int n = 100
        try {
            args ? args[0].toInteger() : 100
        } catch (NumberFormatException e) {
println """
usage: groovy sieve1.groovy <number>
"""
            System.exit(0)
        }

        def app = new SieveOfEratosthenes()
        if (n < 2)
            throw new IllegalArgumentException("n must be 2 or greater")
        println("Prime list:\n${app.find(n)}");
    }

    List find(int n) {
        // make buffer and initialize (maps 2, 3, 5, 7, ... to indecies
        boolean[] sieve = new boolean[indexFor(n) + 1]
        (0 ..< sieve.size()).each { i -> sieve[i] = true }

        // mark all multiples of primes to false
        int head = 0, v
        while ((v = valueFor(head++))**2 <= n) {
            (v*2 .. n).step(v)
                .grep { isOdd(it) }
                .each { sieve[indexFor(it)] = false }
        }

        return (0 ..< sieve.size()).inject([]) { primes, i ->
            (sieve[i] && valueFor(i) <= n) ? primes << valueFor(i) : primes
        }
    }

    boolean isOdd(int n) {
        return n % 2 == 1
    }

    int valueFor(int n) {
        return n == 0 ? 2 : (n << 1) + 1
    }

    int indexFor(int n) {
        return n == 2 ? 0 : n >> 1
    }
}
