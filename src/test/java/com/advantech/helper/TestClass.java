/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

    @Test
    public void testCollection() {
        List<Integer> l = newArrayList(1, 3, 4, 5, 2, 7, 8, 9, 2, 33, 14, 56, 0, 1);
        List<Integer> l2 = newArrayList(2, 9, 8, 21, 3, 9, 5, 11, 2, 14);

        System.out.println(CollectionUtils.subtract(l, l2));
        System.out.println(CollectionUtils.subtract(l2, l));
    }
}
