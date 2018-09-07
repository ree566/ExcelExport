/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import static com.advantech.helper.DateConversion.*;
import com.google.common.collect.ImmutableMap;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.Entry.comparingByKey;
import java.util.Random;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
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
    public void testException() throws BuJamException {
        throw new BuJamException("Guang is disappear...");
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

    @Test
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
}
