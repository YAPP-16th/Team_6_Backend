package com.yapp.fmz.utils;

import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.vo.LocationVo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class GoogleApi {
    private String auth = System.getenv("GOOGLE_API_KEY");;
    private static final Logger logger = LoggerFactory.getLogger(GoogleApi.class);

    @Async
    public CompletableFuture<ArrayList<Zone>> findSimpleZones(LocationVo inputLocation, List<Zone> recommendList, List<String> transitMode, Long startTime, Long endTime, int num) {

        String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json";
        ArrayList<Zone> locations = new ArrayList<>();
        ArrayList<String> originList = new ArrayList<>();
        String destination = "";

        String recommendListString = "";
        recommendListString += recommendList.get(0).getRooms().get(0).getLocation().getLat();
        recommendListString += ',';
        recommendListString += recommendList.get(0).getRooms().get(0).getLocation().getLng();
        for (int i=1; i<recommendList.size(); i++){
            recommendListString += '|';
            recommendListString += recommendList.get(i).getRooms().get(0).getLocation().getLat();
            recommendListString += ',';
            recommendListString += recommendList.get(i).getRooms().get(0).getLocation().getLng();
        }

        try {
            logger.info("async id = " + num);

            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            restTemplateBuilder.setBufferRequestBody(false);
            RestTemplate restTemplate = restTemplateBuilder.build();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("origins", URLEncoder.encode(recommendListString))
                    .queryParam("destinations", inputLocation.getY().toString() + ',' + inputLocation.getX().toString())
                    .queryParam("mode", "transit")
                    .queryParam("transit_mode", URLEncoder.encode(makeModeParameter(transitMode)))
                    .queryParam("language", "ko")
                    .queryParam("key", auth);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> resultMap = restTemplate.exchange(builder.build(true).toUri(), HttpMethod.GET, httpEntity, String.class);

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

                Long distance_value;
                Long duration_value;

                if(duration == null){
//                    System.out.println(resultMap.getBody().toString());
                    continue;
                }else{
                    duration_value = (Long) duration.get("value") / 60;
                    distance_value = (Long) distance.get("value") / 1000;
                }


                if (duration_value >= startTime && duration_value <= endTime) {
                    Zone tempZone = recommendList.get(i);
                    tempZone.setTime(duration_value);
                    locations.add(tempZone);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(new ArrayList<Zone>(locations));
    }

    @Async
    public CompletableFuture<Zone> findZones(LocationVo inputLocation, Zone recommendZone, List<String> transitMode, Long transterLimit, Long startTime, Long endTime, int num) {

        String apiUrl = "https://maps.googleapis.com/maps/api/directions/json";
        ArrayList<Zone> locations = new ArrayList<>();
        ArrayList<String> originList = new ArrayList<>();
        String destination = "";

        String recommendString = "";
        recommendString += recommendZone.getRooms().get(0).getLocation().getLat();
        recommendString += ',';
        recommendString += recommendZone.getRooms().get(0).getLocation().getLng();

        try {
            logger.info("async id = " + num);

            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            restTemplateBuilder.setBufferRequestBody(false);
            RestTemplate restTemplate = restTemplateBuilder.build();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("origin", URLEncoder.encode(recommendString))
                    .queryParam("destination", inputLocation.getY().toString() + ',' + inputLocation.getX().toString())
                    .queryParam("mode", "transit")
                    .queryParam("transit_mode", makeModeParameter(transitMode))
                    .queryParam("alternatives", "true")
                    .queryParam("language", "ko")
                    .queryParam("key", auth);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> resultMap = restTemplate.exchange(builder.build(true).toUri(), HttpMethod.GET, httpEntity, String.class);

//            System.out.println(resultMap.getBody().toString());

            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject)jsonParser.parse(resultMap.getBody().toString());

            JSONArray routes = (JSONArray)result.get("routes");
            for (int i=0; i<routes.size(); i++) {
                int transitCount = 0;
                JSONObject routeObject = (JSONObject) routes.get(i);

                JSONArray legs = (JSONArray) routeObject.get("legs");

                JSONObject legObject = (JSONObject) legs.get(0);
                JSONObject duration = (JSONObject) legObject.get("duration");
                Long duration_value = (Long) duration.get("value") / 60;
                JSONArray steps = (JSONArray) legObject.get("steps");

                JSONObject stepFirstObject = (JSONObject)steps.get(0);
                String modeValue = (String)stepFirstObject.get("travel_mode");
                for(int j=1; j<steps.size(); j++){
                    JSONObject stepObject = (JSONObject)steps.get(j);
                    String modeObject = (String)stepObject.get("travel_mode");

                    if(modeValue.equals("TRANSIT") && modeObject.equals("TRANSIT")){
                        transitCount++;
                    }
                    modeValue = modeObject;
                }

                if (duration_value >= startTime && duration_value <= endTime && transitCount <= transterLimit) {
                    recommendZone.setTime(duration_value);
                    return CompletableFuture.completedFuture(recommendZone);
                }
                else{
                    continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }

    public String makeModeParameter(List<String> input){
        if(input.isEmpty()){
            return "";
        }
        String output = "";
        output += input.get(0);
        for (int i=1; i<input.size(); i++){
            output += '|';
            output += input.get(i);
        }
        return output;
    }

}
