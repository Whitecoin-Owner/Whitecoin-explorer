package com.browser.tools;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/10/29 0029.
 */
public class DecodeMemo {

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    public static byte[] toByteArray(String data) {
        if (data == null) {
            return new byte[]{};
        }

        if (data.length() == 0) {
            return new byte[]{};
        }

        while (data.length() < 2) {
            data = "0" + data;
        }

        if (data.substring(0, 2).equalsIgnoreCase("0x")) {
            data = data.substring(2);
        }
        if (data.length() % 2 != 0) {
            data = "0" + data;
        }

        data = data.toUpperCase(Locale.US);

        byte[] bytes = new byte[data.length() / 2];
        String hexString = new String(HEX_DIGITS);
        for (int i = 0; i < bytes.length; i++) {
            int byteConv = hexString.indexOf(data.charAt(i * 2)) * 0x10;
            byteConv += hexString.indexOf(data.charAt(i * 2 + 1));
            bytes[i] = (byte) (byteConv & 0xFF);
        }

        return bytes;
    }

    public static void main(String[] args){
        System.out.println(new String(toByteArray("68656c6c6f2c204bd5")));
    }
}
