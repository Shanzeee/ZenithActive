package com.brvsk.ZenithActive.review;

public enum Rating {
    STAR_1(1.0),
    STAR_2(2.0),
    STAR_3(3.0),
    STAR_4(4.0),
    STAR_5(5.0);

    private final double numericValue;

    Rating(double numericValue) {
        this.numericValue = numericValue;
    }

    public double getNumericValue() {
        return numericValue;
    }
}
