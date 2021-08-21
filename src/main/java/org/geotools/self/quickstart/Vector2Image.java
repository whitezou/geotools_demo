package org.geotools.self.quickstart;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Vector2Image {


    public Parameters addShapeLayer(String shpPath,MapContent map ,Color styleColor,String geo_type){
        Parameters parameters = new Parameters();
        try{
            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            builder.setName("TwoDistancesType");
            builder.setCRS(DefaultGeographicCRS.WGS84);
//            builder.add("MultiPolygon", MultiPolygon.class);
            if("multipolygon".equals(geo_type)) {
                builder.add("MultiPolygon", MultiPolygon.class);
            }else {
                builder.add("line", LineString.class);
            }


            final SimpleFeatureType TYPE = builder.buildFeatureType();
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
            String filePath = shpPath;
            String encoding = "UTF-8";
            File file = new File(filePath);
            WKTReader wktReader = new WKTReader();
            DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal", TYPE);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null) {
//                    System.out.println(lineTxt);
                    String wkt = lineTxt.split("\t")[0].replace("\"", "");
                    Geometry geometry = wktReader.read(wkt);
                    System.out.println(geometry);

                    featureBuilder.add(geometry);
                    SimpleFeature feature = featureBuilder.buildFeature(null);
                    featureCollection.add(feature);
                }
                ReferencedEnvelope bounds = featureCollection.getBounds();
                parameters.bbox = new double[4];
                parameters.bbox[0] = bounds.getMinX();
                parameters.bbox[1] = bounds.getMinY();
                parameters.bbox[2] = bounds.getMaxX();
                parameters.bbox[3] = bounds.getMaxY();
                System.out.println("Envelope: ");
                System.out.println(parameters.bbox[0]);
                System.out.println(parameters.bbox[1]);
                System.out.println(parameters.bbox[2]);
                System.out.println(parameters.bbox[3]);
                SimpleFeatureSource featuresource = DataUtilities.source(featureCollection);
//                Color styleColor=new Color(255,0,0);
                Style style = SLD.createSimpleStyle(featuresource.getSchema(),styleColor);
                FeatureLayer layer = new FeatureLayer(featuresource, style);
                map.addLayer(layer);
                bufferedReader.close();
                read.close();
                parameters.map = map;

            }
            else {
                System.out.println("找不到指定的文件");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return parameters;
    }
    public void getMapContent(Map paras, String imgPath,MapContent map){
        try{
            double[] bbox = (double[]) paras.get("bbox");
            double x1 = bbox[0], y1 = bbox[1],
                    x2 = bbox[2], y2 = bbox[3];
            int width = (int) paras.get("width"),
                    height=(int) paras.get("height");

            // 设置输出范围
            CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
            ReferencedEnvelope mapArea = new ReferencedEnvelope(x1, x2, y1, y2, crs);
            // 初始化渲染器
            StreamingRenderer sr = new StreamingRenderer();
            sr.setMapContent(map);
            // 初始化输出图像
            BufferedImage bi = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.getGraphics();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Rectangle rect = new Rectangle(0, 0, width, height);
            // 绘制地图
            sr.paint((Graphics2D) g, rect, mapArea);
            //将BufferedImage变量写入文件中。
            ImageIO.write(bi,"png",new File(imgPath));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Vector2Image().test_2shp_img();
    }
    public void test_2shp_img(){

        //两个图形叠加生成img
        long start = System.currentTimeMillis();
        Color styleColor1=new Color(255,0,0);
        Color styleColor2=new Color(0, 0, 255);
        MapContent map = new MapContent();
        Vector2Image shape2Image = new Vector2Image();
        System.out.println("第一个");
        Parameters parameters = shape2Image.addShapeLayer("/home/runxuan/data/TIGER/STATE.csv",map,styleColor1,"multipolygon");
        map = parameters.map;
        System.out.println("第二个");
        Parameters parameters2 = shape2Image.addShapeLayer("/home/runxuan/data/TIGER/PRIMARYROADS.csv",map,styleColor2,"linestring");
        map = parameters2.map;
        double[] bbox = new double[]{parameters.bbox[0],parameters.bbox[1],parameters.bbox[2],parameters.bbox[3]};
//        System.out.println(parameters.bbox[0]+"\n"+parameters.bbox[1]+"\n"+parameters.bbox[2]+"\n"+parameters.bbox[3]);
        Map paras = new HashMap();
        paras.put("bbox", bbox);
        paras.put("width", (int)((bbox[2]-bbox[0])*100));
        paras.put("height", (int)((bbox[3]-bbox[1])*100));
        JMapFrame.showMap(map);
        shape2Image.getMapContent(paras,"/home/runxuan/data/generated_img/COUNTY.png",map);
        System.out.println("图片生成完成，共耗时"+(System.currentTimeMillis() - start)+"ms");
    }

}
