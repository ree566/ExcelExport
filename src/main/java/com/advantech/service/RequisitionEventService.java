/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.db1.Requisition;
import com.advantech.model.db1.RequisitionEvent;
import com.advantech.repo.db1.RequisitionEventRepository;
import java.util.List;
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
@Transactional("tx1")
public class RequisitionEventService {

    @Autowired
    private RequisitionEventRepository repo;

    public DataTablesOutput<RequisitionEvent> findAll(DataTablesInput dti) {
        return repo.findAll(dti);
    }

    public DataTablesOutput<RequisitionEvent> findAll(DataTablesInput dti, Specification<RequisitionEvent> s) {
        return repo.findAll(dti, s);
    }

    public DataTablesOutput<RequisitionEvent> findAll(DataTablesInput dti, Specification<RequisitionEvent> s, Specification<RequisitionEvent> s1) {
        return repo.findAll(dti, s, s1);
    }

    public <R> DataTablesOutput<R> findAll(DataTablesInput dti, Function<RequisitionEvent, R> fnctn) {
        return repo.findAll(dti, fnctn);
    }

    public <R> DataTablesOutput<R> findAll(DataTablesInput dti, Specification<RequisitionEvent> s, Specification<RequisitionEvent> s1, Function<RequisitionEvent, R> fnctn) {
        return repo.findAll(dti, s, s1, fnctn);
    }

    public <S extends RequisitionEvent> S save(S s) {
        return repo.save(s);
    }

    public void delete(RequisitionEvent t) {
        repo.delete(t);
    }

    public void deleteAll(Iterable<? extends RequisitionEvent> itrbl) {
        repo.deleteAll(itrbl);
    }

}
