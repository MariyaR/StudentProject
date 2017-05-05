package edu.javacourse.third;

import edu.javacourse.third.db.FactoryDataSource;
import edu.javacourse.third.db.StudentOrderDataSource;
import edu.javacourse.third.domain.StudentOrder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessStarter {
    public static void main(String[] params) {
        ProcessStarter t = new ProcessStarter();
        t.processList();
    }

    public void processList() {
        StudentOrderDataSource ds = FactoryDataSource.getDataSource();
        List<StudentOrder> orderList = ds.getStudentOrders();
        ExecutorService es = Executors.newFixedThreadPool(6);
        for (StudentOrder so : orderList) {
            StudentOrderHandler sh = new StudentOrderHandler(so);
            es.execute(new Thread(sh));
        }
        es.shutdown();

    }
}