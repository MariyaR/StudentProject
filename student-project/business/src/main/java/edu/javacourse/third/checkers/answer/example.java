package edu.javacourse.third.checkers.answer;

import edu.javacourse.third.domain.Family;
import edu.javacourse.third.domain.PersonAdult;
import edu.javacourse.third.domain.PersonChild;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by maria on 12/04/17.
 */
public class example {

    private PersonAdult husband;
    private PersonAdult wife;
    private PersonChild child;

    public void setParameters (PersonAdult h, PersonAdult w, PersonChild ch) {
        this.husband = h;
        this.wife = w;
        this.child = ch;

    }

    public String buildFamily (PersonAdult h, PersonAdult w, PersonChild ch)
            throws UnsupportedEncodingException, XMLStreamException {

        Family f= new Family();
        f.setParameters(h,w,ch);

        String answer = new String();
        try {
            JAXBContext context = JAXBContext.newInstance(Family.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            m.marshal(f, sw);
            answer=sw.toString();
        }
        catch (Exception e) {e.printStackTrace();}
        System.out.println("построили family: " + answer);
        return answer;
    }
}
