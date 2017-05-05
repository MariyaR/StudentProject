package edu.javacourse.third.checkers;

import edu.javacourse.third.answer.CheckerAnswer;
import edu.javacourse.third.checkers.answer.BasicCheckerAnswer;
import edu.javacourse.third.domain.Pair;
import edu.javacourse.third.domain.Person;
import edu.javacourse.third.domain.PersonAdult;
import edu.javacourse.third.exception.SendGetDataException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by antonsaburov on 02.03.17.
 */
public class StudentChecker extends BasicChecker implements Callable<CheckerAnswer>
{
    private static Map<String, String> settings = new HashMap<>();

    static {
        PropertyResourceBundle pr = (PropertyResourceBundle)
                PropertyResourceBundle.getBundle("student_checker");
        settings.put("host", pr.getString("stud.host"));
        settings.put("port", pr.getString("stud.port"));
        settings.put("login", pr.getString("stud.login"));
        settings.put("password", pr.getString("stud.password"));
    }

    private PersonAdult person;
    private StringWriter sw;

    public StudentChecker(PersonAdult person, StringWriter sw) {
        super(settings.get("host"), Integer.parseInt(settings.get("port")),
                settings.get("login"), settings.get("password"));
        this.person = person;
        this.sw=sw;
    }

    public void setPerson(PersonAdult person) {this.person = person;}

    @Override
    public CheckerAnswer call() throws Exception {
        CheckerAnswer c = check();
        return c;
    }

    protected CheckerAnswer sendAndGetData() throws SendGetDataException {
        try  {
            StringBuilder ans = new StringBuilder();
            CheckerAnswer answer = new BasicCheckerAnswer(false,"default");
            Reader br = new InputStreamReader(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            answer= checkStudent(os, br);
            System.out.println();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SendGetDataException(e.getMessage());
        }
    }

    private CheckerAnswer checkStudent(OutputStream os, Reader br) {
        StringBuilder SendString= new StringBuilder();
        StringBuilder GetString = new StringBuilder();
        try {
            SendString.append(buildPerson(person));
            os.write(SendString.toString().getBytes());
            os.flush();
            char[] request = new char[6];
            int count= br.read(request);
            while(count != -1) {
                GetString.append(new String(request, 0, count)); //формируем строку ответа
                if (GetString.toString().endsWith("OK")) {
                    break;
                }
                count = br.read(request);
            }
        }
        catch (Exception e) {e.printStackTrace();}
        sw.write("Student status for <"+person.getGivenName()+" "+person.getSurName()+">:"+"\n");
        sw.write(getAnswer(GetString.toString()).getMessage()+"\n\n");
        return getAnswer(GetString.toString());
    }

    private String buildPerson (PersonAdult p)
            throws UnsupportedEncodingException, XMLStreamException {

        String answer = new String();
        try {
            JAXBContext context = JAXBContext.newInstance(PersonAdult.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            m.marshal(p, sw);
            answer = sw.toString();
        }
        catch (Exception e) {e.printStackTrace();}
        return answer;
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
}

