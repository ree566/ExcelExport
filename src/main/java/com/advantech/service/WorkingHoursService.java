/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.db1.WorkingHoursReport;
import com.advantech.repo.db1.WorkingHoursRepository;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional("tx1")
public class WorkingHoursService {

    @Autowired
    private WorkingHoursRepository repo;

    public List<WorkingHoursReport> findDailyWhReport(DateTime dt) {
        return repo.findDailyWhReport(dt.toDate());
    }

    public List<WorkingHoursReport> findWeeklyWhReport(DateTime dt) {
        return repo.findWeeklyWhReport(dt.toDate());
    }

    public List<WorkingHoursReport> findMonthlyWhReport(DateTime dt) {
        return repo.findMonthlyWhReport(dt.toDate());
    }

    public List<WorkingHoursReport> findDailyWhReportM8(DateTime sD) {
        return repo.findDailyWhReportM8(sD.toDate());
    }

    public List<WorkingHoursReport> findWeeklyWhReportM8(DateTime sD) {
        return repo.findWeeklyWhReportM8(sD.toDate());
    }

    public List<WorkingHoursReport> findMonthlyWhReportM8(DateTime sD) {
        return repo.findMonthlyWhReportM8(sD.toDate());
    }

}
