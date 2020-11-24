/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db2;

import com.advantech.model.db1.User;
import com.advantech.model.db2.Items;
import com.advantech.model.db2.Orders;
import com.advantech.model.db2.QryWipAtt;
import com.advantech.model.db2.Users;
import com.advantech.repo.db2.ItemsRepository;
import com.advantech.repo.db2.OrdersRepository;
import com.advantech.repo.db2.UsersRepository;
import com.advantech.webservice.Factory;
import com.advantech.webservice.port.QryWipAttQueryPort;
import static com.google.common.base.Preconditions.checkState;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional("tx2")
public class OrdersService {

    @Autowired
    private OrdersRepository repo;

    @Autowired
    private ItemsRepository itemRepo;

    @Autowired
    private UsersRepository usersRepo;

    @Autowired
    private QryWipAttQueryPort modelNameQueryPort;

    public List<Orders> findAll() {
        return repo.findAll();
    }

    public Optional<Orders> findById(Integer id) {
        return repo.findById(id);
    }

    public <S extends Orders> S save(S s, Items i) throws Exception {
        //Retrive modelName from MES port(M3, M2, M6)
        String po = i.getLabel1();
        List<QryWipAtt> l = modelNameQueryPort.query(po, Factory.DEFAULT);
        if (l.isEmpty()) {
            l = modelNameQueryPort.query(po, Factory.TEMP1);
        }
        if (l.isEmpty()) {
            l = modelNameQueryPort.query(po, Factory.TEMP2);
        }
        checkState(!l.isEmpty(), "Can't find modelName in current po");
        i.setLabel2(l.get(0).getModelName());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userInSession = (User) auth.getPrincipal();

        //Auto change user's identity when ADMIN is testing data.
        Users remoteUser = usersRepo.findById(userInSession.getJobnumber()).orElse(null);
        if (remoteUser == null && auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            remoteUser = usersRepo.getOne("test2");
        }

        checkState(remoteUser != null, "Remote user's account not found");
        s.setUsers(remoteUser);
        s.setTeams(remoteUser.getTeams());

        repo.save(s);
        i.setOrders(s);
        itemRepo.save(i);

        repo.updateTimeStampToZero(s.getId());
        return null;
    }

}
