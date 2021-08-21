package org.geotools.self.quickstart;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.awt.*;
public class polygonCreator {


    public void addFeaturePolygon(String type) throws Exception {
        Polygon polygon = null;
        //创建GeometryFactory工厂
        GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureCollection collection = null;
        //    获取类型
        final SimpleFeatureType TYPE =
                DataUtilities.createType(
                        "polygonFeature",
                        "the_geom:Polygon:srid=4326,"
                                + // <- the geometry attribute: LineString type
                                "name:String,"
                                + // <- a String attribute
                                "number:Integer" // a number attribute
                );
        System.out.println("TYPE:" + TYPE);
        //    创建要素集合
        //    创建要素模板
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        //    创建要素并添加到集合
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(103, 30), new Coordinate(103, 40), new Coordinate(105, 40), new Coordinate(103, 30)};
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        LinearRing holes[] = null;

        if (type.equals("feature")) {
            polygon = geometryFactory.createPolygon(linearRing, holes);
        } else if (type.equals("wkt")) {
            WKTReader reader = new WKTReader(geometryFactory);
            polygon = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        }
        String name = "添加区";
        int num = Integer.parseInt("16");
        //    添加的数据一定安装SimpleFeatureType给的字段顺序进行赋值
        featureBuilder.add(polygon);
        featureBuilder.add(name);
        featureBuilder.add(num);
        //    构建要素
        SimpleFeature feature = featureBuilder.buildFeature(null);
        System.out.println("feature;" + feature);
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal", TYPE);
        featureCollection.add(feature);
        showFeature(featureCollection, "polygon");
    }
    public void showFeature(DefaultFeatureCollection features, String type) {
        MapContent map = new MapContent();
        Style style = null;
        map.setTitle("map");
        Color color1 = Color.BLUE;
        Color color2 = Color.RED;
        if (type.equals("polygon")) {
            style = SLD.createPolygonStyle(color1, color2.brighter(), 1);
        } else if (type.equals("lineString")) {
            style = SLD.createLineStyle(color1, 2);
        } else if (type.equals("point")) {
            style = SLD.createPointStyle("Circle", color1, color2, 2, 5);
        }
        Layer layer = new FeatureLayer(features, style);
        map.addLayer(layer);
        // Now display the map
        JMapFrame.showMap(map);
    }
    public static void main(String[] args) throws Exception {
        System.out.println("start");
        polygonCreator polygoncreator = new polygonCreator();
        //featureLab.addFeaturePoint("feature");
        //featureLab.addFeaturePoint("wkt");
        //featureLab.addFeatureLine("feature");
        //featureLab.addFeatureLine("wkt");
        //featureLab.addFeaturePolygon("feature");
        polygoncreator.addFeaturePolygon("wkt");
        System.out.println("end");
    }

}
