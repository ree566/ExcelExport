/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.demo.ExcelChart;
import com.advantech.model.ScrappedDetail;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.mail.MessagingException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.xml.sax.SAXException;

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

    @Autowired
    private ExcelReader reader;

    private DateTime sDOW, eDOW;

    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");

    @Test
    public void testMail() throws MessagingException, IOException {
        ChartPanel chartPanel = new ExcelChart().createChart();
        JFreeChart chart = chartPanel.getChart();
        byte[] image = org.jfree.chart.ChartUtils.encodeAsPNG(chart.createBufferedImage(1800, 768));
        InputStreamSource is = new ByteArrayResource(image);
        Map<String, InputStreamSource> m = new HashMap();
        m.put("img1", is);

        String[] to = {"Wei.Cheng@advantech.com.tw"};
        String mailBody = generateMailBody();
        manager.sendMail(to, "test", mailBody, m);
    }

    private List getExcelData() throws IOException, SAXException, InvalidFormatException {
        String xmlConfig = "\\excel-template\\ScrappedDetail.xml";
        String desktop = System.getProperty("user.home") + "/Desktop";
        String dataXLS = desktop + "/2018不良品&良品表單.xlsx";
        List<ScrappedDetail> l = reader.read(xmlConfig, dataXLS);
        return l;
    }

    private List<ScrappedDetail> filterResult(List<ScrappedDetail> l) {
        return l.stream().filter(p -> isDateBetweens(p.getCreateDate())).collect(toList());
    }

    private void updateDateRange(DateTime d) {
        sDOW = d.minusDays(d.getDayOfWeek() - 1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        eDOW = sDOW.plusDays(7 - 1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
    }

    private boolean isDateBetweens(Date date) {
        DateTime d = new DateTime(date);
        return (sDOW.isBefore(d) || sDOW.isEqual(d)) && (eDOW.isAfter(d) || eDOW.isEqual(d));
    }

    private String generateMailBody() {
        try {
            updateDateRange(new DateTime("2018-05-22"));
            List<ScrappedDetail> l = filterResult(getExcelData());

            assertNotEquals(0, l.size());

            StringBuilder sb = new StringBuilder();

            sb.append("<style>");
            sb.append("table {border-collapse: collapse; padding:5px; }");
            sb.append("table, th, td {border: 1px solid black;}");
            sb.append("table th {background-color: yellow;}");
            sb.append("</style>");
            sb.append("<h3>Dear All:</h3>");
            sb.append("<h3>本週報廢明細如下:</h3>");
            sb.append("<h5>5F:</h5>");
            sb.append("<hr />");
            sb.append("<h5>6F:</h5>");
            sb.append("<table>");
            sb.append("<tr>");
            sb.append("<th>日期</th>");
            sb.append("<th>工單</th>");
            sb.append("<th>機種</th>");
            sb.append("<th>料號</th>");
            sb.append("<th>數量</th>");
            sb.append("<th>退料原因</th>");
            sb.append("<th>類別</th>");
            sb.append("<th>單價</th>");
            sb.append("<th>總金額</th>");
            sb.append("<th>疏失人員</th>");
            sb.append("</tr>");

            //日期	工單	機種	料號	數量	退料原因	類別	單價	總金額	疏失人員
            for (ScrappedDetail detail : l) {
                sb.append("<tr>");
                sb.append("<td>");
                sb.append(fmt.print(new DateTime(detail.getCreateDate())));
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getPo());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getModelName());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getMaterialNumber());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getAmount());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getReason());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getKind());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getPrice());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getPrice() * detail.getAmount());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(detail.getNegligenceUser());
                sb.append("</td>");

                sb.append("</tr>");
            }
            sb.append("</table>");
            sb.append("<hr />");
            sb.append("<h5>報廢指數表:</h5>");
            sb.append("<img src=\"cid:img1\"></img>");

            return sb.toString();
        } catch (IOException | SAXException | InvalidFormatException ex) {
            System.out.println(ex);
            return null;
        }

    }
}
