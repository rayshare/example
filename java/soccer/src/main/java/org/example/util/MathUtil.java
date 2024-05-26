package org.example.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.example.handler.ButtonEvent;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MathUtil {
    private static final int scale = 1000;

    public static String average(ButtonEvent buttonEvent) {
        Double[] values = buttonEvent.getMainFrame().getTextFields().stream()
                .filter(e -> !Objects.equals(e.getForeground(), Color.gray))
                .map(JTextField::getText)
                .filter(StringUtils::isNotBlank)
                .map(Double::parseDouble)
                .toArray(Double[]::new);

        List<Integer> ret = middleValue(values);
        if (CollectionUtils.isEmpty(ret)) {
            return null;
        }
        int sum = ret.stream().mapToInt(e -> e).sum();
        List<String> list0 = ret.stream().map(String::valueOf).toList();
        List<String> list1 = null;
        int gcd = gcd(ret);
        if (gcd != 1) {
            list1 = ret.stream().map(e -> e / gcd).map(String::valueOf).toList();
            if (gcd == scale) {
                list0 = list1;
                list1 = null;
            }
        }
        List<Double> avg = new ArrayList<>(values.length);
        for (int i = 0; i < ret.size(); i++) {
            Integer n1 = ret.get(i);
            Double d1 = values[i];
            avg.add(n1 * d1 / sum);
        }
        List<String> list2 = avg.stream().map(e -> String.format("%.2f", e)).toList();
        return list1 == null ? String.join("\n", list0.toString(), list2.toString()) : String.join("\n", list0 + " " + list1, list2.toString());
    }

    public static List<Integer> middleValue(Double... arr) {
        if (arr.length == 0) {
            return Collections.emptyList();
        }
        if (arr.length == 1) {
            return List.of(1);
        }
        double product = 1;
        for (Double aDouble : arr) {
            if (aDouble <= 0.0) {
                throw new IllegalArgumentException("Input <=0");
            }
            product *= aDouble;
        }
        double finalProduct = product;
        List<Double> list = Arrays.stream(arr).map(e -> finalProduct / e).toList();
        double min = list.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
        return list.stream().map(e -> e / min * scale).map(e -> (int) Math.round(e)).toList();
    }
    
    private static Integer gcd(List<Integer> numbers) {
        if (CollectionUtils.isEmpty(numbers)) {
            return null;
        }
        int gcd = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            gcd = ArithmeticUtils.gcd(gcd, numbers.get(i));
        }
        return gcd;
    }

}
