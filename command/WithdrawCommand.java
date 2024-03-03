package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulator;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;
import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.util.Map;
import java.util.ResourceBundle;

class WithdrawCommand implements Command {
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "withdraw");

    @Override
    public void execute() throws InterruptOperationException {
        String code = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator currencyManipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(code);
        while (true) {
            try {
                ConsoleHelper.writeMessage(res.getString("specify.amount"));
                int amount = Integer.parseInt(ConsoleHelper.readString());
                if (!currencyManipulator.isAmountAvailable(amount)) {
                    ConsoleHelper.writeMessage(res.getString("not.enough.money"));
                    break;
                }
                ConsoleHelper.writeMessage(res.getString("before"));
                Map<Integer, Integer> denominationForWithdraw = currencyManipulator.withdrawAmount(amount);
                ConsoleHelper.writeMessage(String.format(res.getString("success.format"), amount, code));
                denominationForWithdraw.forEach(
                        (denomination, count) -> System.out.println("\t" + denomination + " - " + count)
                );
                break;
            } catch (NumberFormatException e) {
                ConsoleHelper.writeMessage(res.getString("specify.not.empty.amount"));
            } catch (NotEnoughMoneyException e) {
                ConsoleHelper.writeMessage(res.getString("exact.amount.not.available"));
                break;
            }
        }
    }
}
