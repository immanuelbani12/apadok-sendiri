package com.apadok.emrpreventive.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringToTimeStampFormatting {
    public static String changeFormat(String time, String inputPattern, String outputPattern) {
        Locale locale = new Locale("in", "ID");
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, locale);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, locale);
        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}