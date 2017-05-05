package edu.javacourse.third.checkers;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import edu.javacourse.third.answer.CheckerAnswer;
import edu.javacourse.third.checkers.answer.BasicCheckerAnswer;
import edu.javacourse.third.domain.Person;
import edu.javacourse.third.exception.SendGetDataException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by antonsaburov on 27.02.17.
 */
public class GrnChecker extends BasicChecker
{
    private Person person;

    public GrnChecker(String host, int port, String login, String password) {
        super(host, port, login, password);
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    protected CheckerAnswer sendAndGetData() throws SendGetDataException {
        try {
            OutputStream os = socket.getOutputStream();
            StringBuilder sb = new StringBuilder(buildXmlForPerson()); //получаем хмл для персоны
            os.write(sb.toString().getBytes()); //отправляем данные в грн систему
            os.flush();

            StringBuilder ans = new StringBuilder();   //считываем ответ в строку ans
            Reader br = new InputStreamReader(socket.getInputStream());
            char[] request = new char[6];
            int count = br.read(request);
            while(count != -1) {
                ans.append(new String(request, 0, count)); //формируем строку ответа
                if(ans.toString().endsWith("OK")) {
                    break;
                }
                count = br.read(request);
            }
            return getAnswer(ans.toString());

        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            throw new SendGetDataException(e.getMessage());
        }
    }

    private CheckerAnswer getAnswer (String s) {

        String s1= "<answer>";
        String s2= "</answer>";
        String s3= "<message>";
        String s4= "</message>";
        int begin= s.indexOf(s3)+9;
        int end = s.indexOf(s4);
        String Message =s.substring(begin, end);
        begin= s.indexOf(s1)+8;
        end=s.indexOf(s2);
        String Answer=s.substring(begin, end);
        CheckerAnswer B = new BasicCheckerAnswer(Boolean.parseBoolean(Answer), Message);
        return B;

    }

    private String buildXmlForPerson() throws UnsupportedEncodingException, XMLStreamException {
        ByteOutputStream bos = new ByteOutputStream();

        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        XMLStreamWriter xml = factory.createXMLStreamWriter(bos);

        xml.writeStartDocument();
        xml.writeStartElement("person");   //здесь мы создаем хмл файл для взрослой персоны
        xml.writeStartElement("surName");  // и возвращаем его в виде строки
        xml.writeCharacters(person.getSurName());
        xml.writeEndElement();
        xml.writeStartElement("givenName");
        xml.writeCharacters(person.getGivenName());
        xml.writeEndElement();
        xml.writeStartElement("patronymic");
        xml.writeCharacters(person.getPatronymic());
        xml.writeEndElement();
        xml.writeStartElement("dateOfBirth");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        xml.writeCharacters(sdf.format(new Date()));
        xml.writeEndElement();

        xml.writeEndElement();
        xml.writeEndDocument();

        String answer = new String(bos.getBytes(), 0, bos.getCount(), "utf-8");
        bos.close();
        //System.out.println(answer);
        return answer;
    }
}
