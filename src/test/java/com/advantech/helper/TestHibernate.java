/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.ScrappedDetail;
import com.advantech.repo.db1.ScrappedDetailRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestHibernate {

    @Autowired
    private ScrappedDetailRepository repo;

    @Autowired
    @Qualifier("entityManagerFactory")
    private EntityManagerFactory factory;

    @Autowired
    @Qualifier("entityManagerFactory2")
    private EntityManagerFactory factory2;

//    @Test
    @Transactional
    @Rollback(true)
    public void testPojo() {
        ScrappedDetail o = repo.findById(1).get();
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    public void testPojo2() {
        EntityManager manager = factory.createEntityManager();
        ScrappedDetail o = manager.find(ScrappedDetail.class, 1);
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }
    
    @Test
    @Transactional("transactionManager2")
    @Rollback(true)
    public void testFactory2Pojo() {
        EntityManager manager = factory2.createEntityManager();
        List l = manager.createNativeQuery("select * from T_OutputValueSummaryDaily").setMaxResults(1).getResultList();
        HibernateObjectPrinter.print(l);
        
    }

}
