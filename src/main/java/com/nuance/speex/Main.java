package com.nuance.speex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File f = new File("d:/Speex16K.speex_wb");
        if (f.exists()) {
            try {
                FileInputStream fi = new FileInputStream(f);
                byte[] data = new byte[fi.available()];
                fi.read(data);
                SpeexReader reader = new SpeexReader();
                List<SpeexFrame> frameList =  reader.read(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
