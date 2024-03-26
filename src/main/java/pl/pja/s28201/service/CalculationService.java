package pl.pja.s28201.service;

import lombok.SneakyThrows;
import pl.pja.s28201.model.Entry;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CalculationService {

    public static boolean isInteger(String string) {
        if (string == null) return false;
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidLr(double lr) {
        return lr >= 0 && lr <= 1;
    }

    public static boolean isDecimal(String s) {
        if (s == null) return false;
        try {
            new BigDecimal(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean allDecimals(String... decimals) {
        for (String s : decimals) {
            if (!isDecimal(s)) return false;
        }
        return true;
    }

    public static List<BigDecimal> stringsToDecimalList(String[] attributes) {
        List<BigDecimal> decimals = new ArrayList<>();

        for (String d : attributes) {
            decimals.add(new BigDecimal(d));
        }

        return decimals;
    }

    public static boolean isDouble(String str) {
        if (str == null) return false;
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean calcOutput(List<BigDecimal> weights, Entry entry, double theta) {
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < entry.inputCount(); i++) {
            BigDecimal multiply = entry.getInputs().get(i).multiply(weights.get(i));
            sum = sum.add(multiply);
        }
        return sum.doubleValue() >= theta;
    }
}
