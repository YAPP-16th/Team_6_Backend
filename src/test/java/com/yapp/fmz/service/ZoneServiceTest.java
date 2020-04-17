package com.yapp.fmz.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ZoneServiceTest {

    @Test
    public void test(){
        System.out.println(System.getenv("KAKAO_API_KEY"));
    }

}