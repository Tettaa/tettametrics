package org.example;

import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.proj4j.*;

import java.util.List;

public class ConvexHullCalculator {

    public static Coordinate convert(Coordinate c) {
        // Step 1: Set up the CRS for WGS84 and UTM
        CRSFactory crsFactory = new CRSFactory();

        // Define WGS84
        CoordinateReferenceSystem wgs84 = crsFactory.createFromName("epsg:4326");

        // Define UTM Zone 32N (for example, if in Switzerland)
        // Adjust this zone number based on your location
        CoordinateReferenceSystem utm32n = crsFactory.createFromName("epsg:32632");

        // Step 2: Create the transform
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(wgs84, utm32n);

        // Step 3: Define a point in WGS84
        ProjCoordinate sourceCoord = new ProjCoordinate();
        sourceCoord.x = c.y; // Replace with your longitude
        sourceCoord.y = c.x;  // Replace with your latitude

        // Step 4: Transform to UTM
        ProjCoordinate targetCoord = new ProjCoordinate();
        transform.transform(sourceCoord, targetCoord);

        return new Coordinate(targetCoord.x,targetCoord.y);

    }


    public static Double distance(Coordinate[] coordinates) {

        double d = 0;

        for (int i = 0; i < coordinates.length-1 ;i++) {
            d += coordinates[i].distance(coordinates[i+1]);


        }
        return d;
    }


    public static void main(String[] args) {
        GeometryFactory geometryFactory = new GeometryFactory();

        // Replace this with your array of coordinates (in meters)
        Coordinate[] coordinates = new Coordinate[] {
                new Coordinate(46.14329790211723,8.953769890434986),
                new Coordinate(45.90569616796948,9.024522115626668),
                new Coordinate(45.92148870527029,8.98888329451769),
                new Coordinate(45.95558483310496,8.92922214110469),
                new Coordinate(45.963988858883795,8.973315918093089),
                new Coordinate(45.98296349388758,8.96739551439888),
                new Coordinate(45.981426018811966,8.947994226214853),
                new Coordinate(46.13970034247126,8.953669923268768)
            // ... add all your points here
        };

        //470m^2
        Coordinate[] ponte = new Coordinate[] {
                new Coordinate(46.06282484786887,8.944324571588826),
                new Coordinate(46.06293257314863,8.944611887063532),
                new Coordinate(46.06276525557623,8.944744274338108),
                new Coordinate(46.06268447920675,8.944527496095674),
                new Coordinate(46.06282484786887,8.944324571588826),
                // ... add all your points here
        };

        //164.01 km^2
        Coordinate[] gmap = new Coordinate[] {
                new Coordinate(46.16040354153185, 8.917510392695718),
                new Coordinate(46.02774196420396, 8.981600569364451),
                new Coordinate(45.9400240018598, 8.812635558146878),
                new Coordinate(46.09040876930343, 8.836912140218368),
                // ... add all your points here
        };

        //867 km^2
        Coordinate[] mapgeoTI = new Coordinate[]{

                new Coordinate(46.38653618476709, 8.683390320681367),
                new Coordinate(46.14966785513856, 9.08899178435622),
                new Coordinate(46.03927920912098, 8.94031330450883),
                new Coordinate(46.260886230757436, 8.424344366603103),
                new Coordinate(46.38653618476709, 8.683390320681367)

        };

        Coordinate[] coordinates2 = List.of(mapgeoTI).stream().map(ConvexHullCalculator::convert)
                .toArray(Coordinate[]::new);


        // Step 1: Calculate the convex hull
        ConvexHull convexHull = new ConvexHull(coordinates2, geometryFactory);
        Geometry hullGeometry = convexHull.getConvexHull();

        if (hullGeometry instanceof Polygon) {
            Polygon hullPolygon = (Polygon) hullGeometry;

            // Step 2: Calculate area and perimeter
            double area = hullPolygon.getArea();       // Area in square meters
            double perimeter = hullPolygon.getLength(); // Perimeter in meters

            System.out.println("Area of Convex Hull: " + area + " square meters");
            System.out.println("Perimeter of Convex Hull: " + perimeter + " meters");
        } else {
            System.out.println("Convex hull is not a polygon.");
        }


        //Distance 346
        Coordinate[] distancePonte = new Coordinate[]{
                 new Coordinate(46.06272254901243, 8.944517278543898),
                 new Coordinate(46.06228211248508, 8.944849007520025),
                 new Coordinate(46.06204086686767, 8.945136080672441),
                 new Coordinate(46.0626849239187, 8.946714983010734),
                 new Coordinate(46.062459172817896, 8.948166297281285)
        };

        Coordinate[] distancePonteNew = List.of(distancePonte).stream().map(ConvexHullCalculator::convert)
                .toArray(Coordinate[]::new);


        Double distancePonteVero = distance(distancePonteNew);
        System.out.println("Total distance: " + distancePonteVero + " meters");
    }
}
