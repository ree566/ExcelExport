/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.chart;

import com.advantech.helper.ChartUtils;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.Serie;
import com.advantech.model.db1.ScrappedDetailWeekGroup;
import com.advantech.repo.db1.ScrappedDetailRepository;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.commons.collections.CollectionUtils;
import org.jfree.chart.ChartFactory;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class ExcelChart {

    List<String> cate;
    List ser;
    List<DefaultCategoryDataset> dataset;

    @Autowired
    private ScrappedDetailRepository repo;

    private void excelToData() {
        List<ScrappedDetailWeekGroup> d = repo.findAllGroupByWeek();

        cate = newArrayList();
        ser = new ArrayList();

        //先分樓層
        Map<String, List<ScrappedDetailWeekGroup>> dGroup = d.stream()
                .collect(groupingBy(ScrappedDetailWeekGroup::getFloorName));

        List<ScrappedDetailWeekGroup> floorFiveD = dGroup.get("5F");
        List<ScrappedDetailWeekGroup> floorSixD = dGroup.get("6F");

        //取得兩邊都有的week
        List<Integer> c1 = dGroup.get("5F").stream()
                .map(s -> s.getWeek())
                .collect(Collectors.toList());

        List<Integer> c2 = dGroup.get("6F").stream()
                .map(s -> s.getWeek())
                .collect(Collectors.toList());

        List<Integer> totalWeek = (List<Integer>) CollectionUtils.union(c1, c2);

        //將沒有week的資料補0
        List<Integer> floorFiveSub = (List<Integer>) CollectionUtils.subtract(totalWeek, c1);
        List<Integer> floorSixSub = (List<Integer>) CollectionUtils.subtract(totalWeek, c2);

        floorFiveSub.forEach(s -> {
            floorFiveD.add(new ScrappedDetailWeekGroup() {
                @Override
                public int getWeek() {
                    return s;
                }

                @Override
                public String getFloorName() {
                    return "5F";
                }

                @Override
                public int getTotal() {
                    return 0;
                }
            });
        });

        floorSixSub.forEach(s -> {
            floorSixD.add(new ScrappedDetailWeekGroup() {
                @Override
                public int getWeek() {
                    return s;
                }

                @Override
                public String getFloorName() {
                    return "6F";
                }

                @Override
                public int getTotal() {
                    return 0;
                }
            });
        });
        
        //Transform week to string list
        cate = totalWeek.stream().map(s -> s.toString()).collect(Collectors.toList());

        List<Integer> floorFiveTotal = floorFiveD.stream()
                .sorted((ScrappedDetailWeekGroup o1, ScrappedDetailWeekGroup o2) -> o1.getWeek() - o2.getWeek())
                .map(ScrappedDetailWeekGroup::getTotal)
                .collect(Collectors.toList());

        List<Integer> floorSixTotal = floorSixD.stream()
                .sorted((ScrappedDetailWeekGroup o1, ScrappedDetailWeekGroup o2) -> o1.getWeek() - o2.getWeek())
                .map(ScrappedDetailWeekGroup::getTotal)
                .collect(Collectors.toList());

        List<Integer> total = new ArrayList();
        floorFiveTotal.forEach((_item) -> {
            total.add(-1);
        });

        ser.add(new Serie("5F", floorFiveTotal));
        ser.add(new Serie("6F", floorSixTotal));
        ser.add(new Serie("total", total));

        List l = newArrayList(ser.get(0), ser.get(1));
        List l2 = newArrayList(ser.get(2));

        DefaultCategoryDataset dataset1 = ChartUtils.createDefaultCategoryDataset(l, cate.toArray(new String[cate.size()]));
        DefaultCategoryDataset dataset2 = ChartUtils.createDefaultCategoryDataset(l2, cate.toArray(new String[cate.size()]));

        dataset = newArrayList(dataset1, dataset2);
    }

    public ChartPanel createChart() {
        excelToData();
        // 2：创建Chart[创建不同图形]

        DefaultCategoryDataset ds1 = dataset.get(0);
        DefaultCategoryDataset ds2 = dataset.get(1);

//        CategoryPlot plot = new CategoryPlot();
//        JFreeChart chart = new JFreeChart(plot);
        JFreeChart chart = ChartFactory.createLineChart("TEST", "1", "2", ds1);
        CategoryPlot plot = chart.getCategoryPlot();
       

        CategoryItemRenderer lineRenderer = new LineAndShapeRenderer();
        plot.setDataset(0, ds1);
        plot.setRenderer(0, lineRenderer);

        CategoryItemRenderer barRenderer = new BarRenderer();
        plot.setDataset(1, ds2);
        plot.setRenderer(1, barRenderer);
        plot.setDomainAxis(new CategoryAxis("週別"));
        plot.setRangeAxis(new NumberAxis("金額"));

        chart.setTitle("報廢金額統計圖");

        // 3:设置抗锯齿，防止字体显示不清楚
        ChartUtils.setAntiAlias(chart);// 抗锯齿
//        // 4:对柱子进行渲染[[采用不同渲染]]
        ChartUtils.setLineRender(plot, 0, false, true);//Set third param to show number label on each dot
        ChartUtils.setBarRenderer(plot, 1, false);
//        // 5:对其他部分进行渲染
        ChartUtils.setXAixs(chart.getCategoryPlot());// X坐标轴渲染
        ChartUtils.setYAixs(chart.getCategoryPlot());// Y坐标轴渲染
//        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));

        ValueAxis axis = plot.getRangeAxis();
        Font font = new Font("微軟正黑體", Font.PLAIN, 16);
        axis.setTickLabelFont(font);
        axis.setLabelFont(font);

        CategoryAxis caxis = plot.getDomainAxis();
        caxis.setTickLabelFont(font);
        caxis.setLabelFont(font);
        // 6:使用chartPanel接收
        ChartPanel chartPanel = new ChartPanel(chart);

        return chartPanel;
    }

    public void toFrame(ChartPanel chartPanel) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLocationRelativeTo(null);

        Runnable task3 = () -> {
            frame.getContentPane().add(chartPanel);
            frame.setVisible(true);
        };

        SwingUtilities.invokeLater(task3);
    }

    public void saveAsFileDemo(JFreeChart chart) {
        try {
            String desktop = System.getProperty("user.home") + "/Desktop";
            saveAsFile(chart, desktop + "/123.jpg", 1024, 420);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void saveAsFile(JFreeChart chart, String outputPath,
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
