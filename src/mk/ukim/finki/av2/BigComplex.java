package mk.ukim.finki.av2;

import java.math.BigDecimal;

public class BigComplex {

    private BigDecimal realPart;
    private BigDecimal imaginaryPart;

    public BigComplex(BigDecimal realPart,BigDecimal imaginaryPart){
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public BigComplex add(BigComplex complexNumber){
        BigDecimal realPart = this.realPart.add(complexNumber.realPart);
        BigDecimal imaginaryPart = this.realPart.add(complexNumber.imaginaryPart);
        return new BigComplex(realPart,imaginaryPart);
    }
    public BigComplex subtract(BigComplex complexNumber){
        BigDecimal realPart = this.realPart.subtract(complexNumber.realPart);
        BigDecimal imaginaryPart = this.realPart.subtract(complexNumber.imaginaryPart);
        return new BigComplex(realPart,imaginaryPart);
    }
    public BigComplex multiply(BigComplex complexNumber){
        BigDecimal realPart = this.realPart.multiply(complexNumber.realPart);
        BigDecimal imaginaryPart = this.realPart.multiply(complexNumber.imaginaryPart);
        return new BigComplex(realPart,imaginaryPart);
    }


}
