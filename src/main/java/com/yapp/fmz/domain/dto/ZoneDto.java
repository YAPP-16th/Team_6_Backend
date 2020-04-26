package com.yapp.fmz.domain.dto;

import com.yapp.fmz.domain.Address;
import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.Zone;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class ZoneDto implements Comparable<ZoneDto>{

    private Long id;
    private Long zipcode;
    private Address address;
    private Location location;
    private String type;
    private String polygon;
    private Long time;
    private List<RoomDto> rooms = new ArrayList<>();

    public ZoneDto(Zone zone) {
        this.id = zone.getId();
        this.zipcode = zone.getZipcode();
        this.address = zone.getAddress();
        this.location = zone.getLocation();
        this.type = zone.getType();
        this.polygon = zone.getPolygon();
        this.time = zone.getTime();
        this.rooms = zone.getRooms().stream()
                .map(RoomDto::new)
                .collect(toList());
    }

    @Override
    public int compareTo(ZoneDto z) {
        if(this.time > z.time){
            return 1;
        }
        return -1;
    }
}
