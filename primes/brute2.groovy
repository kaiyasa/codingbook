
import static java.lang.Math.*

/*
 * Find primes to user specified limit using a brute-force
*  divide-and-check method over previous primes
 */
class BruteForce {
    static void main(String[] args) {
        def app = new BruteForce()
            try {
                int n = (args) ? args[0].toInteger() : 100
                println("isPrime list:\n${app.find(n)}")
            } catch (NumberFormatException e) {
                println """
usage: groovy brute2.groovy <number>

Uses a brute-force method to find primes to <number>
"""
            }
    }

    List find(int n) {
        return allPossible(n).inject([]) { primes, v -> isPrime(v, primes) ? primes << v : primes }
    }

    List allPossible(int n) {
        if (n < 2)
            throw new IllegalArgumentException("n must be 2 or greater")
        return (2 .. n).findAll { it == 2 || it % 2 != 0 }
    }

    boolean isPrime(int p, List partial) {
        int limit = upperBound(p)
        List factors = partial.findAll { it <= limit }
        return p < 2**2 ? true : factors.every { p % it != 0 }
    }

    int upperBound(int p) {
        return round(floor(sqrt(p)))
    }
}
