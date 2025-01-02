// HypercomplexAlgebra.kt
// By Sebastian Raaphorst, 2025.

package org.vorpal.algebra.hypercomplex

interface HypercomplexAlgebra<T : HypercomplexAlgebra<T>> {
    fun isZero(): Boolean
    fun isUnity(): Boolean

    operator fun unaryMinus(): T
    operator fun plus(other: T): T
    operator fun minus(other: T): T
    operator fun times(other: T): T
    operator fun div(other: T): T

    operator fun plus(other: Double): T
    operator fun minus(other: Double): T
    operator fun times(other: Double): T
    operator fun div(other: Double): T

    operator fun plus(scalar: Int): T
    operator fun minus(scalar: Int): T
    operator fun times(other: Int): T
    operator fun div(o: Int): T

    fun pow(other: Int): T
    fun pow(other: Double): T
    fun pow(other: T): T

    fun invert(): T
    fun conjugate(): T
    fun norm(): Double
}