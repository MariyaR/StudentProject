package edu.javacourse.student;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by maria on 04/05/17.
 */
public class StudentSystem {

    public static void main(String[] args) {
        StudentSystem st = new StudentSystem();
        st.start();
    }

    private void start() {
        System.out.println("StudentSystem started");
        System.out.println("------------------------------------------------------------------");
        System.out.println();
            try {
                ServerSocket ses = new ServerSocket(7999);
                while(true) {
                Socket socket = ses.accept();
                RequestHandler rh = new RequestHandler(socket);
                new Thread(rh).start();
            }
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
