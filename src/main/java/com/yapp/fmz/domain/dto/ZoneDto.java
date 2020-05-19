package com.yapp.fmz.domain.dto;

import com.yapp.fmz.domain.Address;
import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.Zone;
import lombok.Data;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class ZoneDto implements Comparable<ZoneDto>{

    private Long id;
    private Long zipcode;
    private Address address;
    private Location location;
    private Double x;
    private Double y;
    private Object polygon;
    private Long time;
    private List<RoomDto> rooms = new ArrayList<>();

    public ZoneDto(Zone zone) {
        try{
            JSONParser jsonParser = new JSONParser();
            this.id = zone.getId();
            this.zipcode = zone.getZipcode();
            this.address = zone.getAddress();
            this.location = zone.getLocation();
            this.x = zone.getX();
            this.y = zone.getY();
            this.polygon = jsonParser.parse(zone.getPolygonJson());
            this.time = zone.getTime();
            this.rooms = zone.getRooms().stream()
                    .map(RoomDto::new)
                    .collect(toList());
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int compareTo(ZoneDto z) {
        return this.time.compareTo(z.time);
    }
}
