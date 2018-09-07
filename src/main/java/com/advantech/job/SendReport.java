/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.job;

import com.advantech.chart.ExcelChart;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.MailManager;
import com.advantech.model.ScrappedDetail;
import com.advantech.model.User;
import com.advantech.model.UserNotification;
import com.advantech.service.ScrappedDetailService;
import com.advantech.service.UserNotificationService;
import com.advantech.service.UserService;
import static com.google.common.base.Preconditions.checkState;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.mail.MessagingException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.joda.time.DateTime;
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
 * @author Wei.Cheng To: Timmy.Hsu Cc: Allen.Chu; Ls.He; Yichun.Chen; Sen.Hung;
 * Abby.Chen; Candice.Kung; Anderson.Chen; Johnny.Lin; EVON.CHUANG; Rain.Chu;
 * Kai.Yang; Momo.Hsieh; LU.Zheng; Apple.Hsieh; Dongni.Zheng
 *
 */
@Component
public class SendReport {

    private static final Logger logger = LoggerFactory.getLogger(SendReport.class);

    @Autowired
    private MailManager manager;

    @Autowired
    private UserNotificationService notificationService;

    @Autowired
    private ExcelChart excelChart;

    @Autowired
    private ScrappedDetailService scrappedDetailService;

    @Autowired
    private UserService userService;

    private DateTime sDOW, eDOW;

    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");
    DateTimeFormatter fmt2 = DateTimeFormat.forPattern("M/d");

    public void execute() {
        sendMail(new DateTime());
    }

    public void sendMail(DateTime d) {
        try {
            UserNotification notifi = notificationService.findById(1).get();
            UserNotification notifiCc = notificationService.findById(2).get();

            String[] mailTarget = findUsersMail(notifi);
            String[] mailCcTarget = findUsersMail(notifiCc);
//            String[] mailTarget = {"Wei.Cheng@advantech.com.tw"};
//            String[] mailCcTarget = {};

            updateDateRange(d);

            ChartPanel chartPanel = excelChart.createChart();
            JFreeChart chart = chartPanel.getChart();
            byte[] image = org.jfree.chart.ChartUtils.encodeAsPNG(chart.createBufferedImage(1800, 768));
            InputStreamSource is = new ByteArrayResource(image);
            Map<String, InputStreamSource> m = new HashMap();
            m.put("img1", is);

            String mailBody = generateMailBody();
            String mailTitle = "5F-6F樓每週報廢明細" + fmt2.print(sDOW) + "~" + fmt2.print(eDOW);

            manager.sendMail(mailTarget, mailCcTarget, mailTitle, mailBody, m);

        } catch (SAXException | InvalidFormatException | IOException | MessagingException ex) {
            logger.error("Send mail fail.", ex);
        }
    }

    private String[] findUsersMail(UserNotification notifi) {
        List<User> l = userService.findByUserNotifications(notifi);
        return l.stream().map(u -> u.getEmail()).toArray(size -> new String[size]);
    }

    private void updateDateRange(DateTime d) {
        sDOW = d.minusDays(d.getDayOfWeek() - 1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        eDOW = sDOW.plusDays(7 - 1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
    }

    private String generateMailBody() throws IOException, SAXException, InvalidFormatException {

        List<ScrappedDetail> l = scrappedDetailService.findByCreateDateBetween(sDOW, eDOW);
        List<ScrappedDetail> floorFiveDetail = l.stream().filter(s -> s.getFloor().getId() == 1).collect(toList());
        List<ScrappedDetail> floorSixDetail = l.stream().filter(s -> s.getFloor().getId() == 2).collect(toList());

        //Use hashmap mass up the order in current map, so use linkedHashMap instead.
        Map<String, List<ScrappedDetail>> m = new LinkedHashMap();
        m.put("5F", floorFiveDetail);
        m.put("6F", floorSixDetail);

        StringBuilder sb = new StringBuilder();

        sb.append("<style>");
        sb.append("table {border-collapse: collapse; padding:5px; }");
        sb.append("table, th, td {border: 1px solid black;}");
        sb.append("table th {background-color: yellow;}");
        sb.append("#mailBody {font-family: 微軟正黑體;}");
        sb.append(".highlight {background-color: yellow;}");
        sb.append("</style>");
        sb.append("<div id='mailBody'>");
        sb.append("<h3>Dear User:</h3>");
        sb.append("<h3>本週報廢明細如下:</h3>");

        for (Map.Entry<String, List<ScrappedDetail>> entry : m.entrySet()) {
            String key = entry.getKey();
            List<ScrappedDetail> details = entry.getValue();

            sb.append("<h5>");
            sb.append(key);
            sb.append("</h5>");
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

            for (ScrappedDetail detail : details) {
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
        }

        int floorFiveSum = floorFiveDetail.stream().collect(Collectors.summingInt(s -> s.getAmount() * s.getPrice()));
        int floorSixSum = floorSixDetail.stream().collect(Collectors.summingInt(s -> s.getAmount() * s.getPrice()));

        sb.append("<h5 class='highlight'>");
        sb.append(sDOW.getWeekOfWeekyear());
        sb.append("週統計-> ");
        sb.append("5F: ");
        sb.append(floorFiveSum);
        sb.append(" 元");
        sb.append(" / ");
        sb.append("6F: ");
        sb.append(floorSixSum);
        sb.append(" 元");

        sb.append("</h5>");

        sb.append("<h5>報廢指數表:</h5>");
        sb.append("<img src=\"cid:img1\"></img>");
        sb.append("</div>");

        return sb.toString();

    }

}
