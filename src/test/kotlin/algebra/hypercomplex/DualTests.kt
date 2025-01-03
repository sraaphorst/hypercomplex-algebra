// DualTests.ks
// By Sebastian Raaphorst, 2025.

package algebra.hypercomplex

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.forAll
import org.vorpal.algebra.hypercomplex.algebra.hypercomplex.Dual

class DualTests : StringSpec({
    "Dual addition should be commutative" {
        forAll(dualArb, dualArb) { a, b ->
            a + b == b + a
        }
    }

    "Unary minus should map to another value unless zero, and should cancel self" {
        forAll(dualArb, dualArb) { a, b ->
            ((a == Dual.Zero) || (-a != a)) && (-(-a) == a)
        }
    }
})