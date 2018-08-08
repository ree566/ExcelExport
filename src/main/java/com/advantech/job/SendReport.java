/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.job;

import com.advantech.demo.ExcelChart;
import com.advantech.helper.MailManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

/**
 *
 * @author Wei.Cheng
 */
public class SendReport {

    @Autowired
    private ExcelChart excelChart;

    @Autowired
    private MailManager manager;

    public void execute() {
        ChartPanel chartPanel = excelChart.createChart();
        JFreeChart chart = chartPanel.getChart();

        try {
            byte[] image = org.jfree.chart.ChartUtils.encodeAsPNG(chart.createBufferedImage(1024, 768));

            InputStreamSource is = new ByteArrayResource(image);
            Map<String, InputStreamSource> m = new HashMap();
            m.put("img1", is);

            String[] to = {"Wei.Cheng@advantech.com.tw"};
            manager.sendMail(to, "test", "<img src=\"cid:img1\"></img>", m);
        } catch (IOException | MessagingException ex) {
            System.out.println(ex);
        }
    }
}
