package com.waihon.springboot.thymeleaf.jobportal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static Integer extractLastNumber(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile(".*/(\\d+)$"); // 🔥 Notice the $ end anchor
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return null; // No number found at the end
    }

}
