package net.oasisgames.budgetcalculatorrest.components;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Class for calculating basic information about the users budget
 */
@Component
public class Calculate {

    /**
     * Determines the remainder of the users income after expenses and taxes
     * @param monthly the users monthly expenses
     * @param weekly the users weekly expenses
     * @param income the users total annual income
     * @return the users remaining income
     */
    public double calculateTotalRemainder(double[] monthly, double[] weekly, double income) {
        double tax = calculateFederalTax(income);
        double totalExpense = calculateTotalExpenses(monthly, weekly);
        if (totalExpense == -1) return -1;
        double afterTax = income - (income * tax);
        return afterTax - totalExpense;
    }

    /**
     * Reduces all expenses down to one number after multiplying them to a yearly total
     * @param monthly array of all monthly expenses
     * @param weekly array of all weekly expenses
     * @return the total expenses
     */
    public double calculateTotalExpenses(double[] monthly, double[] weekly) {
        double totalMonth = Arrays.stream(monthly)
                .map(i -> i * 12)
                .reduce(Double::sum)
                .orElse(-1);
        double totalWeekly = Arrays.stream(weekly)
                .map(i -> i * 48)
                .reduce(Double::sum)
                .orElse(-1);
        return totalMonth + totalWeekly;
    }

    /**
     * Calculate the tax percentage of the users income
     * @param income the users annual income
     * @return the total federal tax percent represented as a decimal double
     */
    public double calculateFederalTax(double income) {
        double tax;
        if (income < 11600) tax = 0.10;
        else if (income > 11600 && income < 47150) tax = 0.12;
        else if (income > 47150 && income < 100525) tax = 0.22;
        else if (income > 100525 && income < 191950) tax = 0.24;
        else if (income > 191950 && income < 243725) tax = 0.32;
        else if (income > 243725 && income < 609350) tax = 0.35;
        else tax = 0.37;
        return tax;
    }

    /**
     * Formats a numeric string to USD currency
     * @param str string to format to currency (USD)
     * @return formatted string
     */
    public static String formatToCurrency(String str) {
        DecimalFormat formatter = new DecimalFormat("$#,##0.00");
        BigDecimal deci = new BigDecimal(str);
        return formatter.format(deci);
    }

    /**
     * Formats a numeric string to USD currency
     * @param dbl dbl to format to string currency (USD)
     * @return formatted string
     */
    public static String formatToCurrency(Double dbl) {
        return formatToCurrency(String.valueOf(dbl));
    }

}
