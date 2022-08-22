/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.tempuri.ObjectFactory;
import org.tempuri.Rv;
import org.tempuri.RvResponse;
import org.tempuri.Tx;
import org.tempuri.TxResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class WsClient extends WebServiceGatewaySupport {

    @Autowired
    @Qualifier("webServiceTemplate")
    private WebServiceTemplate webServiceTemplate;
    
    @Autowired
    private ObjectFactory factory;

    public TxResponse simpleTxSendAndReceive(String v, UploadType type) throws IOException {
        Tx tx = factory.createTx();
        tx.setSParam(v);
        tx.setSType(type.toString());
        TxResponse response = (TxResponse) webServiceTemplate.marshalSendAndReceive(tx);
        return response;
    }

    public RvResponse simpleRvSendAndReceive(String v) {
        Rv rv = factory.createRv();
        rv.setSParam(v);
        RvResponse response = (RvResponse) webServiceTemplate.marshalSendAndReceive(rv);
        return response;
    }

    public List<String> getFormatWebServiceData(String queryString) throws Exception {
        RvResponse response = simpleRvSendAndReceive(queryString);
        return formatResponse(response);
    }

    public List formatResponse(RvResponse response) throws Exception {
        List list = new ArrayList();
        List data = response.getRvResult().getAny();
        for (Object obj : data) {
            Document doc = ((Node) obj).getOwnerDocument();
            try (StringWriter sw = new StringWriter()) {
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.transform(new DOMSource(doc), new StreamResult(sw));
                list.add(sw.toString());
            }
        }

        return list;
    }

}
