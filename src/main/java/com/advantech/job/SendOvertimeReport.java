/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.job;

import com.advantech.chart.ExcelChart2;
import com.advantech.helper.MailManager;
import com.advantech.model.db1.MaterialNumberSum;
import com.advantech.model.db1.OvertimeRecord;
import com.advantech.model.db1.OvertimeRecordWeekly;
import com.advantech.model.db1.ScrappedDetail;
import com.advantech.model.db1.ScrappedDetailCount;
import com.advantech.model.db1.User;
import com.advantech.model.db1.UserNotification;
import com.advantech.service.OvertimeRecordService;
import com.advantech.service.ScrappedDetailService;
import com.advantech.service.UserNotificationService;
import com.advantech.service.UserService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.mail.MessagingException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 *
 * @author Wei.Cheng
 *
 */
@Component
public class SendOvertimeReport {

    private static final Logger logger = LoggerFactory.getLogger(SendOvertimeReport.class);

    @Autowired
    private MailManager manager;

    @Autowired
    private UserNotificationService notificationService;

    @Autowired
    private ExcelChart2 excelChart2;

    @Autowired
    private OvertimeRecordService overtimeRecordService;

    @Autowired
    private UserService userService;

    private DateTime sDOW, eDOW;

    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");
    DateTimeFormatter fmt2 = DateTimeFormat.forPattern("M/d");

    public void execute() {
        sendMail();
    }

    public void sendMail() {
        try {
            UserNotification notifi = notificationService.findById(1).get();
            UserNotification notifiCc = notificationService.findById(2).get();

            String[] mailTarget = findUsersMail(notifi);
            String[] mailCcTarget = findUsersMail(notifiCc);
//            String[] mailTarget = {"Wei.Cheng@advantech.com.tw"};
//            String[] mailCcTarget = {};

            if (mailTarget.length == 0) {
                logger.info("Job sendReport can't find mail target in database table.");
                return;
            }

            updateDateRange();

            ChartPanel chartPanel = excelChart2.createChart();
            JFreeChart chart = chartPanel.getChart();
            byte[] image = org.jfree.chart.ChartUtils.encodeAsPNG(chart.createBufferedImage(1800, 768));
            InputStreamSource is = new ByteArrayResource(image);
            Map<String, InputStreamSource> m = new HashMap();
            m.put("img1", is);

            String mailBody = generateMailBody();
            String mailTitle = "5F-6F樓每週加班明細" + fmt2.print(sDOW) + "~" + fmt2.print(eDOW);

            manager.sendMail(mailTarget, mailCcTarget, mailTitle, mailBody, m);

        } catch (SAXException | InvalidFormatException | IOException | MessagingException ex) {
            logger.error("Send mail fail.", ex);
        }
    }

    private String[] findUsersMail(UserNotification notifi) {
        List<User> l = userService.findByUserNotifications(notifi);
        return l.stream().map(u -> u.getEmail()).toArray(size -> new String[size]);
    }

    private void updateDateRange() {
        DateTime lastDayOfPrevWeek = new DateTime().minusWeeks(1).withDayOfWeek(DateTimeConstants.SATURDAY);
        sDOW = new DateTime(lastDayOfPrevWeek).minusWeeks(4).withTime(0, 0, 0, 0);
        eDOW = new DateTime(lastDayOfPrevWeek).withTime(23, 0, 0, 0);
    }

    public String generateMailBody() throws IOException, SAXException, InvalidFormatException {

        int weekOfWeekyear = eDOW.getWeekOfWeekyear();

        List<OvertimeRecordWeekly> l = overtimeRecordService.findWeeklyOvertimeRecord(sDOW, eDOW);
        l = l.stream().filter(o -> o.getWeekOfMonth() == weekOfWeekyear).collect(toList());

        List<OvertimeRecord> l2 = overtimeRecordService.findOvertimeRecord(sDOW, eDOW);

        StringBuilder sb = new StringBuilder();

        //設定mail格式(css...etc)
        sb.append("<style>");
        sb.append("table {border-collapse: collapse; padding:5px; }");
        sb.append("table, th, td {border: 1px solid black;}");
        sb.append("table th {background-color: yellow;}");
        sb.append("#mailBody {font-family: 微軟正黑體;}");
        sb.append(".highlight {background-color: yellow;} .alert {color: red}");
        sb.append("</style>");
        sb.append("<div id='mailBody'>");
        sb.append("<h3>Dear User:</h3>");
        sb.append("<h3>本週加班明細如下:</h3>");

        sb.append("<h5 class='alert'>僅列出當週資料</h5>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<th>週別</th>");
        sb.append("<th>樓層</th>");
        sb.append("<th>直/間接</th>");
        sb.append("<th>時數</th>");
        sb.append("</tr>");

        //週加班明細
        for (OvertimeRecordWeekly orw : l) {
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(orw.getWeekOfMonth());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(orw.getSitefloor());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(orw.getDep());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(orw.getSumAMultiple());
            sb.append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table>");
        sb.append("<hr />");

        //Chart data
        sb.append("<h5>統計圖:</h5>");
        sb.append("<img src=\"cid:img1\"></img>");
        sb.append("<hr />");

        int rankNumLimit = 5;

        List<OvertimeRecord> floorFiveTopN = l2.stream()
                .filter(o -> o.getSitefloor().equals("5") && (o.getWeekOfMonth() == null || Objects.equals(o.getWeekOfMonth(), weekOfWeekyear)) && o.getRankNum() <= rankNumLimit)
                .sorted((OvertimeRecord o1, OvertimeRecord o2) -> new BigDecimal(o1.getSum()).compareTo(new BigDecimal(o2.getSum())))
                .collect(toList());

        List<OvertimeRecord> floorSixTopN = l2.stream()
                .filter(o -> o.getSitefloor().equals("6") && (o.getWeekOfMonth() == null || Objects.equals(o.getWeekOfMonth(), weekOfWeekyear)) && o.getRankNum() <= rankNumLimit)
                .sorted((OvertimeRecord o1, OvertimeRecord o2) -> new BigDecimal(o1.getSum()).compareTo(new BigDecimal(o2.getSum())))
                .collect(toList());

        //各樓層top 5 order by sum
        if (!floorFiveTopN.isEmpty()) {
            sb.append("<h5>");
            sb.append(weekOfWeekyear);
            sb.append("週 5F top 5</h5>");
            sb.append("<table>");

            //Add header
            sb.append("<tr>");
            sb.append("<th>工號</th>");
            sb.append("<th>名稱</th>");
            sb.append("<th>時數</th>");
            sb.append("</tr>");

            //Add row
            floorFiveTopN.forEach((row) -> {
                sb.append("<tr>");
                sb.append("<td>");
                sb.append(row.getEmplrId());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(row.getLocalName());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(row.getSum());
                sb.append("</td>");
                sb.append("</tr>");
            });

            sb.append("</table>");
        }

        if (!floorSixTopN.isEmpty()) {
            sb.append("<h5>");
            sb.append(weekOfWeekyear);
            sb.append("週 6F top 5</h5>");

            sb.append("<table>");

            //Add header
            sb.append("<tr>");
            sb.append("<th>工號</th>");
            sb.append("<th>名稱</th>");
            sb.append("<th>時數</th>");
            sb.append("</tr>");

            //Add row
            floorSixTopN.forEach((row) -> {
                sb.append("<tr>");
                sb.append("<td>");
                sb.append(row.getEmplrId());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(row.getLocalName());
                sb.append("</td>");
                sb.append("<td>");
                sb.append(row.getSum());
                sb.append("</td>");
                sb.append("</tr>");
            });

            sb.append("</table>");
        }

        sb.append("</div>");

        return sb.toString();

    }

}
