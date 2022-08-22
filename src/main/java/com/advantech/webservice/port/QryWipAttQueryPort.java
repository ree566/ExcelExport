/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.db2.QryWipAtt;
import com.advantech.webservice.Factory;
import com.advantech.webservice.root.QryWipAttQueryRoot;
import com.advantech.webservice.unmarshallclass.QryWipAtts;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class QryWipAttQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(QryWipAttQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(QryWipAttQueryRoot.class, QryWipAtts.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    public List<QryWipAtt> query(String po, Factory f) throws Exception {
        QryWipAttQueryRoot root = new QryWipAttQueryRoot();
        root.getWIPATT().setWIPNO(po);
        List<QryWipAtt> l = this.query(root, f);
        return l;
    }

}
