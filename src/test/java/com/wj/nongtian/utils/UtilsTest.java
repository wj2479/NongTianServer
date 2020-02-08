package com.wj.nongtian.utils;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class UtilsTest {

    @Test
    public  void dasd(){
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
