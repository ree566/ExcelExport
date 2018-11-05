/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.job;

import com.advantech.helper.MailManager;
import com.advantech.model.UserNotification;
import com.advantech.model.WorkingHoursReport;
import com.advantech.service.UserNotificationService;
import com.advantech.service.WorkingHoursService;
import java.io.IOException;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class SendWhReports {

    private static final Logger logger = LoggerFactory.getLogger(SendReport.class);

    @Autowired
    private MailManager manager;

    @Autowired
    private UserNotificationService notificationService;

    @Autowired
    private WorkingHoursService whService;

    private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");

    public void execute() {
        sendMail();
    }

    public void sendMail() {
        try {
            UserNotification notifi = notificationService.findById(1).get();
            UserNotification notifiCc = notificationService.findById(2).get();

//            String[] mailTarget = findUsersMail(notifi);
//            String[] mailCcTarget = findUsersMail(notifiCc);
            String[] mailTarget = {"Wei.Cheng@advantech.com.tw"};
            String[] mailCcTarget = {};

            DateTime now = new DateTime();
            String mailBody = generateMailBody(new DateTime());
            String mailTitle = "" + fmt.print(now);

            manager.sendMail(mailTarget, mailCcTarget, mailTitle, mailBody);

        } catch (SAXException | InvalidFormatException | IOException | MessagingException ex) {
            logger.error("Send mail fail.", ex);
        }

    }

    public String generateMailBody(DateTime dt) throws IOException, SAXException, InvalidFormatException {

        StringBuilder sb = new StringBuilder();

        //設定mail格式(css...etc)
        sb.append("<meta charset=\"UTF-8\">");
        sb.append("<style>");
        sb.append("table {border-collapse: collapse; padding:5px; }");
        sb.append("table, th, td {border: 1px solid black;}");
        sb.append("table th {background-color: yellow;}");
        sb.append("#mailBody {font-family: 微軟正黑體;}");
        sb.append(".highlight {background-color: yellow;}");
        sb.append("</style>");
        sb.append("<div id='mailBody'>");
        sb.append("<h3>Dear User:</h3>");
        sb.append("<h3>SAP產值資料如下(");
        sb.append(fmt.print(dt));
        sb.append("):</h3>");

        //Generate DailyWhReport table, send mail when friday
        List<WorkingHoursReport> daliyList = whService.findDailyWhReport(dt);

        sb.append("<h5>Daily report(7日)</h5>");

        addTable(daliyList, sb);

        //Generate weekly table
        DateTime lastDateOfWeek = dt.withTime(0, 0, 0, 0).dayOfWeek().withMaximumValue();
        lastDateOfWeek = lastDateOfWeek.minusDays(2);
        if (dt.toLocalDate().compareTo(new LocalDate(lastDateOfWeek)) == 0) {
            List weeklyList = whService.findWeeklyWhReport(dt);
            sb.append("<h5>Weekly report(4週)</h5>");
            addTable(weeklyList, sb);
        }

        //Generate monthly table
        DateTime lastDateOfMonth = dt.withTime(0, 0, 0, 0).dayOfMonth().withMaximumValue();
        int lastDateMonthOfWeek = lastDateOfMonth.getDayOfWeek();
        lastDateOfMonth = lastDateOfMonth.minusDays(lastDateMonthOfWeek == 7 ? 2 : (lastDateMonthOfWeek == 6 ? 1 : 0));
        if (dt.toLocalDate().compareTo(new LocalDate(lastDateOfMonth)) == 0) {
            List monthlyList = whService.findMonthlyWhReport(dt);
            sb.append("<h5>Monthly report</h5>");
            addTable(monthlyList, sb);
        }

        return sb.toString();

    }

    private void addTable(List<WorkingHoursReport> l, StringBuilder sb) {
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<th>日期</th>");
        sb.append("<th>Quantity</th>");
        sb.append("<th>SAP工時</th>");
        sb.append("<th>SAP產值</th>");
        sb.append("<th>廠別</th>");
        sb.append("</tr>");

        for (WorkingHoursReport whr : l) {
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(whr.getDateField());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(whr.getQuantity());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(whr.getSapWorktime());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(whr.getSapOutputValue());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(whr.getPlant());
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("<hr />");
    }
}
