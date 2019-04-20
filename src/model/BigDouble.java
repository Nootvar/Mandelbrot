package model;

public class BigDouble {

    private int exponent;
    private long significand;

    public BigDouble(double value) {

    }

    public BigDouble(int value) {
        this.exponent = 0;
        this.significand = value;
    }

    private BigDouble(int exponent, long significand) {
        this.exponent = exponent;
        this.significand = significand;
    }

    public BigDouble add(BigDouble other) {
        int newExponent;
        long newSignificand;
        int exponentDiff = Math.abs(exponent - other.exponent);
        int thisSignificandLenght = (int) (Math.log10(significand) + 1);
        int otherSignificandLength = (int) (Math.log10(other.significand) + 1);
        if (exponent < other.exponent) {
            newExponent = other.exponent;
            thisSignificandLenght += exponentDiff;
        } else if (exponent > other.exponent) {
            newExponent = exponent;
            otherSignificandLength += exponentDiff;
        } else {
            newExponent = exponent;
        }
        int significandDiff = Math.abs(thisSignificandLenght - otherSignificandLength);
        if (thisSignificandLenght < otherSignificandLength) {
            newSignificand = significand*10*significandDiff + other.significand;
        } else if (thisSignificandLenght > otherSignificandLength) {
            newSignificand = significand + other.significand*10*significandDiff;
        } else {
            newSignificand = significand + other.significand;
        }
        return new BigDouble(newExponent, newSignificand);
    }

    public BigDouble subtract(BigDouble other) {
        int newExponent;
        long newSignificand;
        int exponentDiff = Math.abs(exponent - other.exponent);
        int thisSignificandLenght = (int) (Math.log10(significand) + 1);
        int otherSignificandLength = (int) (Math.log10(other.significand) + 1);
        if (exponent < other.exponent) {
            newExponent = other.exponent;
            thisSignificandLenght += exponentDiff;
        } else if (exponent > other.exponent) {
            newExponent = exponent;
            otherSignificandLength += exponentDiff;
        } else {
            newExponent = exponent;
        }
        int significandDiff = Math.abs(thisSignificandLenght - otherSignificandLength);
        if (thisSignificandLenght < otherSignificandLength) {
            newSignificand = significand*10*significandDiff - other.significand;
        } else if (thisSignificandLenght > otherSignificandLength) {
            newSignificand = significand - other.significand*10*significandDiff;
        } else {
            newSignificand = significand - other.significand;
        }
        return new BigDouble(newExponent, newSignificand);
    }

    public BigDouble multiply(BigDouble other) {
        int newExponent;
        long newSignificand;
        newExponent = exponent + other.exponent;
        newSignificand = significand * other.significand;
        return other;
    }

    public BigDouble divide(BigDouble other) {
        return null;
    }

    public double doubleValue() {
        return significand * Math.pow(10,exponent);
    }

    public int compareTo(BigDouble bigDecimal) {
        return 0;
    }
}
