package com.yapp.fmz.repository;

import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT z.location from Zone z Where z.id=?1")
    public Location findLocationByZoneId(Long zoneId);
}
