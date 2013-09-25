package by.oshmianski.utils;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 15.04.13
 * Time: 14:49
 */
public class StringManager {
    public static String Right(String text, int length) {
        int len = text.length() - length;
        int lengthIn = (length + len) > text.length() ? text.length() : (length + len);
        return text.substring(len < 0 ? 0 : len, lengthIn);
    }
}
