/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.job;

import com.advantech.demo.ExcelChart;
import com.advantech.helper.ExcelReader;
import com.advantech.helper.MailManager;
import com.advantech.model.ScrappedDetail;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.mail.MessagingException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Wei.Cheng
 */
public class SendReport {

    @Autowired
    private MailManager manager;

    @Autowired
    private ExcelReader reader;

    private DateTime sDOW, eDOW;

    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");
    DateTimeFormatter fmt2 = DateTimeFormat.forPattern("M/d");

    public void execute() throws MessagingException, IOException {
        ChartPanel chartPanel = new ExcelChart().createChart();
        JFreeChart chart = chartPanel.getChart();
        byte[] image = org.jfree.chart.ChartUtils.encodeAsPNG(chart.createBufferedImage(1800, 768));
        InputStreamSource is = new ByteArrayResource(image);
        Map<String, InputStreamSource> m = new HashMap();
        m.put("img1", is);

        String[] to = {"Wei.Cheng@advantech.com.tw"};
        String mailBody = generateMailBody();
        String mailTitle = "5F-6F樓每週報廢明細" + fmt2.print(sDOW) + "~" + fmt2.print(eDOW);
        manager.sendMail(to, mailTitle, mailBody, m);
    }

    private List<ScrappedDetail> getFloorSixExcelData() throws IOException, SAXException, InvalidFormatException {
        String xmlConfig = "\\excel-template\\ScrappedDetail_6F.xml";
        String desktop = System.getProperty("user.home") + "/Desktop";
        String dataXLS = desktop + "/2018不良品&良品表單.xlsx";
        List<ScrappedDetail> l = reader.read(xmlConfig, dataXLS);
        return l.stream().filter(p -> isDateBetweens(p.getCreateDate())).collect(toList());
    }

    /*
        Floor five excel data structure is different than floor six(Floor six is better)
        Floor five can't use jxls format data until user change data structure.
    */
    private List<ScrappedDetail> getFloorFiveExcelData() {
        List<ScrappedDetail> l = new ArrayList();
        String fileLocation = System.getProperty("user.home") + "/Desktop/2018不良品良品表單.xlsx";
        try {
            FileInputStream excelFile = new FileInputStream(new File(fileLocation));
            try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
                XSSFSheet datatypeSheet = workbook.getSheet("報   廢");

                for (Row row : datatypeSheet) {
                    Cell checkCell = row.getCell(3);
                    Cell dateCell = row.getCell(2);

                    if ("工單".equals(checkCell.getStringCellValue()) || new DateTime(dateCell.getDateCellValue()).isBefore(sDOW)) {
                        continue;
                    } else if (checkCell.getCellTypeEnum() == CellType.BLANK || new DateTime(dateCell.getDateCellValue()).isAfter(eDOW)) {
                        break;
                    }
                    convertCellToString(checkCell, row.getCell(4), row.getCell(5));
                    ScrappedDetail d = this.rowToDetail(row);
                    l.add(d);
                }
                // Closing the workbook
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return l;
    }

    private void convertCellToString(Cell... cells) {
        for (Cell c : cells) {
            c.setCellType(CellType.STRING);
        }
    }

    private ScrappedDetail rowToDetail(Row row) {
        ScrappedDetail d = new ScrappedDetail();
        d.setCreateDate(row.getCell(2).getDateCellValue());
        d.setPo(row.getCell(3).getStringCellValue());
        d.setModelName(row.getCell(4).getStringCellValue());
        d.setMaterialNumber(row.getCell(5).getStringCellValue());
        d.setAmount((int) row.getCell(6).getNumericCellValue());
        d.setReason(row.getCell(7).getStringCellValue());
        d.setKind(row.getCell(8).getStringCellValue());
        d.setPrice((int) row.getCell(9).getNumericCellValue());
        d.setNegligenceUser(row.getCell(11).getStringCellValue());
        d.setRemark(row.getCell(14).getStringCellValue());
        return d;
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
            updateDateRange(new DateTime());
            List<ScrappedDetail> floorFiveDetail = this.getFloorFiveExcelData();
            List<ScrappedDetail> floorSixDetail = getFloorSixExcelData();

            //Use hashmap mass up the order in current map, so use linkedHashMap instead.
            Map<String, List<ScrappedDetail>> m = new LinkedHashMap();
            m.put("5F", floorFiveDetail);
            m.put("6F", floorSixDetail);

            StringBuilder sb = new StringBuilder();

            sb.append("<style>");
            sb.append("table {border-collapse: collapse; padding:5px; }");
            sb.append("table, th, td {border: 1px solid black;}");
            sb.append("table th {background-color: yellow;}");
            sb.append("</style>");
            sb.append("<h3>Dear All:</h3>");
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

            sb.append("<h5>報廢指數表:</h5>");
            sb.append("<img src=\"cid:img1\"></img>");

            return sb.toString();
        } catch (IOException | SAXException | InvalidFormatException ex) {
            System.out.println(ex);
            return null;
        }

    }
}
