package com.javarush.task.task26.task2613;

import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CurrencyManipulator {
    private Map<Integer, Integer> denominations = new TreeMap<>((firstValue, secondValue) -> secondValue - firstValue);
    private String currencyCode;

    public CurrencyManipulator(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean hasMoney() {
        if (getTotalAmount() == 0) {
            return false;
        }
        return true;
    }

    public int getTotalAmount() {
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : denominations.entrySet()) {
            sum += entry.getKey() * entry.getValue();
        }
        return sum;
    }

    public boolean isAmountAvailable(int expectedAmount) {
        return getTotalAmount() >= expectedAmount;
    }

    public Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException {
        int totalAmount = 0;

        Map<Integer, Integer> denominationsForWithdraw = new TreeMap<>(
                (firstValue, secondValue) -> secondValue - firstValue
        );
        Map<Integer, Integer> denominationsCopy = new HashMap<>(denominations);

        while (true) {
            for (int denomination : denominations.keySet()) {
                if (totalAmount + denomination <= expectedAmount) {
                    totalAmount += denomination;
                    denominations.put(denomination, denominations.get(denomination) - 1);
                    if (denominations.get(denomination) == 0) {
                        denominations.remove(denomination);
                    }
                    denominationsForWithdraw.merge(denomination, 1, Integer::sum);
                    break;
                }
            }
            boolean isFinalResult = true;
            for (int denomination : denominations.keySet()) {
                if (totalAmount + denomination <= expectedAmount) {
                    isFinalResult = false;
                    break;
                }
            }
            if (isFinalResult) {
                break;
            }
        }

        if (totalAmount != expectedAmount) {
            denominations.clear();
            denominations.putAll(denominationsCopy);
            throw new NotEnoughMoneyException();
        }
        return denominationsForWithdraw;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void addAmount(int denomination, int count) {
        denominations.merge(denomination, count, Integer::sum);
    }
}
