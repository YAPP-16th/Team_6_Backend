package com.yapp.fmz.utils;

import org.osgeo.proj4j.*;
import org.springframework.stereotype.Component;

@Component
public class ProjectionUtils {

    public ProjCoordinate transformLocationToUtm(Double x, Double y) {

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

    public ProjCoordinate transformLocationToUtm2(Double x, Double y) {

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

}
