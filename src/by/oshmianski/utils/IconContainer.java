package by.oshmianski.utils;

import by.oshmianski.main.AppletWindow;
import by.oshmianski.main.AppletWindowFrame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 02.04.13
 * Time: 15:07
 */
public class IconContainer {
    private static IconContainer ourInstance = new IconContainer();

    public static IconContainer getInstance() {
        return ourInstance;
    }

    private IconContainer() {
    }

    public ImageIcon loadImage(String imageName) {
//        String path = "../../../img/" + imageName;
        String path = "";
        int MAX_IMAGE_SIZE = 64000;  //Change this to the size of your biggest image, in bytes.
        int count = 0;
        BufferedInputStream imgStream = null;

        //TODO: пока не понимаю, как можно из jar'ки в агенте достать картинки. Поэтому картинки должны быть в RES агента!!!
        //Поэтому такой изврат.
        if (AppletWindow.applet != null) {
            path = "../../../img/" + imageName;
            imgStream = new BufferedInputStream(AppletWindow.applet.getClass().getResourceAsStream(path));
        } else {
            path = imageName;
            imgStream = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(path));
        }

        try {
            if (imgStream != null) {
                byte buf[] = new byte[MAX_IMAGE_SIZE];

                try {
                    count = imgStream.read(buf);
                    imgStream.close();
                } catch (IOException ioe) {
                    System.err.println("Couldn't read stream from file: " + path);
                    return null;
                }

                if (count <= 0) {
                    System.err.println("Empty file: " + path);
                    return null;
                }

                return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf));
            } else {
                System.err.println("Couldn't find file: " + path);

                return null;
            }
        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            if (imgStream != null) try {
                imgStream.close();
            } catch (Exception e) {
                MyLog.add2Log(e);
            }
        }

        return null;
    }
}
