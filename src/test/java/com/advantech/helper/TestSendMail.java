/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.demo.ExcelChart;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSendMail {

    @Autowired
    private MailManager manager;

    @Test
    public void testMail() throws MessagingException, IOException {
        ChartPanel chartPanel = new ExcelChart().createChart();
        JFreeChart chart = chartPanel.getChart();
        byte[] image = org.jfree.chart.ChartUtils.encodeAsPNG(chart.createBufferedImage(1024, 768));
        InputStreamSource is = new ByteArrayResource(image);
        Map<String, InputStreamSource> m = new HashMap();
        m.put("img1", is);
        
        
        String[] to = {"Wei.Cheng@advantech.com.tw"};
        manager.sendMail(to, "test", "<img src=\"cid:img1\"></img>", m);
    }
}
