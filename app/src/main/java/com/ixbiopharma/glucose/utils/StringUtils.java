package com.ixbiopharma.glucose.utils;

import java.util.Locale;

/**
 * StringUtils
 *
 * Created by ivan on 02.05.17.
 */

public class StringUtils {

    public static String format(float val) {
        String s;

        s = String.format(Locale.getDefault(), "%.2f", val);

        try {
            if (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }

            if (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }

            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }

            if (s.endsWith(",")) {
                s = s.substring(0, s.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
