package org.apache.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class HDFS_Op {
    public static void upload() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://219.217.203.1:9000");
        FileSystem fs = FileSystem.get(conf);
        fs.copyFromLocalFile(new Path("F:\\chrome_download"), new Path("/user/demo"));
    }

    public static void removeFile() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://219.217.203.1:9000");
        FileSystem fs = FileSystem.newInstance(conf);
        fs.delete(new Path("/user/demo"), true);
    }

    public static void main(String[] args) throws IOException {
        HDFS_Op.upload();
//        HDFS_Op.removeFile();
    }

//    public static void main(String[] args) throws Exception {
////        System.setProperty("hadoop.home.dir", "D:\\hadoop-3.2.2");
//        Configuration conf = new Configuration();
////        conf.set("dfs.replication", "3");
//        FileSystem hdfs = FileSystem.get(new URI("hdfs://node1:9000"), conf, "root");
//
//        //读取本地文件
//        InputStream in = new FileInputStream("D:/1.pdf");
//        //在Hdfs上创建一个文件，返回输出流
//        OutputStream out = hdfs.create(new Path("/1.pdf"));
//        //输入 ---》  输出
//        IOUtils.copyBytes(in, out, 4096, true);
//
//    }


}
