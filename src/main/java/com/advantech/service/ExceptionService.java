/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.ScrappedDetail;
import com.advantech.model.User;
import com.advantech.repo.db1.ScrappedDetailRepository;
import com.advantech.repo.db1.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class ExceptionService {

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private ScrappedDetailRepository sRepo;

    public void testTransaction() {
        User u = uRepo.findById(1).get();
        ScrappedDetail d = sRepo.findById(1).get();

        u.setEmail(u.getEmail() + "1");
        uRepo.save(u);

        t();
//        int i = 1 / 0;

        d.setPrice(999);
        sRepo.save(d);
    }

    private void t() {
        throw new RuntimeException("Rollback!!");
    }

    private void t2() throws Exception {
        throw new Exception("Rollback!!");
    }
}
