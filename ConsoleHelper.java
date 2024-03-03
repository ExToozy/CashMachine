package com.javarush.task.task26.task2613;

import com.javarush.task.task26.task2613.exception.InterruptOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ResourceBundle;


public class ConsoleHelper {
    private static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
    private static ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "common");

    public static String askCurrencyCode() throws InterruptOperationException {
        while (true) {
            writeMessage(res.getString("choose.currency.code"));
            String currency = readString();
            if (currency != null && currency.matches("^[A-Za-z]{3}$")) {
                return currency.toUpperCase();
            } else {
                ConsoleHelper.writeMessage(res.getString("invalid.data"));
            }
        }
    }

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws InterruptOperationException {
        String line = null;
        try {
            line = bis.readLine();
        } catch (IOException e) {
        }
        if (line != null && line.equalsIgnoreCase("exit")) {
            throw new InterruptOperationException();
        }
        return line;
    }

    public static Operation askOperation() throws InterruptOperationException {
        while (true) {
            writeMessage(res.getString("choose.operation"));
            writeMessage("1)" + res.getString("operation.INFO"));
            writeMessage("2)" + res.getString("operation.DEPOSIT"));
            writeMessage("3)" + res.getString("operation.WITHDRAW"));
            writeMessage("4)" + res.getString("operation.EXIT"));
            String operationNumber = readString();
            try {
                return Operation.getAllowableOperationByOrdinal(Integer.parseInt(operationNumber));
            } catch (NumberFormatException e) {
                ConsoleHelper.writeMessage(res.getString("operation.not.exist"));
            } catch (IllegalArgumentException e) {
                ConsoleHelper.writeMessage(res.getString("invalid.data"));
            }
        }
    }

    public static void printExitMessage() {
        ConsoleHelper.writeMessage(res.getString("the.end"));
    }

    public static String[] getValidTwoDigits(String currencyCode) throws InterruptOperationException {
        while (true) {
            writeMessage(String.format(res.getString("choose.denomination.and.count.format"), currencyCode));
            String[] denominationAndCount;
            denominationAndCount = readString().split(" ");
            if (denominationAndCount.length == 2 &&
                    Arrays.stream(denominationAndCount).allMatch(i -> i.matches("^\\d+$"))) {
                return denominationAndCount;
            } else {
                ConsoleHelper.writeMessage(res.getString("invalid.data"));
            }
        }
    }

}
