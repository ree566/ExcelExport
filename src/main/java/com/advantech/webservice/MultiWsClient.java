/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import static com.google.common.base.Preconditions.checkState;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.tempuri.ObjectFactory;
import org.tempuri.Rv;
import org.tempuri.RvResponse;
import org.tempuri.Tx;
import org.tempuri.TxResponse;

/**
 *
 * @author Wei.Cheng
 */
public class MultiWsClient extends WebServiceGatewaySupport {

    private WebServiceTemplate webServiceTemplate = null;

    private WebServiceTemplate webServiceTemplate1 = null;

    private WebServiceTemplate webServiceTemplate2 = null;

    public MultiWsClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public MultiWsClient(WebServiceTemplate webServiceTemplate, WebServiceTemplate webServiceTemplate1) {
        this.webServiceTemplate = webServiceTemplate;
        this.webServiceTemplate1 = webServiceTemplate1;
    }

    public MultiWsClient(WebServiceTemplate webServiceTemplate, WebServiceTemplate webServiceTemplate1, WebServiceTemplate webServiceTemplate2) {
        this.webServiceTemplate = webServiceTemplate;
        this.webServiceTemplate1 = webServiceTemplate1;
        this.webServiceTemplate2 = webServiceTemplate2;
    }

    public TxResponse simpleTxSendAndReceive(String v, UploadType type, final Factory f) throws IOException {
        ObjectFactory factory = new ObjectFactory();
        Tx tx = factory.createTx();
        tx.setSParam(v);
        tx.setSType(type.toString());
        return (TxResponse) marshalSendAndReceive(tx, f);
    }

    public RvResponse simpleRvSendAndReceive(String v, final Factory f) {
        ObjectFactory factory = new ObjectFactory();
        Rv rv = factory.createRv();
        rv.setSParam(v);
        return (RvResponse) marshalSendAndReceive(rv, f);
    }

    private Object marshalSendAndReceive(Object request, final Factory f) {
        switch (f) {
            case TWM3:
                checkState(webServiceTemplate != null, "Default webService template is not inject");
                return webServiceTemplate.marshalSendAndReceive(request);
            case TWM6:
                checkState(webServiceTemplate1 != null, "WebService template1 is not inject");
                return webServiceTemplate1.marshalSendAndReceive(request);
            case TWM2:
                checkState(webServiceTemplate2 != null, "WebService template2 is not inject");
                return webServiceTemplate2.marshalSendAndReceive(request);
            default:
                throw new UnsupportedOperationException();
        }
    }

}
