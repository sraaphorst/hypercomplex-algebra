// Dual.kt
// By Sebastian Raaphorst, 2025.

package org.vorpal.algebra.hypercomplex

import kotlin.math.ln
import kotlin.math.pow

/**
 * Represents a dual number, a hypercomplex number often used in automatic differentiation
 * and other mathematical applications. Dual numbers consist of a "real" part and an
 * "epsilon" or infinitesimal part, where epsilon^2 = 0.
 *
 * @property real The real component of the dual number.
 * @property epsilon The infinitesimal component of the dual number.
 */
data class Dual(val real: Double, val epsilon: Double): HypercomplexAlgebra<Dual> {
    constructor(real: Int, epsilon: Double): this(real.toDouble(), epsilon)
    constructor(real:Double, epsilon: Int): this(real, epsilon.toDouble())
    constructor(real: Int, epsilon: Int): this(real.toDouble(), epsilon.toDouble())

    override fun isZero(): Boolean = real == 0.0 && epsilon == 0.0
    override fun isUnity(): Boolean = real == 1.0 && epsilon == 0.0

    override fun unaryMinus(): Dual = Dual(-real, -epsilon)
    override fun plus(other: Dual): Dual = Dual(real + other.real, epsilon + other.epsilon)
    override fun minus(other: Dual): Dual = Dual(real - other.real, epsilon - other.epsilon)
    override fun times(other: Dual): Dual = Dual(real * other.real - epsilon * other.epsilon, real * other.epsilon + epsilon * other.real)
    override fun div(other: Dual): Dual = times(other.conjugate())
    override fun conjugate(): Dual = Dual(real, -epsilon)
    override fun norm(): Double = real * real + epsilon * epsilon

    override operator fun plus(scalar: Int) = Dual(real + scalar, epsilon)
    override operator fun minus(scalar: Int) = Dual(real - scalar, epsilon)
    override operator fun times(other: Int) = Dual(real * other, epsilon * other)
    override operator fun div(other: Int) = Dual(real / other, epsilon / other)

    override operator fun plus(scalar: Double) = Dual(scalar + real, epsilon)
    override operator fun minus(scalar: Double) = Dual(scalar - real, epsilon)
    override operator fun times(other: Double) = Dual(other * real, other * epsilon)
    override operator fun div(other: Double) = Dual(real / other, epsilon / other)

    override fun pow(exponent: Int): Dual = pow(exponent.toDouble())

    override fun pow(exponent: Double): Dual {
        require(this.real > 0) { "Real part of this Dual number cannot be zero to calculate the exponential." }

        val (a, b) = this

        val realPart = a.pow(exponent)
        val dualPart = (exponent * b / a) * realPart
        return Dual(realPart, dualPart)
    }

    /**
     * Raises this Dual number to the power of another Dual number.
     * To calculate (a + be)^(c + de), we use the Taylor series expansion and properties of Dual numbers.
     * (a + be)^(c + de) = exp((c + de) * ln(a + be)) via exponential-logarithm equivalence.
     * ln(a + be) = ln(a) + (b/a)e -> since e^2 = 0, higher order terms vanish
     * (c + de) * ln(a + be) = c ln(a) + (d ln(a) + cb/a) e
     *
     * We apply the exponentiation function:
     * exp(c ln(a) + (d ln(a) + cb/a) e)
     * = a^c + (d ln(a) + cb/a) a^c e, higher order terms again vanish
     *
     * Finally:
     * (a + be)^(c + de) = a^c + (cb / a + d ln(a)) a^c e
     *
     * Note that it is assumed that a > 0 to ensure ln(a) is defined, and a and cannot be 0.
     *
     * @param exponent The Dual number representing the exponent.
     * @return A new Dual number representing the result of the exponentiation.
     */
    override fun pow(exponent: Dual): Dual {
        if (real == 0.0)
            if (exponent.real > 0) return Zero
            else throw IllegalArgumentException("Undefined: 0 raised to non-positive exponent")
        require(this.real > 0) { "Real part of this Dual number cannot be zero to calculate the exponential." }

        val (a, b) = this
        val (c, d) = exponent
        val realPart = a.pow(c)
        val epsilonPart = (c * b / a + d * ln(a)) * realPart
        return Dual(realPart, epsilonPart)
    }

    override fun invert(): Dual {
        require(real != 0.0) { "Real part of this Dual number cannot be zero to calculate the inverse." }
        return Dual(1.0 / real, -epsilon / real.pow(2))
    }

    override fun toString(): String =
        "$real + ${epsilon}e"

    companion object {
        val E = Dual(0.0, 1.0)
        val DualUnit = Dual(0.0, 1.0)
        val Zero = Dual(0.0, 0.0)
        val One = Dual(1.0, 0.0)
    }
}

operator fun Double.plus(dual: Dual) = dual + this
operator fun Double.minus(dual: Dual) = -dual + this
operator fun Double.times(dual: Dual) = dual * this
operator fun Double.div(dual: Dual) = this * dual.invert()
operator fun Int.plus(dual: Dual) = toDouble() + dual
operator fun Int.minus(dual: Dual) = toDouble() - dual
operator fun Int.times(dual: Dual) = toDouble() * dual
operator fun Int.div(dual: Dual) = this.toDouble() / dual
