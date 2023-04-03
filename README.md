Converter for [tensor](https://github.com/datahaki/tensor) to [Symja](https://github.com/axkr/symja_android_library) objects.
 
```java
package org.matheclipse.tensor.system;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.tensor.ConvertTensor;
import ch.alpine.tensor.Tensor;

public class TensorTest extends AbstractTestCase {

  public TensorTest(String name) {
    super(name);
  }

  public void testConvert() {
    Tensor tensor = ConvertTensor.expr2Tensor(
        F.List(F.C0, F.CD1, F.C1D2, F.CI, F.CNI, F.CDI, F.CDNI, F.stringx("Hello World")));
    assertEquals(//
        "{0, 1.0, 1/2, I, -I, 1.0*I, -1.0*I, Hello World}", //
        tensor.toString());
    IExpr expr = ConvertTensor.tensor2Expr(tensor);
    assertEquals(//
        "{0,1.0,1/2,I,-I,I*1.0,I*(-1.0),Hello World}", //
        expr.toString());
  }
}
```

