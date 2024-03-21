package com.e2x.bigcommerce.routinefinder.enquiry;

import com.google.common.base.Strings;

import static com.google.common.base.CharMatcher.is;

public class TextUtils {
    public static String trim(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return value;
        }

        return is('\u00A0').trimFrom(value).trim();
    }

    public static String replaceWhitespace(String value) {
        return is('\u00A0').replaceFrom(value, ' ');
    }

    public static String removeDuplicateWhitespace(String value) {
        return value.replace("  ", " ");
    }
}
