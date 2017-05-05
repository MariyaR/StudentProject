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
 * Created by maria on 05/05/17.
 */
public class RequestHandler implements Runnable {

    private Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            handleRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest() throws IOException {
        StringBuilder sb= getData(socket);  //получаем данные о персоне в виде строки
        PersonAdult p= loadPerson(sb.toString());            //парсим семью
        BasicCheckerAnswer b= checkStudent(p);
        String Answer= new String();
        try {
            Answer = buildXmlAnswer(b); //формируем строку-ответ хмл
            System.out.println("        Data from client:");
            System.out.println(sb);
            System.out.println();
            System.out.println("        Result for <"+p.getGivenName()+" "+p.getSurName()+">:");
            System.out.println(Answer);
            System.out.println("the request is handled");
            System.out.println("------------------------------------------------------------------");
            System.out.println();
        }
        catch (XMLStreamException e) {e.printStackTrace();}
        try {
            OutputStream os = socket.getOutputStream();  //отправляем ответ
            os.write(Answer.getBytes());
            os.flush();
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
                if (sb.toString().contains("</Person>")) {
                    break;
                }
                count = br.read(request);
            }
        }
        catch (IOException e) {e.printStackTrace();}
       // System.out.println("Data from client:");
       // System.out.println(sb);
       // System.out.println();
        return sb;
    }
    private PersonAdult loadPerson(String s) {         // парсим  персону
        try {
            JAXBContext jf = JAXBContext.newInstance(PersonAdult.class);
            Unmarshaller uf = jf.createUnmarshaller();
            StringBuffer xmlStr = new StringBuffer(s);
            PersonAdult p = new PersonAdult();
            Object o=new Object();
            try {
                o = uf.unmarshal(new StreamSource( new StringReader( xmlStr.toString() ) ) );
                p= (PersonAdult) o;
                return p;
            }
            catch (UnmarshalException e) {e.printStackTrace();}
        } catch (Exception ex) {ex.printStackTrace();}
        return null;
    }

    public static BasicCheckerAnswer checkStudent (PersonAdult p) { //формируем сообщение-ответ
        BasicCheckerAnswer ans = new BasicCheckerAnswer(true, "student check ok");
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
