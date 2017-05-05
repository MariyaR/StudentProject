package edu.javacourse.third.db;

/**
 * Created by antonsaburov on 23.03.17.
 */
public class FactoryDataSource
{
    private static String className = "edu.javacourse.third.db.FakeDataSource";

    private static String cName = "edu.javacourse.third.db.reflection.FlexibleDataSource";
    private static String mName = "getAllOrders";

    public static StudentOrderDataSource getDataSource() {
        try {
            Class aClass = Class.forName(className);
            Object o = aClass.newInstance();
            StudentOrderDataSource ds = (StudentOrderDataSource)o;
            return ds;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new FakeDataSource();
    }
}
