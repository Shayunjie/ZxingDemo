package com.example.zxingdemo;

import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * Created by bin on 16-9-21.
 */
public class MobileInfo {
    //固件版本号
   static String firmwareVersion = android.os.Build.VERSION.RELEASE;
    //终端版本号
    static String name = Build.MODEL;
    //Android版本号
   static String androidVersion = android.os.Build.VERSION.RELEASE;
    //todo证书信息


    /**
     *获得基带版本
     * @return String
     */
    public static String getBaseBandVersion() {
        String version = null;
        try {
            Class cl = Class.forName("android.os.SystemProperties");

            Object invoker = cl.newInstance();

            Method m = cl.getMethod("get", new Class[] { String.class,String.class });

            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});

            version = (String)result;
        } catch (Exception e) {
        }
        return version;
    }
    /**
     * CORE-VER
     * 内核版本
     * return String
     */

    public static String getLinuxCore_Ver() {
        Process process = null;
        String kernelVersion = "";
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


// get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);


        String result = "";
        String line;
// get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            if (result != "") {
                String Keyword = "version ";
                int index = result.indexOf(Keyword);
                line = result.substring(index + Keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

}
