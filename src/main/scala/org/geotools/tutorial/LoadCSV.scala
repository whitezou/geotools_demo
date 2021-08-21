package org.geotools.tutorial

import java.awt.Color

import org.geotools.data.DataUtilities
import org.geotools.feature.DefaultFeatureCollection
import org.geotools.feature.simple.{SimpleFeatureBuilder, SimpleFeatureTypeBuilder}
import org.geotools.map.{FeatureLayer, MapContent}
import org.geotools.referencing.crs.DefaultGeographicCRS
import org.geotools.styling.SLD
import org.geotools.swing.JMapFrame
import org.locationtech.jts.geom.{LineString, MultiPolygon}
import org.locationtech.jts.io.WKTReader
import org.opengis.feature.simple.SimpleFeatureType

import scala.io.Source

object LoadCSV {
  def main(args: Array[String]): Unit = {
    val builder = new SimpleFeatureTypeBuilder
    builder.setName("TwoDistancesType")
    builder.setCRS(DefaultGeographicCRS.WGS84)
    //        builder.add("location", Point.class);
    builder.add("line", classOf[MultiPolygon]) //added a linestring class

    // build the type
    val TYPE = builder.buildFeatureType

    val wktReader = new WKTReader()
    val source = Source.fromFile("F:\\reviseCode_debugqtc\\dataset\\TIGER\\ZCTA5.csv", "UTF-8")
    val lines = source.getLines().toArray
    source.close()

    //    val TYPE = DataUtilities.createType("polygonFeature", "the_geom:MultiPolygon:srid=4326")
    val featureBuilder = new SimpleFeatureBuilder(TYPE)
    val featureCollection = new DefaultFeatureCollection("internal", TYPE)

    val map = new MapContent
    map.setTitle("Quickstart")
    for (line <- lines) {
      var a = line.toString.split("\t")(0).replace("\"", "")
      val polygon = wktReader.read(a)
      println(polygon)
      //    添加的数据一定安装SimpleFeatureType给的字段顺序进行赋值
      featureBuilder.add(polygon)
      val feature = featureBuilder.buildFeature(null)
      featureCollection.add(feature)
      println(featureCollection.size())
//      if(featureCollection.size()%100==0){
//
//        val featuresource = DataUtilities.source(featureCollection)
//        val styleColor = new Color(255, 0, 0)
//        val style = SLD.createSimpleStyle(featuresource.getSchema, styleColor)
//        val layer = new FeatureLayer(featuresource, style)
//        layer.setVisible(true)
//        map.addLayer(layer)
//        featureCollection.clear()
//      }

    }
    if (featureCollection.size()!=0){
      val featuresource = DataUtilities.source(featureCollection)

      val style = SLD.createSimpleStyle(featuresource.getSchema)
      val layer = new FeatureLayer(featuresource, style)
      map.addLayer(layer)
    }

    JMapFrame.showMap(map)
  }
}
