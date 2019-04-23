/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Marc de Verdelhan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.bod.nfcMusic.sky.rfid;

import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Hexadecimal utility class.
 */
public final class HexUtils {

    /** Regex pattern for hexadecimal strings */
    private static final Pattern HEX_STRING_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2})+$");
    
    /** Array of all hexadecimal chars */
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private final static int BIT_LENGT = 16;
    private final static String LEADING_ZEROS = "0000000000000000";
    
    private HexUtils() {
    }

    public static String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes()));
    }


    /**
     * @param s a string
     * @return true if the provided string is hexadecimal, false otherwise
     */
    public static boolean isHexString(String s) {
        return (s != null) && HEX_STRING_PATTERN.matcher(s).matches();
    }
    
    /**
     * @param s a hex string
     * @return a byte array
     */
    public static byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * @param bytes a byte array
     * @return a hex string
     */
    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_CHARS[v >>> 4];
            hexChars[i * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        //Remove leading Zeros
        String value = output.toString().replaceFirst("^0+(?!$)", "");
        return value;
    }

    public static String asciiToHex(String stringData)
    {
        int length = stringData.length();
        int i1 = BIT_LENGT - length;
        if(i1 > 0 ){
            stringData = (LEADING_ZEROS + stringData).substring(length);
        }
        char[] chars = stringData.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }
}