package io.quarkiverse.it.cxf.provider;

import org.apache.cxf.staxutils.StaxUtils;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import java.io.StringReader;

@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class SOAPMessageProvider implements Provider<SOAPMessage> {

    public SOAPMessageProvider() {
    }

    public SOAPMessage invoke(SOAPMessage request) throws WebServiceException {
        try {
            String payload = StaxUtils.toString(request.getSOAPBody().extractContentAsDocument());
            payload = payload.replace("<text>Hello</text>", "<text>Hello from SOAPMessageProvider</text>");

            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage response = mf.createMessage();
            response.getSOAPBody().addDocument(StaxUtils.read(new StreamSource(new StringReader(payload))));
            response.saveChanges();
            return response;

        } catch (SOAPException | XMLStreamException e) {
            throw new WebServiceException(e);
        }
    }
}