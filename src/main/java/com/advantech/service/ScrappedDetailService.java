/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Floor;
import com.advantech.model.ScrappedDetail;
import com.advantech.repo.ScrappedDetailRepository;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class ScrappedDetailService {

    @Autowired
    private ScrappedDetailRepository repo;

    public List<ScrappedDetail> findByFloor(Floor floor) {
        return repo.findByFloor(floor);
    }

    public List<ScrappedDetail> findAll() {
        return repo.findAll();
    }

    public <S extends ScrappedDetail> List<S> saveAll(Iterable<S> itrbl) {
        return repo.saveAll(itrbl);
    }

    public <S extends ScrappedDetail> S save(S s) {
        return repo.save(s);
    }

    public Optional<ScrappedDetail> findById(Integer id) {
        return repo.findById(id);
    }

    public ScrappedDetail getOne(Integer id) {
        return repo.getOne(id);
    }

    public void delete(ScrappedDetail t) {
        repo.delete(t);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

}
