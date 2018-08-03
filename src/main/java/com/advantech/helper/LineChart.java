/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * 折线图
 * <p>
 * 创建图表步骤：<br>
 * 1：创建数据集合<br>
 * 2：创建Chart：<br>
 * 3:设置抗锯齿，防止字体显示不清楚<br>
 * 4:对柱子进行渲染，<br>
 * 5:对其他部分进行渲染<br>
 * 6:使用chartPanel接收<br>
 *
 * </p>
 */
public class LineChart {

    public LineChart() {
    }

    public DefaultCategoryDataset createDataset() {
        // 标注类别
        String[] categories = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        List<Serie> series = new ArrayList();
        // 柱子名称：柱子所有的值集合
        series.add(new Serie("Tokyo", new Double[]{49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4}));
        series.add(new Serie("New York", new Double[]{83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3}));
        series.add(new Serie("London", new Double[]{48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2}));
        series.add(new Serie("Berlin", new Double[]{42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1}));
        // 1：创建数据集合
        DefaultCategoryDataset dataset = ChartUtils.createDefaultCategoryDataset(series, categories);
        return dataset;
    }

    public ChartPanel createChart() {
        // 2：创建Chart[创建不同图形]
        JFreeChart chart = ChartFactory.createLineChart("Monthly Average Rainfall", "", "Rainfall (mm)", createDataset());
        // 3:设置抗锯齿，防止字体显示不清楚
        ChartUtils.setAntiAlias(chart);// 抗锯齿
        // 4:对柱子进行渲染[[采用不同渲染]]
        ChartUtils.setLineRender(chart.getCategoryPlot(), false, true);//
        // 5:对其他部分进行渲染
        ChartUtils.setXAixs(chart.getCategoryPlot());// X坐标轴渲染
        ChartUtils.setYAixs(chart.getCategoryPlot());// Y坐标轴渲染
        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        // 6:使用chartPanel接收
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    public static void main(String[] args) {
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
//        ChartPanel chartPanel = new LineChart().createChart();
//        JFreeChart chart = chartPanel.getChart();
//        try {
//            saveAsFile(chart, "C:\\Users\\wei.cheng\\Desktop\\123.jpg", 1024, 420);
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }

    }

    public static void saveAsFile(JFreeChart chart, String outputPath,
            int weight, int height) throws Exception {
        File outFile = new File(outputPath);
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        FileOutputStream out = new FileOutputStream(outputPath);
        // 保存为PNG      
        org.jfree.chart.ChartUtils.writeChartAsPNG(out, chart, weight, height);
        // 保存为JPEG      
        // ChartUtilities.writeChartAsJPEG(out, chart, weight, height);      
        out.flush();

        try {
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
