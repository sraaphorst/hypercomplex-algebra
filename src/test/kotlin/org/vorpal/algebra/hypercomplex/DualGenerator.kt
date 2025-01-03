// DualGenerator.ks
// By Sebastian Raaphorst, 2025.

package org.vorpal.algebra.hypercomplex

import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.double

private const val MIN_DOUBLE = -1000.0
private const val MAX_DOUBLE =  1000.0
private val doubleArb = Arb.double(min=MIN_DOUBLE, max=MAX_DOUBLE)

val dualArb: Arb<Dual> = Arb.bind(doubleArb, doubleArb)
{ real, epsilon -> Dual(real, epsilon) }
