/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.db1.Requisition;
import com.advantech.model.db1.RequisitionState;
import com.advantech.repo.db1.RequisitionStateRepository;
import java.util.List;
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
@Transactional("tx1")
public class RequisitionStateService {

    @Autowired
    private RequisitionStateRepository repo;

    public List<RequisitionState> findAll() {
        return repo.findAll();
    }

    public DataTablesOutput<RequisitionState> findAll(DataTablesInput dti) {
        return repo.findAll(dti);
    }

    public List<RequisitionState> findAll(Specification<RequisitionState> s) {
        return repo.findAll(s);
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
