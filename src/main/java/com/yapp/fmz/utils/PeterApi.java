package com.yapp.fmz.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yapp.fmz.domain.Room;
import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.vo.LocationVo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class PeterApi {
    private static final Logger logger = LoggerFactory.getLogger(PeterApi.class);

    @Async
    public CompletableFuture<Room> removeTrashRooms(Room room, int num) {

        String apiUrl = "https://www.peterpanz.com/house/";

        try {
            logger.info("async id = " + num);
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            restTemplateBuilder.setBufferRequestBody(false);
            RestTemplate restTemplate = restTemplateBuilder.build();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + room.getRegisterId());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> resultMap = restTemplate.exchange(builder.build(true).toUri(), HttpMethod.GET, httpEntity, String.class);

//            Document doc = Jsoup.connect(apiUrl + roomId).get();
            Document doc = Jsoup.parse(resultMap.getBody().toString());
            String lang = doc.getAllElements().get(1).attr("lang");
            if(lang.equals("ko")){
                return CompletableFuture.completedFuture(room);
            }else if(lang == null){
                return CompletableFuture.completedFuture(room);
            }else{
                System.out.println("정상 방 번호: " + room.getRegisterId());
                return null;
            }

        } catch (Exception e) {
            System.out.println("오류 방 번호 : " + room.getRegisterId());
            e.printStackTrace();
            return null;
        }
    }
}
