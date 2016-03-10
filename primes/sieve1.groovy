
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
        println("Prime list:\n ${app.find(n)}");
    }

    List find(int n) {
        def possibles = allPossible(n)
        def primes = []
        int p

        while ((p = possibles.head())**2 < n) {
            primes << p
            possibles -= (p .. n).step(p)
        }
        return primes + possibles
    }

    List allPossible(int n) {
        return n == 2 ? [2] : [2] + (3 .. n).step(2)
    }
}

