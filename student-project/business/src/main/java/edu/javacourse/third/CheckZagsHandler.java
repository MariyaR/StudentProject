package edu.javacourse.third;

import edu.javacourse.third.answer.CheckerAnswer;
import edu.javacourse.third.checkers.ZagsChecker;
import edu.javacourse.third.domain.PersonChild;
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
public class CheckZagsHandler implements Callable<List<CheckerAnswer>> {

    private StringWriter sw;
    private StudentOrder so;

    CheckZagsHandler(StudentOrder so, StringWriter sw) {this.so=so; this.sw=sw;}

    @Override
    public List<CheckerAnswer> call() {
    List <CheckerAnswer> answers = new ArrayList<CheckerAnswer>();

    return (handleCheckZags(answers));
    }

    private List<CheckerAnswer> handleCheckZags (List <CheckerAnswer> answers) {

        List<CheckerAnswer> Answers = answers;
        sw.write("--------------------------checking Zags--------------------------"+"\n\n");
        ExecutorService es = Executors.newFixedThreadPool(4);
        List<Future<CheckerAnswer>> result = new ArrayList<>();
        ZagsChecker zw = new ZagsChecker(so.getHusband(),so.getWife(),sw);
        result.add(es.submit(zw));

        for (PersonChild ch: so.getChildren()) {
            ZagsChecker zch = new ZagsChecker(so.getWife(), so.getHusband(), ch,sw);
            result.add(es.submit(zch));
        }
        for(Future<CheckerAnswer> f : result) {
            try {
                CheckerAnswer answer = f.get();
                Answers.add(answer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        es.shutdown();
        sw.write("---------------------checking Zags completed---------------------"+"\n");
        return Answers;
    }
}
