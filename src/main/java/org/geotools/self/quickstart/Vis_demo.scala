package org.geotools.self.quickstart

import java.awt.Color

import org.geotools.data.DataUtilities
import org.geotools.feature.DefaultFeatureCollection
import org.geotools.feature.simple.SimpleFeatureBuilder
import org.geotools.map.{FeatureLayer, MapContent, MapViewport}
import org.geotools.styling.SLD
import org.geotools.swing.JMapFrame
import org.locationtech.jts.geom.{GeometryFactory, Point}
import org.locationtech.jts.io.WKTReader

object Vis_demo {
  def main(args: Array[String]): Unit = {
    val geometryFactory = new GeometryFactory
    val reader = new WKTReader(geometryFactory)
    var point:Point = null
    point = reader.read("POINT (3 30)").asInstanceOf[Point]
    val TYPE = DataUtilities.createType("Location", "the_geom:Point:srid=4326") // a number attribute
    val featureCollection = new DefaultFeatureCollection("internal", TYPE)
    val featureBuilder = new SimpleFeatureBuilder(TYPE)
    featureBuilder.add(point)
    val feature = featureBuilder.buildFeature(null)
    featureCollection.add(feature)
    val featuresource = DataUtilities.source(featureCollection)

    val map = new MapContent
    map.setTitle("Quickstart")
    val style = SLD.createSimpleStyle(featuresource.getSchema, Color.blue)
    val layer = new FeatureLayer(featuresource, style)
    map.addLayer(layer)
    map.setViewport(new MapViewport())
    JMapFrame.showMap(map)

  }
}
