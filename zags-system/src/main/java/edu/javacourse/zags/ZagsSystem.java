package edu.javacourse.zags;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.*;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by maria on 11.04.2017.
 */
public class ZagsSystem
{
    public static void main(String[] args) {
        ZagsSystem zs = new ZagsSystem();
        zs.start();
    }

    private void start() {
        System.out.println("ZagsSystem started");
        System.out.println("------------------------------------------------------------------");
        System.out.println();
        try {
            ServerSocket ses = new ServerSocket(7888);
            while (true){
                Socket s = ses.accept();
                RequestHandler rh= new RequestHandler(s);
                new Thread(rh).start();
            }

        }catch (Exception e) {e.printStackTrace();}
    }

}
