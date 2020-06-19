package com.jp.ib.util;

import java.math.BigDecimal;

final public class ValidateUtils {

    public static final String VALUE_MUST_NOT_BE_NULL = "Value must not be Null";

    private ValidateUtils() {
    }

    public static void notNull(Object object, String message) {
        if (object == null)
            throw new IllegalArgumentException(message);
        if (object instanceof String && ((String) object).isEmpty() ) {
            throw new IllegalArgumentException(message);

        }
    }

    public static void isGreaterThenZero(BigDecimal stockPrice, String message) {
        notNull(stockPrice, VALUE_MUST_NOT_BE_NULL);
        if (stockPrice.compareTo(BigDecimal.ZERO) != 1)
            throw new IllegalArgumentException(message);
    }
}