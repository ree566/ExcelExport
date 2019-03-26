/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Requisition;
import com.advantech.model.RequisitionState;
import com.advantech.repo.db1.RequisitionStateRepository;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class RequisitionStateService {

    @Autowired
    private RequisitionStateRepository repo;

    public DataTablesOutput<RequisitionState> findAll(DataTablesInput dti) {
        return repo.findAll(dti);
    }

    public Optional<RequisitionState> findById(Integer id) {
        return repo.findById(id);
    }

    public <S extends RequisitionState> S save(S s) {
        return repo.save(s);
    }

    public void delete(RequisitionState t) {
        repo.delete(t);
    }

    public void deleteAll(Iterable<? extends RequisitionState> itrbl) {
        repo.deleteAll(itrbl);
    }

}
