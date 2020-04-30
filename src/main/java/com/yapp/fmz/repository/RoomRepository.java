package com.yapp.fmz.repository;

import com.yapp.fmz.domain.Room;
import com.yapp.fmz.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT  r from Room r  Where r.zone.id = ?1 order by r.monthlyPayment asc")
    public List<Room> findRoomsByZoneOrderByMonthlyPayment(Long zone_id);

    @Query(value = "SELECT  r from Room r  Where r.zone.id = ?1 order by r.registerId asc")
    public List<Room> findRoomsByZoneOrderByRegisterId(Long zone_id);
}
