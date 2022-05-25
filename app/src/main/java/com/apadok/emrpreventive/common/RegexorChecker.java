package com.apadok.emrpreventive.common;

public class RegexorChecker {

    public boolean DateRegex(String str) {
        String expression = "^(0[1-9]|1[0-2]|[1-9])/([0-9]{4})$";
        return str.matches(expression);
    }

    public boolean HeightChecker(String str) {
        try {
            int val = Integer.parseInt(str);
            return val > 39 && val < 301;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean WeightChecker(String str) {
        try {
            int val = Integer.parseInt(str);
            return val > 19 && val < 651;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean HipsChecker(String str) {
        try {
            int val = Integer.parseInt(str);
            return val > 19 && val < 301;
        } catch (NumberFormatException e){
            return false;
        }
    }
    public boolean PhoneChecker(String str) {
        if (str.length() > 8 && str.length() < 17){
            String expression = "\\+?([ -]?\\d+)+|\\(\\d+\\)([ -]\\d+)";
            return str.matches(expression);
        } else {
            return false;
        }
    }

    public boolean NameRegex(String str) {
        String expression = "(([A-Z]\\.?\\s?)*([A-Z][a-z]+\\.?\\s?)+([A-Z]\\.?\\s?[a-z]*)*)";
        return str.matches(expression);
    }
}
