package com.rcastrucci.dev.toolkit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ToolKit {

    private static final List<String> upperCaseList = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
    private static final List<String> lowerCaseList = Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
    private static final List<String> digits = Arrays.asList("0","1","2","3","4","5","6","7","8","9");
    private enum CodeType { UPPER, LOWER, DIGIT, SYMBOL }

    private static CodeType getCodeType(String string) {
        if (upperCaseList.contains(string)) {
            return CodeType.UPPER;
        } else if (lowerCaseList.contains(string)) {
            return CodeType.LOWER;
        } else if (digits.contains(string)) {
            return CodeType.DIGIT;
        } else {
            return CodeType.SYMBOL;
        }
    }

    private static String getRandomString(CodeType codeType) {
        if (codeType.equals(CodeType.UPPER)) {
            return upperCaseList.get(new Random().nextInt(upperCaseList.size()-1));
        } else if (codeType.equals(CodeType.LOWER)) {
            return lowerCaseList.get(new Random().nextInt(lowerCaseList.size()-1));
        } else if (codeType.equals(CodeType.DIGIT)) {
            return digits.get(new Random().nextInt(digits.size()-1));
        } else {
            return null;
        }
    }

    private static String generate(String pattern) {
        ArrayList<String> code = new ArrayList<>();
        for (int i = 0; i < pattern.length(); i++) {
            String string = String.valueOf(pattern.charAt(i));
            CodeType type = getCodeType(string);
            if (type.equals(CodeType.SYMBOL)) code.add(string);
            else code.add(getRandomString(type));
        }
        return String.join("", code);
    }

    public static String sha256(final String base) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
        final StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            final String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String generateCode(String pattern, boolean addTime) {
        String result = generate(pattern);
        if (addTime) result += System.currentTimeMillis();
        return result.substring(0, result.length()-3);
    }

    public static String generateCode(String pattern) {
        return generate(pattern);
    }

}
