package org.matheclipse.tensor;

import java.math.BigInteger;
import org.hipparchus.fraction.BigFraction;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.S;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IASTAppendable;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.IRational;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;
import ch.alpine.tensor.AbstractTensor;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.io.StringScalar;

public class ConvertTensor {
  public static Tensor expr2Tensor(final IExpr expr) throws ClassCastException {
    if (expr instanceof IAST) {
      if (expr.isList()) {
        IAST list = (IAST) expr;
        Tensor[] tensors = new Tensor[expr.argSize()];
        for (int i = 1; i < list.size(); i++) {
          tensors[i - 1] = expr2Tensor(list.get(i));
        }
        return Tensors.of(tensors);
      }
    } else if (expr instanceof IInteger) {
      IInteger integerNumber = (IInteger) expr;
      return RationalScalar.of(integerNumber.toBigNumerator(), BigInteger.ONE);
    } else if (expr instanceof IFraction) {
      IFraction fractionNumber = (IFraction) expr;
      return RationalScalar.of(fractionNumber.toBigNumerator(), fractionNumber.toBigDenominator());
    } else if (expr instanceof IComplex) {
      IComplex complexNumber = (IComplex) expr;
      RealScalar realPart = (RealScalar) expr2Tensor(complexNumber.getRealPart());
      RealScalar imaginaryPart = (RealScalar) expr2Tensor(complexNumber.getImaginaryPart());
      return ComplexScalar.of(realPart, imaginaryPart);
    } else if (expr instanceof INum) {
      INum number = (INum) expr;
      return DoubleScalar.of(number.doubleValue());
    } else if (expr instanceof IComplexNum) {
      IComplexNum number = (IComplexNum) expr;
      return ComplexScalar.of(Double.valueOf(number.reDoubleValue()), //
          Double.valueOf(number.imDoubleValue()));
    } else if (expr instanceof ISymbol) {
      if (expr==S.False) {
      } else if (expr == S.True) {
      }
    } else if (expr instanceof IStringX) {
      IStringX str = (IStringX) expr;
      return StringScalar.of(str.toString());
    }
    throw new ClassCastException(expr.toString());
  }

  public static IExpr tensor2Expr(final Tensor expr) throws ClassCastException {
    if (expr instanceof AbstractTensor) {
      AbstractTensor tensor = (AbstractTensor) expr;
      IASTAppendable list = F.ListAlloc(tensor.length());
      for (int i = 0; i < tensor.length(); i++) {
        list.append(tensor2Expr(tensor.get(i)));
      }
      return list;
    } else if (expr instanceof RationalScalar) {
      RationalScalar rationalNumber = (RationalScalar) expr;
      BigInteger numerator = rationalNumber.numerator();
      BigInteger denominator = rationalNumber.denominator();
      if (denominator.equals(BigInteger.ONE)) {
        return F.ZZ(numerator);
      }
      return F.QQ(new BigFraction(numerator, denominator));
    } else if (expr instanceof DoubleScalar) {
      DoubleScalar doubleNumber = (DoubleScalar) expr;
      return F.num(doubleNumber.number().doubleValue());
    } else if (expr instanceof ComplexScalar) {
      ComplexScalar complexNumber = (ComplexScalar) expr;
      Scalar real = complexNumber.real();
      Scalar imag = complexNumber.imag();
      if (real instanceof RationalScalar && imag instanceof RationalScalar) {
        RationalScalar rationalReal = (RationalScalar) real;
        RationalScalar rationalImag = (RationalScalar) imag;
        IRational realRational = (IRational) tensor2Expr(rationalReal);
        IRational imagRational = (IRational) tensor2Expr(rationalImag);
        return F.complex(realRational, imagRational);
      }
      return F.complexNum(real.number().doubleValue(), imag.number().doubleValue());
    } else if (expr instanceof StringScalar) {
      StringScalar stringScalar = (StringScalar) expr;
      return F.stringx(stringScalar.toString());
    }
    throw new ClassCastException(expr.toString());
  }

}
