/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.OvertimeRecord;
import com.advantech.model.OvertimeRecordWeekly;
import com.advantech.repo.db1.OvertimeRecordRepository;
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
@Transactional
public class OvertimeRecordService {

    @Autowired
    private OvertimeRecordRepository repo;

    public List<OvertimeRecord> findOvertimeRecord(DateTime sD, DateTime eD) {
        return repo.findOvertimeRecord(sD.toDate(), eD.toDate());
    }

    public List<OvertimeRecordWeekly> findWeeklyOvertimeRecord(DateTime sD, DateTime eD) {
        return repo.findWeeklyOvertimeRecord(sD.toDate(), eD.toDate());
    }

}
