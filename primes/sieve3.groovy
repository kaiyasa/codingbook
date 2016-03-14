
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
            n = args ? args[0].toInteger() : 100
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
        if (n == 2)
            return [2]

        int size = indexFor(n) + 1
        boolean[] sieve = new boolean[size]
        (0 ..< size).each { i -> sieve[i] = true }

        int h = 0, v = 3
        while (v**2 <= n) {
            if (sieve[h])
                // mark 3rd, 5th, etc. multiples of prime to false
                ((h + v) ..< size).step(v)
                    .each { sieve[it] = false }
            h += 1
            v += 2
        }

        return (0 ..< size).inject([2]) { primes, i ->
            v = valueFor(i)
            (sieve[i] && v <= n) ? primes << v : primes
        }
    }

    // reverse index values into the odd number series from 3 and on
    int valueFor(int i) {
        return  1 + ((i + 1) << 1)
    }

    // map values odd numbers starting at 3 to 0, 1, 2, etc..
    int indexFor(int n) {
        return (n >> 1) - 1
    }
}
