package com.lotu_us.usedbook.config;

public class PasswordRegexpConfig {
    private static final String allowNumber = "(?=.*[0-9])";
    private static final String allowEnglish = "(?=.*[a-z])";
    private static final String allowSpecial = "(?=.*[!\"#$%&'()*+,-./:;<=>?@^_`{|}\\[\\]~\\\\])(?=\\S+$)";
    private static final String NotAllowSpace = "(?=\\S+$)";
    public static final String reg = "^"+allowNumber+allowEnglish+allowSpecial+NotAllowSpace+".{5,20}$";
}
