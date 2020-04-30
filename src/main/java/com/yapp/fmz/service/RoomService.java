package com.yapp.fmz.service;

import com.yapp.fmz.domain.Room;
import com.yapp.fmz.domain.Zone;
import com.yapp.fmz.repository.RoomRepository;
import com.yapp.fmz.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;


    public List<Room> findRoomsByMonthlyPayment(Long zone_id){
        return roomRepository.findRoomsByZoneOrderByMonthlyPayment(zone_id);
    }
    public List<Room> findRoomsByRegisterdId(Long zone_id){
        return roomRepository.findRoomsByZoneOrderByRegisterId(zone_id);
    }

}
