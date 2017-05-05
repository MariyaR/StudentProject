package edu.javacourse.third.checkers.answer;

import edu.javacourse.third.answer.CheckerAnswer;

/**
 * Created by antonsaburov on 16.03.17.
 */
public class BasicCheckerAnswer implements CheckerAnswer
{
    private boolean result;
    private String message;

    public BasicCheckerAnswer(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public boolean getResult() {
        return result;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
