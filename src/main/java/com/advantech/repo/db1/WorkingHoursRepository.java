/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo.db1;

import com.advantech.model.Floor;
import com.advantech.model.WorkingHoursReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng Query sql view by nativeQuery because target tables doesn't
 * have PK field.
 */
@Repository
public interface WorkingHoursRepository extends JpaRepository<Floor, Integer> {

    @Query(value = "SELECT * FROM vw_Daily_WH_Report order by 1, 5",
            nativeQuery = true)
    public List findDailyWhReport();

    @Query(value = "SELECT * FROM vw_Weekly_WH_Report order by 1, 5",
            nativeQuery = true)
    public List findWeeklyWhReport();

    @Query(value = "SELECT * FROM vw_Monthly_WH_Report",
            nativeQuery = true)
    public List<WorkingHoursReport> findMonthlyWhReport();

}
