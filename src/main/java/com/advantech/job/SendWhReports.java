/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.job;

import com.advantech.helper.MailManager;
import com.advantech.model.MaterialNumberSum;
import com.advantech.model.ScrappedDetail;
import com.advantech.model.UserNotification;
import com.advantech.service.UserNotificationService;
import com.advantech.service.WorkingHoursService;
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
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Wei.Cheng
 */
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
            String mailBody = generateMailBody();
            String mailTitle = "" + fmt.print(now);

            manager.sendMail(mailTarget, mailCcTarget, mailTitle, mailBody);

        } catch (SAXException | InvalidFormatException | IOException | MessagingException ex) {
            logger.error("Send mail fail.", ex);
        }

    }

    private String generateMailBody() throws IOException, SAXException, InvalidFormatException {

        StringBuilder sb = new StringBuilder();

        //設定mail格式(css...etc)
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

        //Generate DailyWhReport table, send mail when friday
        List daliyList = whService.findDailyWhReport();
        
        //Generate weekly table
        DateTime lastDateOfWeek = new DateTime().withTime(0, 0, 0, 0).dayOfWeek().withMaximumValue();
        lastDateOfWeek = lastDateOfWeek.minusDays(2);
        if (LocalDate.now().compareTo(new LocalDate(lastDateOfWeek)) == 0) {
            List weeklyList = whService.findWeeklyWhReport();
            
        }

        //Generate monthly table
        DateTime lastDateOfMonth = new DateTime().withTime(0, 0, 0, 0).dayOfMonth().withMaximumValue();
        int lastDateMonthOfWeek = lastDateOfMonth.getDayOfWeek();
        lastDateOfMonth = lastDateOfMonth.minusDays(lastDateMonthOfWeek == 7 ? 2 : (lastDateMonthOfWeek == 6 ? 1 : 0));
        if (LocalDate.now().compareTo(new LocalDate(lastDateOfMonth)) == 0) {
            List monthlyList = whService.findMonthlyWhReport();
        }

        return sb.toString();

    }
}
