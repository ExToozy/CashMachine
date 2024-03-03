package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;

import java.util.ResourceBundle;

public class LoginCommand implements Command {
    private ResourceBundle validCreditCards = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "verifiedCards");
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "login");

    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("before"));
        while (true) {
            try {
                ConsoleHelper.writeMessage(res.getString("specify.data"));
                ConsoleHelper.writeMessage(res.getString("specify.data.card"));
                String creditCardNumber = ConsoleHelper.readString();
                if (!creditCardNumber.matches("^\\d{12}$")) {
                    throw new IllegalArgumentException(res.getString("try.again.with.details"));
                }

                ConsoleHelper.writeMessage(res.getString("specify.data.pin"));
                String pinStr = ConsoleHelper.readString();
                if (!pinStr.matches("^\\d{4}$")) {
                    throw new IllegalArgumentException(res.getString("try.again.with.details"));
                }

                if (validCreditCards.containsKey(creditCardNumber) && validCreditCards.getObject(creditCardNumber).equals(pinStr)) {
                    ConsoleHelper.writeMessage(String.format(res.getString("success.format"), creditCardNumber));
                    break;
                } else {
                    throw new SecurityException(String.format(res.getString("not.verified.format"), creditCardNumber));
                }

            } catch (IllegalArgumentException | SecurityException e) {
                ConsoleHelper.writeMessage(e.getMessage());
            }
        }
    }
}
