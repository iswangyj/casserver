package com.test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @author SxL
 * Created on 9/25/2018 1:07 PM.
 */
public class SlirnTest {
    public SlirnTest() {
    }

    public static void main(String[] args) throws IOException {
        String str = "姹夊瓧";
        String strEncoder = (new BASE64Encoder()).encode(str.getBytes("utf-8"));
        System.out.println("_____strEncoder___" + strEncoder);
        String strDecoder = new String((new BASE64Decoder()).decodeBuffer(strEncoder), "utf-8");
        System.out.println("_____strDecoder___" + strDecoder);
    }
}
