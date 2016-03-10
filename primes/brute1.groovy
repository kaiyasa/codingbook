
import static java.lang.Math.*

/*
 * Find primes to user specified limit using a brute-force
*  divide-and-check method.
 */
class BruteForce {
    static void main(String[] args) {
        def app = new BruteForce()
            try {
                int n = (args) ? args[0].toInteger() : 100
                println("Prime list:\n${app.find(n)}")
            } catch (NumberFormatException e) {
                println """
usage: groovy brute1.groovy <number>

Uses a brute-force method to find primes to <number>
"""
            }
    }

    List find(int n) {
        return allPossible(n).findAll { isPrime(it) }
    }

    List allPossible(int n) {
        if (n < 2)
            throw new IllegalArgumentException("n must be 2 or greater")
        return (2 .. n).findAll { it == 2 || it % 2 != 0 }
    }

    boolean isPrime(int p) {
        return p < 2**2 ? true : allPossible(upperBound(p)).every { p % it != 0 }
    }

    int upperBound(int p) {
        return round(floor(sqrt(p)))
    }
}
