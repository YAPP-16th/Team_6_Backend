package com.yapp.fmz.utils;

import com.yapp.fmz.domain.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Component
public class KakaoApi {
    private String auth =  System.getenv("KAKAO_API_KEY");

    public HashMap<String, String> convertAddressToLocation(String address){
        try{
            String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";

            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            RestTemplate restTemplate = restTemplateBuilder.build();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("query", URLEncoder.encode(address,"UTF-8" ));

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            httpHeaders.set(HttpHeaders.AUTHORIZATION, auth);

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> resultMap =restTemplate.exchange(builder.build(true).toUri(), HttpMethod.GET, httpEntity, String.class);

            JSONParser jsonParser = new JSONParser();
            JSONObject temp = (JSONObject)jsonParser.parse(resultMap.getBody().toString());
            JSONArray documents = (JSONArray)temp.get("documents");
            JSONObject first = (JSONObject) documents.get(0);

            String x = (String)first.get("x");
            String y = (String)first.get("y");

            HashMap<String, String> re = new HashMap<String, String>();
            re.put("x", x);
            re.put("y", y);
            return re;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public String convertLocationToAddress(Location location){
        try{
            String apiUrl = "https://dapi.kakao.com/v2/local/geo/coord2address";

            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            RestTemplate restTemplate = restTemplateBuilder.build();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("x", location.getLat())
                    .queryParam("y", location.getLng());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            httpHeaders.set(HttpHeaders.AUTHORIZATION, auth);

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> resultMap =restTemplate.exchange(builder.build(true).toUri(), HttpMethod.GET, httpEntity, String.class);

            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject)jsonParser.parse(resultMap.getBody().toString());
            JSONArray documents = (JSONArray)result.get("documents");
            JSONObject roadAddress = (JSONObject) documents.get(0);

            String address = (String)roadAddress.get("address_name");
            return address;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

