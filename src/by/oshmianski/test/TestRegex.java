package by.oshmianski.test;

/**
 * Created by vintselovich on 02.10.2014.
 */
public class TestRegex {
    public static void main(String[] args) {
        String str = "224020   Брест, Скрипникова улица, д.15 общ";
        System.out.println(str.replaceAll("(?i)" + "общ$".replaceAll("\\.", "\\\\.").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)"), ""));
    }
}
