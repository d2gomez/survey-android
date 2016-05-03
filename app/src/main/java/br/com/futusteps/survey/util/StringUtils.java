package br.com.futusteps.survey.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String value) {
        boolean empty = false;
        if (value == null || value.isEmpty()) {
            empty = true;
        }
        return empty;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isBlank(String value) {
        return isEmpty(value) || isEmpty(value.trim());
    }

    public static boolean isNotBlank(String value) {
        return isNotEmpty(value) && isNotEmpty(value.trim());
    }

}
