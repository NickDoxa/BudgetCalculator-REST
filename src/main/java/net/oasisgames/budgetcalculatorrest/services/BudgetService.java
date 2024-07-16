package net.oasisgames.budgetcalculatorrest.services;

import net.oasisgames.budgetcalculatorrest.components.Calculate;
import net.oasisgames.budgetcalculatorrest.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    private BudgetRepository budgetRepository;
    private Calculate calculate;

    public Calculate getCalculate() {
        return calculate;
    }

    @Autowired
    public void setCalculate(Calculate calculate) {
        this.calculate = calculate;
    }

    public BudgetRepository getBudgetRepository() {
        return budgetRepository;
    }

    @Autowired
    public void setBudgetRepository(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public String calculateBudgetReportAndSave(String user, double income,
                                        double[] monthly, double[] weekly) {
        double remainder = calculate.calculateTotalRemainder(monthly, weekly, income);
        double tax_percent = calculate.calculateFederalTax(income);
        double taxes_taken = income * tax_percent;
        double expenses = calculate.calculateTotalExpenses(monthly, weekly);
        if (!budgetRepository.userExists(user)) {
            budgetRepository.setNewUserBudget(user, remainder, tax_percent, taxes_taken);
        } else {
            budgetRepository.setTotalRemainder(user, remainder);
            budgetRepository.setTaxesTaken(user, taxes_taken);
            budgetRepository.setExpenses(user, expenses);
        }
        return "Your gross income was " + Calculate.formatToCurrency(income) +
                ". After losing " + Calculate.formatToCurrency(taxes_taken) +
                " to taxes, your income becomes " +
                Calculate.formatToCurrency(income - taxes_taken) +
                ". Your expenses add up to " + Calculate.formatToCurrency(expenses) +
                ". This leaves you with a final remainder of " +
                Calculate.formatToCurrency(remainder);
    }

    public String calculateBudgetReport(double income, double[] monthly, double[] weekly) {
        double remainder = calculate.calculateTotalRemainder(monthly, weekly, income);
        double tax_percent = calculate.calculateFederalTax(income);
        double taxes_taken = income * tax_percent;
        double expenses = calculate.calculateTotalExpenses(monthly, weekly);
        return "Your gross income was " + Calculate.formatToCurrency(income) +
                ". After losing " + Calculate.formatToCurrency(taxes_taken) +
                " to taxes, your income becomes " +
                Calculate.formatToCurrency(income - taxes_taken) +
                ". Your expenses add up to " + Calculate.formatToCurrency(expenses) +
                ". This leaves you with a final remainder of " +
                Calculate.formatToCurrency(remainder);
    }



}
