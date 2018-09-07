/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.chart.ExcelChart;
import static com.advantech.helper.DateConversion.fromUSWeekAndYear;
import com.advantech.model.ScrappedDetailWeekGroup;
import com.advantech.repo.ScrappedDetailRepository;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfree.chart.ChartPanel;
import org.joda.time.DateTime;
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

        //同月, 最大week number, sum
        Map collect = d.stream()
                .collect(
                        Collectors.groupingBy(
                                p -> getMonth(fromUSWeekAndYear(2018, p.getWeek(), 1)),
                                Collectors.collectingAndThen(
                                        Collectors.maxBy(Comparator.comparing(ScrappedDetailWeekGroup::getWeek)),
                                        Optional::get
                                )
                        )
                );
                                        //                                        Collectors.collectingAndThen(
                                        //                                                Collectors.summingInt(ScrappedDetailWeekGroup::getTotal),
                                //                                        ).

        HibernateObjectPrinter.print(collect);

    }

    private Integer getMonth(DateTime d) {
        return d.getMonthOfYear();
    }

    @Test
    public void test() throws IOException, Exception {

        ChartPanel chartPanel = excelChart.createChart();
        excelChart.saveAsFileDemo(chartPanel.getChart());

    }

}
