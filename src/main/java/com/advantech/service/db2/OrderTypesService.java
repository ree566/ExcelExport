/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db2;

import com.advantech.model.db2.OrderTypes;
import com.advantech.repo.db2.OrderTypesRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional("tx2")
public class OrderTypesService {

    @Autowired
    private OrderTypesRepository repo;

    public List<OrderTypes> findAll() {
        return repo.findAll();
    }

    public Optional<OrderTypes> findById(Integer id) {
        return repo.findById(id);
    }

}
