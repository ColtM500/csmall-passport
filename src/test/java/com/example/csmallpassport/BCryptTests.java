package com.example.csmallpassport;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTests {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);

    @Test
    void encode(){
        String rawPassword = "123456";
        System.out.println("原文： "+ rawPassword);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 13; i++) {
            passwordEncoder.encode(rawPassword);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }

    @Test
    void matches(){
        String rawPassword = "123456";
        System.out.println("原文: "+rawPassword);

        String encodedPassword = "$2a$10$ZWwzVRwZGI/aKEmgcrwid.Ch6pKySRgtQrSMQBf3YNGeTDSiGIKQq";
        System.out.println("密文： "+ encodedPassword);

        boolean result = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("对比结果： "+result);
    }
}
