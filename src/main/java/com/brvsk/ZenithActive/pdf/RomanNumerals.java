package com.brvsk.ZenithActive.pdf;

public class RomanNumerals {
    private static final String[] ROMAN_NUMERALS = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public static String toRoman(int number) {
        if (number < 1 || number > 10) {
            throw new IllegalArgumentException("Number out of range (1-10)");
        }
        return ROMAN_NUMERALS[number - 1];
    }
}
