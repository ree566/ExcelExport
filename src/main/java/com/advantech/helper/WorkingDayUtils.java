/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.helper;

import static com.google.common.collect.Sets.newHashSet;
import java.util.HashSet;
import java.util.Set;
import org.joda.time.DateTime;
import static org.joda.time.DateTimeConstants.FRIDAY;
import static org.joda.time.DateTimeConstants.MONDAY;
import static org.joda.time.DateTimeConstants.THURSDAY;
import static org.joda.time.DateTimeConstants.TUESDAY;
import static org.joda.time.DateTimeConstants.WEDNESDAY;
import org.joda.time.LocalDate;

/**
 *
 * @author Wei.Cheng
 */
public class WorkingDayUtils {

    public static double findBusinessDayPercentage(DateTime dt) {
        // I've hardcoded the holidays as LocalDates
        // and put them in a Set
        final Set<LocalDate> holidays = new HashSet(0);
        // For the sake of efficiency, I also put the business days into a Set.
        // In general, a Set has a better lookup speed than a List.
        final Set<Integer> businessDays = newHashSet(
                MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
        );

        if (!businessDays.contains(dt.dayOfWeek().get())) {
            return -1d;
        }

        int period = new DateTime(dt).dayOfMonth().getMaximumValue();

        int curr = 0, total = 0;

        dt = new DateTime(dt).withTime(0, 0, 0, 0);
        DateTime d = new DateTime(dt).dayOfMonth().withMinimumValue().withTime(0, 0, 0, 0);
        
        for (int i = 1; i <= period; i++) {
            if (businessDays.contains(d.dayOfWeek().get())) {
                total++;
                if (d.isEqual(dt)) {
                    curr = total;
                }
            }
            d = d.plusDays(1);
        }

        return curr * 1.0 / total;
    }
}
