/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalFieldValueException;

/*
Copyright 2014 Andrew MacKinlay
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
/**
 *
 */
public class DateConversion {

    /**
     * Convert a date from US customary Year-WeekNumber-Day format to a Joda
     * DateTime object.
     *
     * Identical to {@link #fromUSWeekAndYear(int, int, int, boolean)} but with
     * <code>permissive</code> set to false, so that exceptions are thrown for
     * invalid (but interpretable) values.
     *
     * @param year
     * @param usWeek
     * @param usWeekDay
     * @return
     */
    public static DateTime fromUSWeekAndYear(int year, int usWeek, int usWeekDay) {
        return fromUSWeekAndYear(year, usWeek, usWeekDay, false);
    }

    /**
     * Convert a date from US customary Year-WeekNumber-Day format to a Joda
     * DateTime object
     * <p>
     * </p>
     * US week numbers correspond to the <b>%U</b> format specifier in the C
     * standard library's <code>strftime</code> function. They assume that weeks
     * start on Sunday (with zero-based day numbering) These can range from 0 to
     * 53, although not in all years, as it depends on the weekday on which New
     * Year's Day falls. If it falls on Sunday, there is no week 0, but
     * otherwise all days preceding the first Sunday are in week 0. The final
     * week of each year is either 52 or 53.
     *
     * @param permissive If true, allow malformed variants in the date, when
     * that can be sensibly interpreted. For example, when this is set to
     * <code>true</code> if the week day in weeks 0, 52 or 53 causes crossing a
     * year boundary (so the returned date's year is different to passed-in
     * year), no exception will be thrown where it would be otherwise. This also
     * allows week numbers outside the range <code>[0, 53]</code>.
     * @param year The calendar year
     * @param usWeek The US week number, which can range from 0-53
     * @param usWeekDay The zero-based US day number, where Sunday is 0 and
     * Saturday is 6
     * @return
     * @throws org.joda.time.IllegalFieldValueException If one of the values is
     * invalid, and <code>permissive</code> is false
     */
    public static DateTime fromUSWeekAndYear(int year, int usWeek, int usWeekDay, boolean permissive) {
        if (!permissive && (usWeek < 0 || usWeek > 53)) {
            throw new IllegalFieldValueException("US Week Number", usWeek, 0, 53);
        }
        final DateTime nyd = startOfDay(year, 1, 1);
        DateTime weekOneStart = nyd.withDayOfWeek(7); // us week one always starts on first sunday
        final DateTime usWeekStart = weekOneStart.plusWeeks(usWeek - 1); // week 0 starts up to 6 days before week 1
        // this also allows slightly malformed dates where we push back week 0 into the previous year.
        int isoWeekDay = usWeekDay == 0 ? 7 : usWeekDay; // since it's zero-based, only 7 or 0 are different.
        final DateTime possiblyOffsetDate = usWeekStart.withDayOfWeek(isoWeekDay);
        // if it's not sunday, the .withDayOfWeek call will have taken us back to the week before, so
        // we have to undo that
        final DateTime result = isoWeekDay == 7 ? possiblyOffsetDate : possiblyOffsetDate.plusWeeks(1);
        if (!permissive && result.getYear() != year) {
            throw new IllegalFieldValueException("US Week Day / Week Number", String.format("%d:%d", usWeek, usWeekDay));
        }
        return result;
    }

    protected static DateTime startOfDay(int year, int month, int day) {
        return new DateTime(year, month, day, 0, 0, DateTimeZone.UTC);
    }

}
