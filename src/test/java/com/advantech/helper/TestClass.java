/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import static com.advantech.helper.DateConversion.*;
import com.google.common.collect.ImmutableMap;
import static com.google.common.collect.Lists.newArrayList;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.Entry.comparingByKey;
import java.util.Random;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

//    @Test
    public void testDerCool() {
        new Random().ints(1, 50)
                .distinct()
                .limit(12)
                .boxed()
                .sorted()
                .collect(Collectors.toList())
                .forEach(i -> customP(i));
    }

    public void customP(Integer i) {
        System.out.printf("Number: %d\n", i);
    }

//    @Test
    public void testDateTime() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");

        DateTime d = new DateTime();

        DateTime sDOW = d.minusDays(d.getDayOfWeek() - 1);
        DateTime eDOW = sDOW.plusDays(7 - 1);

        System.out.println(fmt.print(sDOW));
        System.out.println(fmt.print(eDOW));
    }

//    @Test
    public void testAscii() {
        String str = "&#x5c0f;&#x5149;&#x4f60;&#x597d;";
        String s = "";
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            System.out.println(str.charAt(r.nextInt(str.length())));
        }
        System.out.println(s);
    }

//    @Test
    public void testCollection() {
        List<Integer> l = newArrayList(1, 3, 4, 5, 2, 7, 8, 9, 2, 33, 14, 56, 0, 1);
        List<Integer> l2 = newArrayList(2, 9, 8, 21, 3, 9, 5, 11, 2, 14);

        System.out.println(CollectionUtils.subtract(l, l2));
        System.out.println(CollectionUtils.subtract(l2, l));
    }

//    @Test
    public void testDateConversion() {
        assertEquals(startOfDay(2018, 2, 1), fromUSWeekAndYear(2018, 4, 1));
    }

//    @Test
    public void testMapSort() {
        ImmutableMap<String, Integer> map = ImmutableMap
                .<String, Integer>builder()
                .put("一月", 15250)
                .put("七月", 37688)
                .put("三月", 27346)
                .put("九月", 6218)
                .put("二月", 18983)
                .put("五月", 31542)
                .put("六月", 48241)
                .put("八月", 26126)
                .put("四月", 51494)
                .build();

        HibernateObjectPrinter.print(map);

        Map collect = map.entrySet()
                .stream()
                .sorted(comparingByKey())
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        HibernateObjectPrinter.print(collect);

    }

//    @Test
    public void testBigDecimal() {
        String str = "9.680017514E9";
        System.out.println(Long.toString(new BigDecimal(str).longValue()));
    }

//    @Test
    public void testDateTime2() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");
        
        DateTime lastDateOfWeek = new DateTime().withTime(0, 0, 0, 0).dayOfWeek().withMaximumValue();
        lastDateOfWeek = lastDateOfWeek.minusDays(2);
        System.out.println(fmt.print(lastDateOfWeek));
        
        DateTime lastDateOfMonth = new DateTime().withTime(0, 0, 0, 0).dayOfMonth().withMaximumValue();
        int lastDateMonthOfWeek = lastDateOfMonth.getDayOfWeek();
        lastDateOfMonth = lastDateOfMonth.minusDays(lastDateMonthOfWeek == 7 ? 2 : (lastDateMonthOfWeek == 6 ? 1 : 0));
        System.out.println(fmt.print(lastDateOfMonth));
        
        System.out.println(LocalDate.now().compareTo(new LocalDate(lastDateOfWeek)) == 0);
    }
    
    @Test
    public void testDateTime3(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");
        DateTime firstDateOfWeek = new DateTime().withDayOfMonth(14).withTime(0, 0, 0, 0).dayOfWeek().withMinimumValue();
        System.out.println(fmt.print(firstDateOfWeek));
    }
    
//    @Test
    public void testDate(){
        Date d = new Date("2019-10-15T10:45:39+08:00");
        HibernateObjectPrinter.print(d);
        DateTime dt = new DateTime(d);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d H:m:s");
        System.out.println(fmt.print(dt));
    }
}
