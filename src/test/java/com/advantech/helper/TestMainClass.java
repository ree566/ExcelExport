/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.chart.LineChart;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartPanel;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class TestMainClass {

    @Test
    public void testGui() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 420);
        frame.setLocationRelativeTo(null);

        Runnable task3 = () -> {
            ChartPanel chartPanel = new LineChart().createChart();
            frame.getContentPane().add(chartPanel);
            frame.setVisible(true);
        };

        SwingUtilities.invokeLater(task3);
    }

}
