package com.yapp.fmz.controller;

import com.yapp.fmz.domain.Room;
import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.dto.RequestFindZoneDto;
import com.yapp.fmz.domain.dto.RoomDto;
import com.yapp.fmz.domain.dto.ZoneDto;
import com.yapp.fmz.domain.vo.CategoryVo;
import com.yapp.fmz.domain.vo.PlaceVo;
import com.yapp.fmz.service.ZoneService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
public class ZoneController {
    @Autowired
    ZoneService zoneService;

    @GetMapping("/init")
    public String init() {
        zoneService.initialZoneToRoomData();
        zoneService.initialAddressData();
        return "DB INITIALIZED SUCCESS";
    }

    @GetMapping("/init/polygon")
    public String initPolygon() {
        zoneService.initialPolygonJsonData();
        return "Polygon INITIALIZED SUCCESS";
    }

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
        List<Zone> zones = zoneService.findZones(address, addressTag, transitMode, transferLimit, minTime - 2, maxTime + 2);
        List<ZoneDto> data = zones.stream().map(ZoneDto::new).sorted().collect(toList());
        if (data.size() > 0) {
            response.put("code", 200);
            response.put("message", "정상");
            response.put("data", data);

        } else {
            response.put("code", 404);
            response.put("message", "해당 조건의 기초구역이 존재하지 않습니다.");
        }
        System.out.println("전체 api 수행시간: " + (System.currentTimeMillis() - time) / 1000 + "초");
        return response;
    }

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

        System.out.println("address = " + address);
        System.out.println("tag = " + addressTag);
        System.out.println("transitMode = " + transitMode.toString());
        System.out.println("transferLimit = " + transferLimit);
        System.out.println("minTime = " + minTime);
        System.out.println("maxTime = " + maxTime);

        HashMap<String, Object> response = new HashMap<>();
        List<Zone> zones = zoneService.findTestZones();
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
        List<Zone> zones = zoneService.findOnlyRecommendZones(address, addressTag, transitMode, transferLimit, minTime - 2, maxTime + 2);
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
