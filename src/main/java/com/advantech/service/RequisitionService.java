/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Requisition;
import com.advantech.repo.db1.RequisitionRepository;
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
public class RequisitionService {

    @Autowired
    private RequisitionRepository repo;

    public DataTablesOutput<Requisition> findAll(DataTablesInput dti) {
        return repo.findAll(dti);
    }

    public DataTablesOutput<Requisition> findAll(DataTablesInput dti, Specification<Requisition> s) {
        return repo.findAll(dti, s);
    }

    public DataTablesOutput<Requisition> findAll(DataTablesInput dti, Specification<Requisition> s, Specification<Requisition> s1) {
        return repo.findAll(dti, s, s1);
    }

    public <R> DataTablesOutput<R> findAll(DataTablesInput dti, Function<Requisition, R> fnctn) {
        return repo.findAll(dti, fnctn);
    }

    public <R> DataTablesOutput<R> findAll(DataTablesInput dti, Specification<Requisition> s, Specification<Requisition> s1, Function<Requisition, R> fnctn) {
        return repo.findAll(dti, s, s1, fnctn);
    }

    public <S extends Requisition> S save(S s) {
        return repo.save(s);
    }

    public void delete(Requisition t) {
        repo.delete(t);
    }

    public void deleteAll(Iterable<? extends Requisition> itrbl) {
        repo.deleteAll(itrbl);
    }

}
