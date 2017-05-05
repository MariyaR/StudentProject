package edu.javacourse.third.checkers;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import edu.javacourse.third.answer.CheckerAnswer;
import edu.javacourse.third.checkers.answer.BasicCheckerAnswer;
import edu.javacourse.third.domain.*;
import edu.javacourse.third.exception.SendGetDataException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by antonsaburov on 02.03.17.
 */
public class ZagsChecker extends BasicChecker implements Callable<CheckerAnswer>
{
    private static Map<String, String> settings = new HashMap<>();

    static {
        PropertyResourceBundle pr = (PropertyResourceBundle)
                PropertyResourceBundle.getBundle("zags_checker");
        settings.put("host", pr.getString("zags.host"));
        settings.put("port", pr.getString("zags.port"));
        settings.put("login", pr.getString("zags.login"));
        settings.put("password", pr.getString("zags.password"));
    }

    private PersonAdult husband;
    private PersonAdult wife;
    private PersonChild child;
    private int flag;
    private StringWriter sw;

    public ZagsChecker(PersonAdult h, PersonAdult w, StringWriter sw) {
        super(settings.get("host"), Integer.parseInt(settings.get("port")),
                settings.get("login"), settings.get("password"));
        this.husband=h;
        this.wife=w;
        this.flag=0;
        this.sw=sw;
    }

    public ZagsChecker(PersonAdult h, PersonAdult w, PersonChild ch, StringWriter sw) {
        super(settings.get("host"), Integer.parseInt(settings.get("port")),
                settings.get("login"), settings.get("password"));
        this.husband=h;
        this.wife=w;
        this.child=ch;
        this.flag=1;
        this.sw=sw;
    }

    public void setParameters (PersonAdult h, PersonAdult w, PersonChild ch) {
        this.husband=h;
        this.wife=w;
        this.child=ch;
        flag=1;
    }

    public void setParameters (PersonAdult h, PersonAdult w) {
        this.husband=h;
        this.wife=w;
        flag=0;
    }

    @Override
    public CheckerAnswer call()throws Exception {
        CheckerAnswer c = check();
        return c;
    }

    @Override
    protected CheckerAnswer sendAndGetData() throws SendGetDataException {
        try  {
            StringBuilder ans = new StringBuilder();
            CheckerAnswer answer = new BasicCheckerAnswer(false,"default");
            Reader br = new InputStreamReader(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            if (flag==0){
                answer= checkWedding(os, br);
            }
            else {
                answer=checkChild(os,br);
            }
           return answer;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SendGetDataException(e.getMessage());
        }
    }

  private String buildFamily (PersonAdult h, PersonAdult w, PersonChild ch)
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
        return answer;
  }

  private String buildFamily (PersonAdult h, PersonAdult w)
             throws UnsupportedEncodingException, XMLStreamException {

         Pair p= new Pair();
         p.setParameters(h,w);
         String answer = new String();
        try {
            JAXBContext context = JAXBContext.newInstance(Pair.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            m.marshal(p, sw);
            answer = sw.toString();
       }
       catch (Exception e) {e.printStackTrace();}
       return answer;
  }

  private CheckerAnswer checkWedding(OutputStream os, Reader br) {
        StringBuilder SendString= new StringBuilder();
        StringBuilder GetString = new StringBuilder();
        try {
            SendString.append(buildFamily(husband, wife));
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
        sw.write("Wedding result for <"+husband.getGivenName()+" "+husband.getSurName()
                            + " and "+wife.getGivenName()+" "+wife.getSurName()+">:"+"\n");
        sw.write(getAnswer(GetString.toString()).getMessage()+"\n\n");
       // System.out.println();
        return getAnswer(GetString.toString());
  }
  private CheckerAnswer checkChild(OutputStream os, Reader br) {
        StringBuilder SendString= new StringBuilder();
        StringBuilder GetString = new StringBuilder();
        try {
            SendString.append(buildFamily(husband, wife, child));
            os.write(SendString.toString().getBytes());
            os.flush();
            char[] request = new char[6];
            int count = br.read(request);
            while(count != -1) {
                 GetString.append(new String(request, 0, count)); //формируем строку ответа
                 if (GetString.toString().endsWith("OK")) {
                     break;
                 }
                 count = br.read(request);
            }
        }
        catch (Exception e) {e.printStackTrace();}
        sw.write("Family result for <"+husband.getGivenName()+" "+husband.getSurName()
              + " and "+wife.getGivenName()+" "+wife.getSurName()
              +" and "+child.getGivenName()+" "+child.getSurName()+">:"+"\n");
        sw.write(getAnswer(GetString.toString()).getMessage()+"\n\n");
        //System.out.println(sw);
        return getAnswer(GetString.toString());
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
