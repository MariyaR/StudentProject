package edu.javacourse.third;

import edu.javacourse.third.answer.CheckerAnswer;
import edu.javacourse.third.domain.StudentOrder;

import java.io.*;
import java.util.List;

/**
 * Created by antonsaburov on 02.03.17.
 */
public class ApproveManager
{
    public void approveOrder(StudentOrder so, List<CheckerAnswer> answers) {
        System.out.println("ApproveManager.approveOrder");

        try (FileWriter writer = new FileWriter("result.txt", false)) {
            writer.append("StudentOrder:" + System.lineSeparator());
            for (CheckerAnswer ca : answers) {
                writer.write(ca.getMessage().toCharArray());
//                writer.append(ca.getMessage());
                writer.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = new FileInputStream("result.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                line = br.readLine();
            }
            br.close();
            isr.close();
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream("student.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(so);
            oos.close();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            FileInputStream fis = new FileInputStream("student.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            StudentOrder so1 = (StudentOrder) ois.readObject();
            System.out.println(so.getHusband().getGivenName());
            System.out.println(so1.getHusband().getGivenName());
            ois.close();
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void denyOrder(StudentOrder so, List<CheckerAnswer> answers) {

    }
}
