package edu.javacourse.third;

import edu.javacourse.third.answer.CheckerAnswer;
import edu.javacourse.third.checkers.GrnChecker;
import edu.javacourse.third.domain.PersonChild;
import edu.javacourse.third.domain.StudentOrder;
import edu.javacourse.third.exception.CheckException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by maria on 05/05/17.
 */
public class StudentOrderHandler implements Runnable{

    private StudentOrder so;
    private StringWriter OutStringZags= new StringWriter();
    private StringWriter OutStringStudent= new StringWriter();

    public StudentOrderHandler(StudentOrder so) {
        this.so = so;
    }

    public void run() {
        try {
            handleStudentOrder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleStudentOrder() throws IOException {
       List<CheckerAnswer> answers = new ArrayList<>();
        try {
            //answers.addAll(checkGrn(so));
            ExecutorService es = Executors.newFixedThreadPool(2);
            List<Future<List <CheckerAnswer>>> result = new ArrayList<>();
            CheckZagsHandler zh = new CheckZagsHandler(so, OutStringZags);
            result.add(es.submit(zh));
            CheckStudentHandler sh = new CheckStudentHandler(so, OutStringStudent);
            result.add(es.submit(sh));
            for(Future<List <CheckerAnswer>> f : result) {
                try {
                    List <CheckerAnswer> answer = f.get();
                    answers.addAll(answers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            es.shutdown();
            System.out.print(OutStringZags);
            System.out.print(OutStringStudent);
        } catch(Exception ex) {
            // TODO Сделать обработку ошибки - что-то записать в базу
            return;
        }

        ApproveManager approveManager = new ApproveManager();
        for(CheckerAnswer ca : answers) {
            if(!ca.getResult()) {
                approveManager.denyOrder(so, answers);
                return;
            }
        }
        approveManager.approveOrder(so, answers);
    }

    private List<CheckerAnswer> checkGrn(StudentOrder so) throws CheckException {
        GrnChecker grn = new GrnChecker("127.0.0.1", 7777, "3", "4");
        List<CheckerAnswer> answers = new ArrayList<>();
        // GrnChecker grn = new GrnChecker();
        grn.setPerson(so.getHusband());
        answers.add(grn.check());
        grn.setPerson(so.getWife());
        answers.add(grn.check());
        for (PersonChild pc : so.getChildren()) {
            grn.setPerson(pc);
            answers.add(grn.check());
        }
        return answers;
    }
}
