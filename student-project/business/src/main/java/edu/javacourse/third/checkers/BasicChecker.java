package edu.javacourse.third.checkers;

import edu.javacourse.third.answer.CheckerAnswer;
import edu.javacourse.third.checkers.answer.BasicCheckerAnswer;
import edu.javacourse.third.exception.CheckException;
import edu.javacourse.third.exception.ConnectException;
import edu.javacourse.third.exception.SendGetDataException;

import java.io.*;
import java.net.Socket;

/**
 * Created by antonsaburov on 02.03.17.
 */
public abstract class BasicChecker
{
    protected final static double LEVEL = -1.000;

    protected String host;
    protected int port;
    protected String login;
    protected String password;

    protected Socket socket;

    public BasicChecker(String host, int port, String login, String password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public CheckerAnswer check() throws CheckException {
        try {
            connect();
            try {
                CheckerAnswer result = sendAndGetData();
                return result;
            } catch (SendGetDataException ex) {
                throw new CheckException(ex.getMessage(), ex);
            } finally {
                disconnect();
            }
        } catch (ConnectException ex) {
            throw new CheckException(ex.getMessage(), ex);
        }
    }

    private void connect() throws ConnectException {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectException(e.getMessage(), e);
        }
    }

    private void disconnect() throws ConnectException {
        try {
            socket.close();
        } catch(IOException ex) {
            ex.printStackTrace();
            throw new ConnectException(ex.getMessage(), ex);
        }
    }

    protected abstract CheckerAnswer sendAndGetData() throws SendGetDataException;
}
