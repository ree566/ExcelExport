/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo.db1;

import com.advantech.model.Floor;
import com.advantech.model.OvertimeRecord;
import com.advantech.model.OvertimeRecordWeekly;
import com.advantech.model.OvertimeRecordWeeklyChart;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng Query sql view by nativeQuery because target tables doesn't
 * have PK field.
 */
@Repository
public interface OvertimeRecordRepository extends JpaRepository<Floor, Integer> {

    @Query(value = "{CALL usp_OvertimeRecord_rank(:sD, :eD)}",
            nativeQuery = true)
    public List<OvertimeRecord> findOvertimeRecord(@Param("sD") Date sD, @Param("eD") Date eD);

    @Query(value = "{CALL usp_OvertimeRecord_Weekly(:sD, :eD)}",
            nativeQuery = true)
    public List<OvertimeRecordWeekly> findWeeklyOvertimeRecord(@Param("sD") Date sD, @Param("eD") Date eD);
    
    @Query(value = "{CALL usp_OvertimeRecord_Weekly_Chart(:sD, :eD)}",
            nativeQuery = true)
    public List<OvertimeRecordWeeklyChart> findWeeklyOvertimeChart(@Param("sD") Date sD, @Param("eD") Date eD);

}
