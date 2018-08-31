/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.chart.ExcelChart;
import com.advantech.model.ScrappedDetailWeekGroup;
import com.advantech.repo.ScrappedDetailRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TestGeneratePivot {

    @Autowired
    ExcelChart excelChart;

    @Autowired
    private ScrappedDetailRepository repo;

//    @Test
    public void testGroupBy() {
        List<ScrappedDetailWeekGroup> d = repo.findAllGroupByWeek();

        Map<String, List<ScrappedDetailWeekGroup>> dGroup = d.stream()
                .collect(groupingBy(ScrappedDetailWeekGroup::getFloorName));

        List<Integer> floorFiveTotal = dGroup.get("5F").stream()
                        .map(ScrappedDetailWeekGroup::getTotal)
                        .collect(Collectors.toList());
        
        List<Integer> floorSixTotal = dGroup.get("6F").stream()
                        .map(ScrappedDetailWeekGroup::getTotal)
                        .collect(Collectors.toList());

        HibernateObjectPrinter.print(dGroup);
        HibernateObjectPrinter.print(floorFiveTotal);
        HibernateObjectPrinter.print(floorSixTotal);
    }

    @Test
    public void test() throws IOException, Exception {

        ChartPanel chartPanel = excelChart.createChart();
        excelChart.saveAsFileDemo(chartPanel.getChart());

    }

}
