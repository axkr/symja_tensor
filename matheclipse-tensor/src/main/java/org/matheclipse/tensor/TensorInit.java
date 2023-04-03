package org.matheclipse.tensor;

import org.matheclipse.core.basic.Config;
import org.matheclipse.gpl.numbertheory.BigIntegerPrimality;

public class TensorInit {
  public static void init() {
    // set for only small prime factorization
    // Config.PRIME_FACTORS = new Primality();

    // set for BigInteger prime factorization
    Config.PRIME_FACTORS = new BigIntegerPrimality();

  }
}
