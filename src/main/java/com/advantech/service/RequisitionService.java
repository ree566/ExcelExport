/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.Requisition;
import com.advantech.model.RequisitionEvent;
import com.advantech.model.RequisitionState;
import com.advantech.model.User;
import com.advantech.repo.db1.RequisitionEventRepository;
import com.advantech.repo.db1.RequisitionRepository;
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
public class RequisitionService {

    @Autowired
    private RequisitionRepository repo;

    @Autowired
    private RequisitionEventRepository eventRepo;

    @Autowired
    private RequisitionStateRepository stateRepo;

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

    public Optional<Requisition> findById(Integer id) {
        return repo.findById(id);
    }

    public <S extends Requisition> S save(S s) {
        RequisitionState stat = stateRepo.findById(3).get();
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();

        s.setRequisitionState(stat);
        s.setUser(user);
        S result = repo.save(s);

        RequisitionEvent e = new RequisitionEvent(s, user, stat, "");
        eventRepo.save(e);

        return result;
    }

    public void changeState(int requisition_id, int state_id) {
        Requisition r = this.findById(requisition_id).get();
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        RequisitionState state = stateRepo.getOne(state_id);
        r.setRequisitionState(state);
        RequisitionEvent e = new RequisitionEvent(r, user, state, "");
        
        repo.save(r);
        eventRepo.save(e);
    }

    public void delete(Requisition t) {
        repo.delete(t);
    }

    public void deleteAll(Iterable<? extends Requisition> itrbl) {
        repo.deleteAll(itrbl);
    }

}
