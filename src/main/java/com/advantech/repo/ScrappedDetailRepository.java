/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.Floor;
import com.advantech.model.ScrappedDetail;
import com.advantech.model.ScrappedDetailWeekGroup;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface ScrappedDetailRepository extends JpaRepository<ScrappedDetail, Integer> {

    public List<ScrappedDetail> findByFloor(Floor floor);

    @Query(
            value = "SELECT * FROM vw_ScrappedDetailWeekGroup order by 1, 2",
            nativeQuery = true)
    public List<ScrappedDetailWeekGroup> findAllGroupByWeek();

}
