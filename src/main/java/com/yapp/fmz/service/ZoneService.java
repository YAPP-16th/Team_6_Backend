package com.yapp.fmz.service;

import com.yapp.fmz.domain.Room;
import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.domain.vo.LocationVo;
import com.yapp.fmz.repository.RoomRepository;
import com.yapp.fmz.repository.ZoneRepository;
import com.yapp.fmz.utils.GoogleApi;
import com.yapp.fmz.utils.KakaoApi;
import org.osgeo.proj4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ZoneService {
    @Autowired
    ZoneRepository zoneRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    KakaoApi kakaoAPI;
    @Autowired
    GoogleApi googleAPI;
//    @Autowired
//    SearchRepositoy searchRepositoy;

    @Transactional
    public void initialData(){
        // 각 zone room 세팅해주기
        List<Zone> zoneList = zoneRepository.findAll();
        List<Room> roomList = roomRepository.findAll();

        for (Zone zone: zoneList) {
            List<Room> temp = new ArrayList<>();
            for(Room room: roomList){
                if(room.getZipcode().equals(zone.getZipcode())){
                    temp.add(room);
                    room.setZone(zone);
                    //매물 중 하나의 좌표를 주소로 변환하여 기초 구역 전체 주소로 사용
                    String address = kakaoAPI.convertLocationToAddress(room.getLocation());
                    room.setAddress(address);
                    // 매물 주소가 부정확한경우 매물 주소도 좌표 주소로 변환
                    if(room.getAddress().length()<5){
                        room.setAddress(address);
                    }
                }
            }
            zone.setRooms(temp);
        }
    }

    public ArrayList<Zone> findZones(String address, String tag, List<String> travelMode, Long transferLimit, Long minTime, Long maxTime){
//         주소->좌표 변환
        HashMap<String, String> location = kakaoAPI.convertAddressToLocation(address);
        Double x = Double.parseDouble(location.get("x"));
        Double y = Double.parseDouble(location.get("y"));
        LocationVo parsedLocation = new LocationVo(x,y);

//         좌표->좌표 변환
        long transStart = System.currentTimeMillis();
        ProjCoordinate trans = transformLocationToUtm(x, y);
        System.out.println("좌표 변환 수행시간: " + (System.currentTimeMillis() - transStart)/1000 + "초");

//         매물이 있는 기초구역만 필터링
        long dbStart = System.currentTimeMillis();
        List<Zone> all = zoneRepository.findZonesHasRoomV2();
        System.out.println("전체 리스트: " + all.size());
        System.out.println("매물 있는 기초구역 DB 수행시간: " + (System.currentTimeMillis() - dbStart)/1000 + "초");

//         거리 필터링
        long lengthStart = System.currentTimeMillis();
        List<Zone> recommend = new ArrayList<>();
        for(int i=0; i<all.size(); i++){
            Double tx = all.get(i).getLocation().getLat();
            Double ty = all.get(i).getLocation().getLng();

//             거리 계산
            Double length = Math.sqrt(Math.pow(Math.abs(tx-trans.x), 2) + Math.pow(Math.abs(ty-trans.y), 2))/1000;

//             30분 거리 필터링
            if(length >=0 && length <= 30){
                recommend.add(all.get(i));
            }
        }
        System.out.println("매물 있고 거리가 30KM 이하인 기초구역 리스트: " + recommend.size());
        System.out.println("거리 필터링 수행시간: " + (System.currentTimeMillis() - lengthStart)/1000 + "초");

        long apiStart = System.currentTimeMillis();
        ArrayList<Zone> recommendLocationList = new ArrayList<>();

        if(transferLimit == 0 || travelMode.equals("transit") ){
//             api 제한으로 인해 리스트 100개씩 나누기
            List<List<Zone>> partitionList = partition( recommend, 2);

            List<CompletableFuture<ArrayList<Zone>>> futureList = new ArrayList<>();
//            for(int i=0; i<partitionList.size(); i++) {
            for(int i=0; i<2; i++) {
                futureList.add(googleAPI.findSimpleZones(parsedLocation, partitionList.get(0), travelMode, minTime, maxTime, i));
            }
            System.out.println("지금?");

            try{
                for (CompletableFuture<ArrayList<Zone>> locationCompletableFuture : futureList) {
                    recommendLocationList.addAll(locationCompletableFuture.join());
                }
                System.out.println("지금?");

            } catch(Exception e){
                e.printStackTrace();
            }
        }else{
            ArrayList<CompletableFuture<Zone>> futureList = new ArrayList<>();
            for(int i=0; i<recommend.size(); i++){
                futureList.add(googleAPI.findZones(parsedLocation, recommend.get(i), travelMode, transferLimit, minTime, maxTime, i));
            }

            try{
                for (CompletableFuture<Zone> locationCompletableFuture : futureList) {
                    recommendLocationList.add(locationCompletableFuture.join());
                }

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("간이 길찾기 api 수행시간: " + (System.currentTimeMillis() - apiStart)/1000 + "초");

        // 서치 객체 저장
        // Search search = new Search(parsedAddress, location, tag, transportation, transferLimit, minTime, maxTime, data);
        // searchRepository.save(search)

        return recommendLocationList;
    }

    public static ProjCoordinate transformUtmToLocation(Double x, Double y) {

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();

        CRSFactory csFactory = new CRSFactory();

        CoordinateReferenceSystem GOOGLE = csFactory.createFromParameters("EPSG:3857", "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs ");

        CoordinateReferenceSystem WGS84 = csFactory.createFromParameters("WGS84", "+proj=longlat +datum=WGS84 +no_defs");

        CoordinateTransform trans = ctFactory.createTransform(GOOGLE, WGS84);

        ProjCoordinate p = new ProjCoordinate();

        ProjCoordinate p2 = new ProjCoordinate();

        p.x = x;
        p.y = y;

        return trans.transform(p, p2);

    }

    public static ProjCoordinate transformLocationToUtm(Double x, Double y) {

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();

        CRSFactory csFactory = new CRSFactory();

        CoordinateReferenceSystem GOOGLE = csFactory.createFromParameters("WGS84", "+proj=longlat +datum=WGS84 +no_defs");

        CoordinateReferenceSystem WGS84 = csFactory.createFromParameters("EPSG:3857", "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs ");

        CoordinateTransform trans = ctFactory.createTransform(GOOGLE, WGS84);

        ProjCoordinate p = new ProjCoordinate();

        ProjCoordinate p2 = new ProjCoordinate();

        p.x = x;
        p.y = y;

        return trans.transform(p, p2);

    }

    public static ProjCoordinate transformLocationToUtm2(Double x, Double y) {

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();

        CRSFactory csFactory = new CRSFactory();

        CoordinateReferenceSystem GOOGLE = csFactory.createFromParameters("WGS84", "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs\n");

        CoordinateReferenceSystem WGS84 = csFactory.createFromParameters("EPSG:3857", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");

        CoordinateTransform trans = ctFactory.createTransform(GOOGLE, WGS84);

        ProjCoordinate p = new ProjCoordinate();

        ProjCoordinate p2 = new ProjCoordinate();

        p.x = x;
        p.y = y;

        return trans.transform(p, p2);

    }

    private static  <T> List<List<T>> partition(List<T> resList, int count) {
        if (resList == null || count <1)
            return null;
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        if (size <= count) {
            // 데이터 부족 count 지정 크기
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            // 앞 pre 개 집합, 모든 크기 다 count 가지 요소
            for (int i = 0; i <pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j <count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            // last 진행이 처리
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i <last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }
}
