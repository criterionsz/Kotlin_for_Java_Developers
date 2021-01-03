package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger


class Rational(rational: String) : Comparable<Rational> {
    private var numerator: BigInteger
    private var denominator: BigInteger

    init {
        val res = rational.split('/')
        numerator = BigInteger(res[0])
        if (res.size == 1)
            denominator = BigInteger("1")
        else {
            if (res[1] == "0")
                throw IllegalArgumentException()
            denominator = BigInteger(res[1])
        }
        normalized()
    }

    private fun normalized() {
        val res = numerator.gcd(denominator)
        numerator = numerator.divide(res)
        denominator = denominator.divide(res)
        if (denominator < BigInteger("0") && (numerator < BigInteger("0") || numerator > BigInteger("0"))) {
            numerator *= BigInteger("-1")
            denominator *= BigInteger("-1")
        }
    }

    override fun toString(): String {
        if (denominator == BigInteger("1"))
            return "$numerator"
        return "$numerator/$denominator"
    }

    operator fun plus(other: Rational): Rational {
        val lcm = (other.denominator * this.denominator)
        val numerator = other.numerator * (lcm / other.denominator) + this.numerator * (lcm / this.denominator)
        return Rational("$numerator/$lcm")
    }

    operator fun times(other: Rational): Rational =
            Rational("${this.numerator * other.numerator}/${this.denominator * other.denominator}")

    operator fun div(other: Rational) = Rational("${this.numerator * other.denominator}/${this.denominator * other.numerator}")

    //TODO all methods
    operator fun minus(other: Rational): Rational {
        val lcm = (other.denominator * this.denominator)
        val numerator = this.numerator * (lcm / this.denominator) - other.numerator * (lcm / other.denominator)
        return Rational("$numerator/$lcm")
    }

    override operator fun compareTo(other: Rational): Int {
        val lcm = (other.denominator * this.denominator)
        return when {
            this == other -> 0
            this.numerator * (lcm / this.denominator) > other.numerator * (lcm / other.denominator) -> 1
            else -> -1
        }
    }

    operator fun rangeTo(other: Rational): ClosedRange<Rational> {
        return object : ClosedRange<Rational> {
            override val endInclusive: Rational
                get() = other
            override val start: Rational
                get() = this@Rational

        }
    }

    operator fun contains(other: Rational) = true
    operator fun unaryMinus() = Rational("${this.numerator * BigInteger("-1")}/${this.denominator}")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        if (numerator != other.numerator) return false
        if (denominator != other.denominator) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }


}

infix fun BigInteger.divBy(denominator: BigInteger) = Rational("$this/$denominator")
infix fun Int.divBy(denominator: Int): Rational = Rational("$this/$denominator")
infix fun Long.divBy(denominator: Long): Rational = Rational("$this/$denominator")
fun String.toRational() = Rational(this)

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}
