/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.UserProfile;
import com.advantech.repo.db1.UserProfileRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class UserProfileService {

    @Autowired
    private UserProfileRepository repo;

    public List<UserProfile> findAll() {
        return repo.findAll();
    }

    public Page<UserProfile> findAll(Pageable pgbl) {
        return repo.findAll(pgbl);
    }

    public <S extends UserProfile> S save(S s) {
        return repo.save(s);
    }

    public Optional<UserProfile> findById(Integer id) {
        return repo.findById(id);
    }

    public void delete(UserProfile t) {
        repo.delete(t);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

}
