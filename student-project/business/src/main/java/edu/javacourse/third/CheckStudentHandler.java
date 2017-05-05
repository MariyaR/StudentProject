package edu.javacourse.third;

import edu.javacourse.third.answer.CheckerAnswer;
import edu.javacourse.third.checkers.StudentChecker;
import edu.javacourse.third.domain.StudentOrder;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by maria on 05/05/17.
 */
public class CheckStudentHandler implements Callable<List<CheckerAnswer>> {

    private StringWriter sw;
    private StudentOrder so;

    CheckStudentHandler (StudentOrder so, StringWriter sw) {this.so=so; this.sw=sw;}

    @Override
    public List<CheckerAnswer> call() {
        List<CheckerAnswer> answers = new ArrayList<>();
        return (handleList(answers));
    }

    private List<CheckerAnswer> handleList (List<CheckerAnswer> answers){

        List<CheckerAnswer> Answers=answers;
        sw.write("---------------------checking Student status---------------------"+"\n");
        ExecutorService es = Executors.newFixedThreadPool(2);
        List<Future<CheckerAnswer>> result = new ArrayList<>();
        StudentChecker stH = new StudentChecker(so.getHusband(), sw);
        result.add(es.submit(stH));
        StudentChecker stW = new StudentChecker(so.getWife(), sw);
        result.add(es.submit(stW));
        for(Future<CheckerAnswer> f : result) {
            try {
                CheckerAnswer answer = f.get();
                Answers.add(answer);
            } catch (Exception e) {e.printStackTrace();}
        }
        es.shutdown();
        sw.write("----------------checking Student status completed----------------"+"\n\n");
        return Answers;
    }
}
