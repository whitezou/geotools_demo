package org.geotools.self.quickstart;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.Scanner;

public class Quickstart {
    public static void main(String[] args) throws Exception {
// display a data store file chooser dialog for shapefiles
//        File file = JFileDataStoreChooser.showOpenFile("shp", null);
//        if (file == null) {
//            return;
//        }
//
//        FileDataStore store = FileDataStoreFinder.getDataStore(file);
//        SimpleFeatureSource featureSource = store.getFeatureSource();

        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

        // ask for current and destination positions
        double latitude, longitude, latitudeDest, longitudeDest;
        Scanner reader = new Scanner(System.in);
        reader.useLocale(Locale.US);
        System.out.println("Enter reference longitude and latitude:\n");
        longitude = reader.nextDouble();
        latitude = reader.nextDouble();
        System.out.println("Enter destination longitude and latitude:\n");
        longitudeDest = reader.nextDouble();
        latitudeDest = reader.nextDouble();
        reader.close();

        Point start = gf.createPoint(new Coordinate(longitude, latitude));
        Point end = gf.createPoint(new Coordinate(longitudeDest, latitudeDest));

//        final String EPSG4326 = "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\"," +
//                "\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\", " +
//                "0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]";
//
//        CoordinateReferenceSystem crs = CRS.parseWKT(EPSG4326);
//
//        GeodeticCalculator gc = new GeodeticCalculator(crs);
//        gc.setStartingPosition(JTS.toDirectPosition(start.getCoordinate(), crs));
//        gc.setDestinationPosition(JTS.toDirectPosition(end.getCoordinate(), crs));
//
//        double distance = gc.getOrthodromicDistance();
//
//        int totalmeters = (int) distance;
//        int km = totalmeters / 1000;
//        int meters = totalmeters - (km * 1000);
//        float remaining_cm = (float) (distance - totalmeters) * 10000;
//        remaining_cm = Math.round(remaining_cm);
//        float cm = remaining_cm / 100;
//
//        System.out.println("Distance = " + km + "km " + meters + "m " + cm + "cm");

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("TwoDistancesType");
        builder.setCRS(DefaultGeographicCRS.WGS84);
//        builder.add("location", Point.class);
        builder.add("line", LineString.class); //added a linestring class
        // build the type
        final SimpleFeatureType TYPE = builder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal", TYPE);
        Coordinate[] coordinates = {start.getCoordinate(), end.getCoordinate()};
        LineString line = gf.createLineString(coordinates);
        featureBuilder.add(line);
        SimpleFeature feature = featureBuilder.buildFeature(null);
        featureCollection.add(feature);
        Style style = SLD.createSimpleStyle(TYPE, Color.red);
        Layer layer = new FeatureLayer(featureCollection, style);

        // Create a map content and add our shapefile to it
        MapContent map = new MapContent();
        map.setTitle("TEST");

        map.addLayer(layer);
        Style shpStyle = SLD.createSimpleStyle(TYPE, Color.blue);


//        Layer shpLayer = new FeatureLayer(featureSource, shpStyle);
//        map.addLayer(shpLayer);

        // Now display the map
        JMapFrame.showMap(map);


    }
}
