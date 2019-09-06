/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Achieving;
import com.advantech.repo.db1.AchievingRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class AchievingService {

    @Autowired
    private AchievingRepository repo;

    public List<Achieving> findAll() {
        return repo.findAll();
    }

    public DataTablesOutput<Achieving> findAll(DataTablesInput dti) {
        return repo.findAll(dti);
    }

    public <S extends Achieving> S save(S s) {
        return repo.save(s);
    }

    public void delete(Achieving t) {
        repo.delete(t);
    }

}
