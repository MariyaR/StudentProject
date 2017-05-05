package edu.javacourse.zags;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.Data;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
/**
 * Created by maria on 05/05/17.
 */
public class RequestHandler implements Runnable {

    private Socket socket;

    public RequestHandler(Socket s) {this.socket=s;}

    @Override
    public void run() {
        try {
            handleRequest();
        }catch (Exception e) {e.printStackTrace();}
    }

    private void handleRequest() throws IOException{

                StringBuilder sb= getData(socket);  //получаем данные о персоне в виде строки
                Family f = new Family();
                BasicCheckerAnswer b= new BasicCheckerAnswer();
                f= loadFamily(sb.toString());            //парсим семью
                if (f.getType().equals("pair")) {
                    b= checkWedding(f);
                }
                else if (f.getType().equals("family")) {
                    b= checkChild(f);
                }
                else {
                    System.out.println("type error");
                }
                String Answer= new String();
                try {
                    Answer = buildXmlAnswer(b); //формируем строку-ответ хмл
                } catch (Exception e) {e.printStackTrace();}
                try {
                    OutputStream os = socket.getOutputStream();  //отправляем ответ грн чекеру
                    os.write(Answer.getBytes());
                    os.flush();
                    System.out.println("        Data from client:");
                    System.out.println(sb.toString());
                    System.out.println();
                    System.out.println("        Check result:");
                    System.out.println(Answer);
                    System.out.println("the request is handled");
                    System.out.println("--------------------------------------------");
                    socket.close();
                }catch (Exception e) {e.printStackTrace();}
    }

    private static StringBuilder getData(Socket s) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader br = new InputStreamReader(s.getInputStream());
            char[] request = new char[6];
            int count = br.read(request);
            while (count != -1){
                sb.append(new String(request, 0, count));
                if (sb.toString().contains("</family>")) {
                    break;
                }
                count = br.read(request);
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return sb;
    }

    private Family loadFamily(String s) {         // парсим  персону
        try
        {
            JAXBContext jf = JAXBContext.newInstance(Family.class);
            Unmarshaller uf = jf.createUnmarshaller();
            StringBuffer xmlStr = new StringBuffer(s);
            Family f = new Family();
            Object of= new Object();
            try {
                of = uf.unmarshal(new StreamSource( new StringReader( xmlStr.toString() ) ) );
                f= (Family) of;
                return f;
            }
            catch (UnmarshalException e) {System.out.println("person unmarshalling error");}
        } catch (Exception ex) {ex.printStackTrace();}
        return null;
    }

    public static BasicCheckerAnswer checkWedding (Family f) { //формируем сообщение-ответ
        BasicCheckerAnswer ans = new BasicCheckerAnswer(true, "wedding check ok");
        return ans;
    }

    public static BasicCheckerAnswer checkChild (Family f) { //формируем сообщение-ответ
        BasicCheckerAnswer ans = new BasicCheckerAnswer(true, "family check ok");
        return ans;
    }

    private static String buildXmlAnswer(BasicCheckerAnswer b) throws UnsupportedEncodingException, XMLStreamException {
        ByteOutputStream bos = new ByteOutputStream();
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        XMLStreamWriter xml = factory.createXMLStreamWriter(bos);
        xml.writeStartDocument();
        xml.writeStartElement("answer");
        xml.writeCharacters ((new Boolean(b.getResult())).toString());
        xml.writeEndElement();

        xml.writeStartElement("message");
        xml.writeCharacters(b.getMessage());
        xml.writeEndElement();
        xml.writeEndDocument();
        String answer = new String(bos.getBytes(), 0, bos.getCount(), "utf-8");
        bos.close();
        return answer;
    }
}



