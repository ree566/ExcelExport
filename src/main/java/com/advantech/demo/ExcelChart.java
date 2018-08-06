/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.demo;

import com.advantech.helper.ChartUtils;
import com.advantech.helper.Serie;
import static com.google.common.collect.Lists.newArrayList;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
public class ExcelChart {

    List<String> cate;
    List<Serie> ser;
    List<DefaultCategoryDataset> dataset;

    public ExcelChart() {
    }

    private void excelToData() {
        try {
            String fileLocal = "C:\\Users\\wei.cheng\\Desktop\\2018不良品&良品表單.xlsx";
            FileInputStream excelFile = new FileInputStream(new File(fileLocal));
            try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
                XSSFSheet datatypeSheet = workbook.getSheet("工作表6");

                List skipRows = newArrayList(0, 1, datatypeSheet.getPhysicalNumberOfRows() - 1);
                int i = 0;

                cate = new ArrayList();
                ser = new ArrayList();

                List five_floor = new ArrayList();
                List six_floor = new ArrayList();
                List total = new ArrayList();

                for (Row row : datatypeSheet) {
                    if (!skipRows.contains(i++)) {
                        row.forEach(cell -> {
                            int colIdx = cell.getColumnIndex();
                            switch (colIdx) {
                                case 0:
                                    cate.add(cell.getStringCellValue());
                                    break;
                                case 1:
                                    five_floor.add(cell.getNumericCellValue());
                                    break;
                                case 2:
                                    six_floor.add(cell.getNumericCellValue());
                                    break;
                                case 3:
                                    total.add(cell.getCellTypeEnum() == CellType.BLANK ? -1 : cell.getNumericCellValue());
                                default:
                                    break;
                            }
                        });
                    }
                }

                ser.add(new Serie("5F", five_floor.toArray()));
                ser.add(new Serie("6F", six_floor.toArray()));
                ser.add(new Serie("total", total.toArray()));

                // Closing the workbook
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        List l = newArrayList(ser.get(0), ser.get(1));
        List l2 = newArrayList(ser.get(2));

        DefaultCategoryDataset dataset1 = ChartUtils.createDefaultCategoryDataset(l, cate.toArray(new String[cate.size()]));
        DefaultCategoryDataset dataset2 = ChartUtils.createDefaultCategoryDataset(l2, cate.toArray(new String[cate.size()]));

        dataset = newArrayList(dataset1, dataset2);
//        return dataset;
    }

    public ChartPanel createChart() {
        excelToData();
        // 2：创建Chart[创建不同图形]
//        JFreeChart chart = ChartFactory.createBarChart("每周不良統計", "", "AMOUNT", excelToData());
        DefaultCategoryDataset ds1 = dataset.get(0);
        DefaultCategoryDataset ds2 = dataset.get(1);

        CategoryPlot plot = new CategoryPlot();
        JFreeChart chart = new JFreeChart(plot);

        CategoryItemRenderer lineRenderer = new LineAndShapeRenderer();
        plot.setDataset(0, ds1);
        plot.setRenderer(0, lineRenderer);

        CategoryItemRenderer barRenderer = new BarRenderer();
        plot.setDataset(1, ds2);
        plot.setRenderer(1, barRenderer);
        plot.setDomainAxis(new CategoryAxis("日期"));
        plot.setRangeAxis(new NumberAxis("金額"));

        chart.setTitle("報廢金額統計圖");

        // 3:设置抗锯齿，防止字体显示不清楚
        ChartUtils.setAntiAlias(chart);// 抗锯齿
//        // 4:对柱子进行渲染[[采用不同渲染]]
        ChartUtils.setLineRender(plot, 0, false, true);//
        ChartUtils.setBarRenderer(plot, 1, true);
//        // 5:对其他部分进行渲染
        ChartUtils.setXAixs(chart.getCategoryPlot());// X坐标轴渲染
        ChartUtils.setYAixs(chart.getCategoryPlot());// Y坐标轴渲染
//        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        
        ValueAxis axis = plot.getRangeAxis();
        Font font = new Font("微軟正黑體", Font.PLAIN, 20);
        axis.setTickLabelFont(font);
        // 6:使用chartPanel接收
        ChartPanel chartPanel = new ChartPanel(chart);

        return chartPanel;
    }

    public static void main(String[] args) {
//        final JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1200, 600);
//        frame.setLocationRelativeTo(null);
//
//        Runnable task3 = () -> {
//            ChartPanel chartPanel = new ExcelChart().createChart();
//            frame.getContentPane().add(chartPanel);
//            frame.setVisible(true);
//        };
//
//        SwingUtilities.invokeLater(task3);
        ChartPanel chartPanel = new ExcelChart().createChart();
        JFreeChart chart = chartPanel.getChart();
        try {
            saveAsFile(chart, "C:\\Users\\wei.cheng\\Desktop\\123.jpg", 1024, 420);
        } catch (Exception ex) {
            System.out.println(ex);
        }

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
