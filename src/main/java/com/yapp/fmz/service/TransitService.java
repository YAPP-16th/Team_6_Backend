package com.yapp.fmz.service;
import com.yapp.fmz.domain.Location;
import com.yapp.fmz.domain.vo.LocationVo;
import com.yapp.fmz.repository.TransitRepository;
import com.yapp.fmz.utils.GoogleApi;
import org.osgeo.proj4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;


@Service
public class TransitService {

    @Autowired
    GoogleApi googleAPI;

    @Autowired
    TransitRepository transitRepository;


    public ArrayList findTransit(Double x, Double y,Long zoneId) throws IOException {

        LocationVo orgin = new LocationVo(y, x);
        Location location  = transitRepository.findLocationByZoneId(zoneId);
        ProjCoordinate transformLocation = transformUtmToLocation( location.getLat(),location.getLng());
        LocationVo destination = new LocationVo(transformLocation.x, transformLocation.y);

        return googleAPI.findTransport(orgin,destination);

    }


    public static ProjCoordinate transformUtmToLocation(Double x, Double y) {

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CRSFactory csFactory = new CRSFactory();
        CoordinateReferenceSystem GOOGLE = csFactory.createFromParameters("WGS84", "+proj=longlat +datum=WGS84 +no_defs");
        CoordinateReferenceSystem WGS84 = csFactory.createFromParameters("EPSG:3857", "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs ");
        CoordinateTransform trans = ctFactory.createTransform( WGS84,GOOGLE);
        ProjCoordinate p = new ProjCoordinate();
        ProjCoordinate p2 = new ProjCoordinate();
        p.x = x;
        p.y = y;

        return trans.transform(p, p2);

    }


}
