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
    private static final List<String> symbols = Arrays.asList("-","@","!","#","+","*","?");
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
            return symbols.get(new Random().nextInt(symbols.size()-1));
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

    /**
     * Method to generate a hash from a String using the algorithm Sha-256
     * @param base string base
     * @return a String with the Sha-256 hash
     * @throws NoSuchAlgorithmException This exception is thrown if the cryptographic algorithm SHA-256 is not available in the environment.
     */
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

    /**
     * Method to generate a random code with a specific pattern and add a current time millis at the end
     * @param pattern a sequence of characters as a pattern to generate a code.
     *                use any uppercase or lowercase letter, any number or symbol.
     *                Pattern example XXXX-0000-xxxx-0X0X
     * @param addTime boolean true to add the millis at the end of the code
     * @return a string with the generated code.
     */
    public static String generateCode(String pattern, boolean addTime) {
        String result = generate(pattern);
        if (addTime) result += System.currentTimeMillis();
        return result.substring(0, result.length()-3);
    }

    /**
     * Method to generate a random code with a specific pattern
     * @param pattern a sequence of characters as a pattern to generate a code.
     *                use any uppercase or lowercase letter, any number or symbol.
     *                Pattern example XXXX-0000-xxxx-0X0X
     * @return a string with the generated code.
     */
    public static String generateCode(String pattern) {
        return generate(pattern);
    }

    /**
     * Method to generate a random code with possibilities to customize avoiding numbers and/or symbols.
     * @param length the code's length
     * @param optionals Allow uppercase letter, lowercase letter, number and symbols in that specific order.
     *                  Set's a boolean value to true or false.
     *                  Ex. generate a random code only numbers with a 10 digits length.
     *                  generateRandomCode(10, false, false, true, false)
     *                  If no boolean value is set, all non specified value will be considered as true.
     * @return a String type with the generated code.
     */
    public static String generateRandomCode(Integer length, boolean ... optionals) {
        length = Math.abs(length);
        StringBuilder pattern = new StringBuilder();
        List<Integer> types = new ArrayList<>();
        if (optionals.length > 0) {
            for (int j = 0; j < CodeType.values().length; j++) {
                if (j < optionals.length) {
                    if (optionals[j]) types.add(j);
                } else types.add(j);
                if ((j == CodeType.values().length-1) && (types.size() == 0)) {
                    types = Arrays.asList(0,1,2,3);
                }
            }
        }
        else types = Arrays.asList(0,1,2,3);
        for (int i = 0; i < length; i++) {
            pattern.append(getRandomString(CodeType.values()[types.get(new Random().nextInt(types.size()))]));
        }
        return generateCode(pattern.toString());
    }

}
