package com.yapp.fmz.controller;

import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.dto.RequestFindZoneDto;
import com.yapp.fmz.domain.dto.ZoneDto;
import com.yapp.fmz.domain.vo.CategoryVo;
import com.yapp.fmz.domain.vo.LocationVo;
import com.yapp.fmz.service.ZoneService;
import com.yapp.fmz.utils.KakaoApi;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
public class ZoneController {
    @Autowired
    ZoneService zoneService;

    @Autowired
    KakaoApi kakaoAPI;

    @GetMapping("/db/init/room/mapping")
    public String initZoneToRoomMaping() {
        zoneService.initialZoneToRoomData();
        return "DB (Zone To Room) Maping INITIALIZED SUCCESS";
    }

    @GetMapping("/db/init/zone/address")
    public String initZone() {
        zoneService.initialAddressData();
        return "DB Zone Address INITIALIZED SUCCESS";
    }

    @GetMapping("/db/init/zone/polygon")
    public String initPolygon() {
        zoneService.initialPolygonJsonData();
        return "DB Zone Polygon INITIALIZED SUCCESS";
    }

    @CrossOrigin("*")
    @ApiOperation(value = "사용자 선택 기반 추천 기초구역 매물 정보", notes = "사용자 선택에 기반한 추천 기초구역과 기초구역 내 매물 정보입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "기초구역 데이터를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "추천하는 기초구역이 존재하지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @ResponseBody
    @GetMapping("/zones")
    public HashMap<String, Object> recommendZone(
            @RequestParam String address,
            @RequestParam String addressTag,
            @RequestParam List<String> transitMode,
            @RequestParam Long transferLimit,
            @RequestParam Long minTime,
            @RequestParam Long maxTime
    ) {
        long time = System.currentTimeMillis();

        HashMap<String, Object> response = new HashMap<>();
        try{
            RequestFindZoneDto requestFindZoneDto = new RequestFindZoneDto(address, addressTag, transitMode, transferLimit, minTime, maxTime);

        }catch(Exception e){
            response.put("code", 404);
            response.put("message", "요청 조건의 형식의 오류가 있습니다.");
            return response;
        }
        //         주소->좌표 변환
        HashMap<String, String> location = kakaoAPI.convertAddressToLocation(address);
        Double x = Double.parseDouble(location.get("x"));
        Double y = Double.parseDouble(location.get("y"));
        LocationVo locationVo = new LocationVo(x, y);

        List<Zone> zones = zoneService.findZones(locationVo, addressTag, transitMode, transferLimit, minTime - 2, maxTime + 2);
        List<ZoneDto> data = zones.stream().map(ZoneDto::new).sorted().collect(toList());
        if(data.size() > 10){
            data = data.subList(0,10);
        }

        if(data.size() > 0) {
            response.put("code", 200);
            response.put("message", "정상");
            response.put("inputLocation", locationVo);
            response.put("data", data);

        } else {
            response.put("code", 404);
            response.put("message", "해당 조건의 기초구역이 존재하지 않습니다.");
        }
        System.out.println("전체 api 수행시간: " + (System.currentTimeMillis() - time) / 1000 + "초");
        return response;
    }

    @CrossOrigin("*")
    @ResponseBody
    @GetMapping("/address/toLocation")
    public HashMap<String, Object> convertAddressToLocation(
            @ApiParam(value = "ADDRESS", required = true, example = "서울특별시 강남구 역삼동 테헤란로48길 10")
            @RequestParam String address
    ) {
        long time = System.currentTimeMillis();

        HashMap<String, Object> response = new HashMap<>();

        //         주소->좌표 변환
        HashMap<String, String> location = kakaoAPI.convertAddressToLocation(address);
        Double x = Double.parseDouble(location.get("x"));
        Double y = Double.parseDouble(location.get("y"));
        LocationVo locationVo = new LocationVo(x, y);

        if (locationVo.getY() > 0 && locationVo.getX() > 0) {
            response.put("code", 200);
            response.put("message", "정상");
            response.put("data", locationVo);

        } else {
            response.put("code", 404);
            response.put("message", "해당 주소의 좌표가 존재하지 않습니다.");
        }
        System.out.println("전체 api 수행시간: " + (System.currentTimeMillis() - time) / 1000 + "초");
        return response;
    }

    @CrossOrigin("*")
    @ApiOperation(value = "기초구역 주변 주거 환경 정보", notes = "기초구역 기준 주거 환경 정보입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "주거 환경 데이터를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "주거 환경 데이터가 존재하지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @GetMapping("/places")
    public HashMap<String, Object> findPlaceInZone(@ApiParam(value = "기초구역 ID", required = true, example = "2") @RequestParam("zoneId") Long zone_id) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        List<CategoryVo> categoryVoList = zoneService.findPlaces(zone_id);
        if (categoryVoList.size() == 0) {
            response.put("code", 300);
            response.put("message", "해당 조건의 주변 환경 정보가 존재하지 않습니다.");
        } else {
            response.put("code", 200);
            response.put("message", "주변 환경 정보를 정상적으로 가져왔습니다.");
            response.put("data", categoryVoList);
        }

        return response;

    }

    @CrossOrigin("*")
    @ApiOperation(value = "String 형태의 Message", notes = "개발 과정에서 생기는 스트레스를 해소하기 위한 익명 메시지 Api, 욕을 보내시면 익명으로 저의 슬랙으로 전송됩니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "메시지를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 400, message = "너무 심한 욕입니다.")
    })
    @GetMapping("/test/message")
    public String sendMessage(@RequestParam String message){
        zoneService.sendMessage(message);
        return "전송 완료";
    }

    @CrossOrigin("*")
    @ApiOperation(value = "개발 편의를 위한 사용자 선택 기반 추천 기초구역 매물 정보", notes = "/zones api 한번 수행이 시간과 비용이 많이 소비되어 테스트용으로 만든 api입니다.(Polygon, MultiPolygon 정보 각각 있음)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "기초구역 데이터를 정상적으로 가져왔습니다."),
            @ApiResponse(code = 300, message = "추천하는 기초구역이 존재하지 않습니다."),
            @ApiResponse(code = 400, message = "통신에 실패했습니다.")
    })
    @ResponseBody
    @GetMapping("/test/zones")
    public HashMap<String, Object> testRecommendZone(
            @RequestParam String address,
            @RequestParam String addressTag,
            @RequestParam List<String> transitMode,
            @RequestParam Long transferLimit,
            @RequestParam Long minTime,
            @RequestParam Long maxTime
    ) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            RequestFindZoneDto requestFindZoneDto = new RequestFindZoneDto(address, addressTag, transitMode, transferLimit, minTime, maxTime);
            Optional.of(requestFindZoneDto.getAddress());
            Optional.of(requestFindZoneDto.getAddressTag());
            Optional.of(requestFindZoneDto.getTransitMode().get(0));
            Optional.of(requestFindZoneDto.getTransferLimit());
            Optional.of(requestFindZoneDto.getMinTime());
            Optional.of(requestFindZoneDto.getMaxTime());
        } catch(Exception e){
            response.put("code", 404);
            response.put("message", "size of transitMode is 0");
            response.put("error_message", e.getMessage());
            return response;
        }
        //         주소->좌표 변환
        HashMap<String, String> location = kakaoAPI.convertAddressToLocation(address);
        Double x = Double.parseDouble(location.get("x"));
        Double y = Double.parseDouble(location.get("y"));
        LocationVo locationVo = new LocationVo(x, y);

        List<Zone> zones = zoneService.findTestZones();
        List<ZoneDto> data = zones.stream().map(ZoneDto::new).sorted().collect(toList());
        if (data.size() > 0) {
            response.put("code", 200);
            response.put("message", "정상");
            response.put("inputLocation", locationVo);
            response.put("data", data);

        } else {
            response.put("code", 404);
            response.put("message", "해당 조건의 기초구역이 존재하지 않습니다.");
        }
        return response;
    }
    
    @ResponseBody
    @GetMapping("/server/test/zones")
    public HashMap<String, Object> testServerRecommendZone(
            @RequestParam String address,
            @RequestParam String addressTag,
            @RequestParam List<String> transitMode,
            @RequestParam Long transferLimit,
            @RequestParam Long minTime,
            @RequestParam Long maxTime
    ) {

        HashMap<String, Object> response = new HashMap<>();
        List<Zone> zones = zoneService.findOnlyRecommendZones(address);
        List<ZoneDto> data = zones.stream().map(ZoneDto::new).sorted().collect(toList());
        if (data.size() > 0) {
            response.put("code", 200);
            response.put("message", "정상");
            response.put("data", data);

        } else {
            response.put("code", 404);
            response.put("message", "해당 조건의 기초구역이 존재하지 않습니다.");
        }
        return response;
    }
}
