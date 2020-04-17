package com.yapp.fmz.utils;

import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.vo.LocationVo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class CalculateGoogleTransportTimeList {
    private String auth;
    private final String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final Logger logger = LoggerFactory.getLogger(CalculateGoogleTransportTimeList.class);

    @Async
    public CompletableFuture<ArrayList<Zone>> find(LocationVo inputLocation, List<Zone> recommendList, Long startTime, Long endTime, int num) {

        auth = System.getenv("GOOGLE_API_KEY");;
        ArrayList<Zone> locations = new ArrayList<>();
        ArrayList<String> originList = new ArrayList<>();
        String destination = "";

        String recommendListString = "";
        recommendListString += recommendList.get(0).getRooms().get(0).getLocation().getLat();
        recommendListString += ',';
        recommendListString += recommendList.get(0).getRooms().get(0).getLocation().getLng();
        for (int i=1; i<100; i++){
            recommendListString += '|';
            recommendListString += recommendList.get(i).getRooms().get(0).getLocation().getLat();
            recommendListString += ',';
            recommendListString += recommendList.get(i).getRooms().get(0).getLocation().getLng();
        }
        System.out.println("recommendListString = " + recommendListString);

        try {
            logger.info("async id = " + num);

            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            restTemplateBuilder.setBufferRequestBody(false);
            RestTemplate restTemplate = restTemplateBuilder.build();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("origins", URLEncoder.encode(recommendListString))
                    .queryParam("destinations", inputLocation.getY().toString() + ',' + inputLocation.getX().toString())
                    .queryParam("mode", "transit")
                    .queryParam("language", "ko")
                    .queryParam("key", auth);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> resultMap = restTemplate.exchange(builder.build(true).toUri(), HttpMethod.GET, httpEntity, String.class);

            System.out.println(resultMap.getBody().toString());

            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject)jsonParser.parse(resultMap.getBody().toString());

            JSONArray destination_addresses = (JSONArray)result.get("destination_addresses");
            for(Object des : destination_addresses) {
                destination = (String)des;
            }
            JSONArray origin_addresses = (JSONArray)result.get("origin_addresses");
            for(Object ori : origin_addresses) {
                originList.add((String)ori);
            }

            JSONArray rows = (JSONArray)result.get("rows");

            for (int i=0; i<rows.size(); i++) {
                JSONObject elementListObject = (JSONObject) rows.get(i);

                JSONArray elements = (JSONArray) elementListObject.get("elements");

                JSONObject element = (JSONObject) elements.get(0);
                JSONObject distance = (JSONObject) element.get("distance");
                JSONObject duration = (JSONObject) element.get("duration");

                Long distance_value = (Long) distance.get("value") / 1000;
                Long duration_value = (Long) duration.get("value") / 60;

                if (duration_value >= startTime && duration_value <= endTime) {
                    locations.add(recommendList.get(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(new ArrayList<Zone>(locations));
    }

}
