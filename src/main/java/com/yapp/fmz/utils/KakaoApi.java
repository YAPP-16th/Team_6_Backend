package com.yapp.fmz.utils;

import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.enu.Category;
import com.yapp.fmz.domain.vo.CategoryVo;
import com.yapp.fmz.domain.vo.LocationVo;
import com.yapp.fmz.domain.vo.PlaceVo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                    .queryParam("x", location.getLng())
                    .queryParam("y", location.getLat());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            httpHeaders.set(HttpHeaders.AUTHORIZATION, auth);

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> resultMap =restTemplate.exchange(builder.build(true).toUri(), HttpMethod.GET, httpEntity, String.class);

            System.out.println(resultMap.getBody().toString());
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject)jsonParser.parse(resultMap.getBody().toString());
            JSONArray documents = (JSONArray)result.get("documents");
            JSONObject documentObject = (JSONObject) documents.get(0);
            JSONObject roadAddress = (JSONObject) documentObject.get("address");
            String address = (String)roadAddress.get("address_name");
            System.out.println(address);
            return address;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public CategoryVo findPlaceNearZone(LocationVo location, Category category){
        try{
            CategoryVo categoryVo;
            List<PlaceVo> placeList = new ArrayList<>();
            String apiUrl = "https://dapi.kakao.com/v2/local/search/category.json";

            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            RestTemplate restTemplate = restTemplateBuilder.build();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("x", location.getX().toString())
                    .queryParam("y", location.getY().toString())
                    .queryParam("radius", 1000)
                    .queryParam("category_group_code", category.toString());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            httpHeaders.set(HttpHeaders.AUTHORIZATION, auth);

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> resultMap =restTemplate.exchange(builder.build(true).toUri(), HttpMethod.GET, httpEntity, String.class);

            System.out.println(resultMap.getBody().toString());
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject)jsonParser.parse(resultMap.getBody().toString());
            JSONObject meta = (JSONObject)result.get("meta");
            Long totalCount = (Long) meta.get("total_count");

            JSONArray documents = (JSONArray)result.get("documents");
            String objectCategorName = "";
            for(int i=0; i<documents.size(); i++){
                JSONObject documentObject = (JSONObject) documents.get(i);
                objectCategorName = (String) documentObject.get("category_group_name");
                String objectPlaceName = (String) documentObject.get("place_name");
                String objectAddress = (String) documentObject.get("address_name");
                String objectDistance = (String) documentObject.get("distance");

                placeList.add(new PlaceVo(objectCategorName, objectPlaceName, objectAddress, objectDistance));
            }
            categoryVo = new CategoryVo(objectCategorName, totalCount, placeList);

            return categoryVo;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void sendKakaoMessage(String message){

        String apiUrl = "https://hooks.slack.com/services/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";

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        JSONObject requestObject = new JSONObject();
        requestObject.put("text", message);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, "application/json");
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<?> httpEntity = new HttpEntity<>(requestObject.toString(), httpHeaders);
        ResponseEntity<String> resultMap =restTemplate.exchange(builder.build(true).toUri(), HttpMethod.POST, httpEntity, String.class);

        System.out.println(resultMap.getBody().toString());
    }
}

