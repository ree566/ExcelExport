/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo.db2;

import com.advantech.model.db2.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    @Modifying(clearAutomatically = true)
    @Query(value = "update orders set orders.time_close = 0 where orders.id =:entryId",
           nativeQuery = true)
    public void updateTimeStampToZero(@Param("entryId") Integer entryId);
}
