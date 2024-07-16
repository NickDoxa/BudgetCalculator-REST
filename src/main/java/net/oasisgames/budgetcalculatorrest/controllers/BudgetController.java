package net.oasisgames.budgetcalculatorrest.controllers;

import net.oasisgames.budgetcalculatorrest.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BudgetController {

    private BudgetService budgetService;

    @PostMapping("budget")
    public BudgetReport sendBudgetData(String user, double income,
                               double monthly_expenses, double weekly_expenses) {
        return new BudgetReport(budgetService.calculateBudgetReportAndSave(
                user, income, new double[] {monthly_expenses}, new double[] {weekly_expenses}));
    }

    @GetMapping("budget")
    public BudgetInformation getBudgetData(String user) {
        double remainder = budgetService.getBudgetRepository().getTotalRemainder(user);
        double taxes = budgetService.getBudgetRepository().getTaxesTaken(user);
        double expenses = budgetService.getBudgetRepository().getExpenses(user);
        return new BudgetInformation(user, remainder, taxes, expenses);
    }

    @Autowired
    public void setBudgetService(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public static class BudgetReport {

        public String report;

        public BudgetReport(String report) {
            this.report = report;
        }
    }

    public static class BudgetInformation {

        public String user;
        public double remainder;
        public double taxes;
        public double expenses;

        public BudgetInformation(String user, double remainder, double taxes, double expenses) {
            this.user = user;
            this.remainder = remainder;
            this.taxes = taxes;
            this.expenses = expenses;
        }

    }

}
