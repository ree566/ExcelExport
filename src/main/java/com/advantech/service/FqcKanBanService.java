/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.db1.FqcKanBan;
import com.advantech.repo.db1.FqcKanBanRepository;
import java.util.List;
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
public class FqcKanBanService {

    @Autowired
    private FqcKanBanRepository repo;

    public List<FqcKanBan> findAll() {
        return repo.findAll();
    }

    public FqcKanBan getOne(Integer id) {
        return repo.getOne(id);
    }

    public DataTablesOutput<FqcKanBan> findAll(DataTablesInput dti) {
        return repo.findAll(dti);
    }

    public DataTablesOutput<FqcKanBan> findAll(DataTablesInput dti, Specification<FqcKanBan> s) {
        return repo.findAll(dti, s);
    }

    public <S extends FqcKanBan> S save(S s) {
        return repo.save(s);
    }

    public <S extends FqcKanBan> List<S> saveAll(Iterable<S> itrbl) {
        return repo.saveAll(itrbl);
    }

    public void delete(FqcKanBan t) {
        repo.delete(t);
    }

    public void deleteInBatch(Iterable<FqcKanBan> itrbl) {
        repo.deleteInBatch(itrbl);
    }
}
