/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.sap;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Wei.Cheng
 */
public class SAPConn {

    private static final String SAP_NAME = "SAPMES";

    static {
        Properties jcoPropeties = new Properties();

//        jcoPropeties.setProperty("jco.client.client", "000");
//        jcoPropeties.setProperty("jco.client.user", "MY_USER");
//        jcoPropeties.setProperty("jco.client.passwd", "SAPSAP");
//        jcoPropeties.setProperty("jco.client.lang", "EN");
//        jcoPropeties.setProperty("jco.client.r3name", "ABC");
//        jcoPropeties.setProperty("jco.client.group", "PUBLIC");
//        jcoPropeties.setProperty("jco.client.mshost", "myhost");
//        jcoPropeties.setProperty("jco.client.type", "3");
//        jcoPropeties.setProperty("jco.destination.peak_limit", "20");
//        jcoPropeties.setProperty("jco.destination.pool_capacity", "10");

        jcoPropeties.setProperty("jco.client.ashost", "172.20.3.6");// 服务器
        jcoPropeties.setProperty("jco.client.sysnr", "06"); // 系统编号
        jcoPropeties.setProperty("jco.client.client", "168"); // SAP集团
        jcoPropeties.setProperty("jco.client.user", "MES.ACL"); // SAP用户名
        jcoPropeties.setProperty("jco.client.passwd", "MESMES"); // 密码
        jcoPropeties.setProperty("jco.client.lang", "ZF"); // 登录语言:ZH EN
        jcoPropeties.setProperty("jco.destination.pool_capacity", "3"); // 最大连接数
        jcoPropeties.setProperty("jco.destination.peak_limit", "10"); // 最大连接线程

        createDataFile(SAP_NAME, "jcoDestination", jcoPropeties);
    }

    /**
     * 创建SAP接口属性文件。
     *
     * @param name ABAP管道名称
     * @param suffix 属性文件后缀
     * @param properties 属性文件内容
     */
    private static void createDataFile(String name, String suffix, Properties properties) {
        File cfg = new File(name + "." + suffix);
        System.out.println(name + "." + suffix);
        if (cfg.exists()) {
            cfg.deleteOnExit();
        }
        try {
            cfg.createNewFile(); 
            try (FileOutputStream fos = new FileOutputStream(cfg, false)) {
                properties.store(fos, "for tests only !");
            }
        } catch (IOException e) {
            System.out.println("Create Data file fault, error msg: " + e.toString());
            throw new RuntimeException("Unable to create the destination file " + cfg.getName(), e);
        }
    }

    /*
      * * 获取SAP连接
      * 
      * @return SAP连接对象
     */
    public static JCoDestination connect() {

        JCoDestination destination = null;
        try {
            destination = JCoDestinationManager.getDestination(SAP_NAME);
        } catch (JCoException e) {
            System.out.println("Connect SAP fault, error msg: " + e.toString());
        }
        return destination;
    }
}
