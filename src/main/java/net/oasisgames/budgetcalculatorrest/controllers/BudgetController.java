package net.oasisgames.budgetcalculatorrest.controllers;

import net.oasisgames.budgetcalculatorrest.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for the Budget App
 */
@RestController
public class BudgetController {

    private BudgetService budgetService;

    /**
     * Post Mapping for the budget path
     * @param user the users name
     * @param income the users income
     * @param monthly_expenses the users monthly expenses
     * @param weekly_expenses the users weekly expenses
     * @return Budget report in JSON format
     */
    @PostMapping("budget")
    public BudgetReport sendBudgetData(String user, double income,
                               double monthly_expenses, double weekly_expenses) {
        return new BudgetReport(budgetService.calculateBudgetReportAndSave(
                user, income, new double[] {monthly_expenses}, new double[] {weekly_expenses}));
    }

    /**
     * Get Mapping for the budget path
     * @param user the users name
     * @return Budget information in JSON format
     */
    @GetMapping("budget")
    public BudgetInformation getBudgetData(String user) {
        double remainder = budgetService.getBudgetRepository().getTotalRemainder(user);
        double taxes = budgetService.getBudgetRepository().getTaxesTaken(user);
        double expenses = budgetService.getBudgetRepository().getExpenses(user);
        return new BudgetInformation(user, remainder, taxes, expenses);
    }

    /**
     * Get Mapping for the users path
     * @return Users list in JSON format
     */
    @GetMapping("users")
    public UserList getAllUsers() {
        return new UserList(budgetService.getBudgetRepository().getAllUsers());
    }

    /**
     * Autowired setter for the BudgetService object
     * @param budgetService the budget service object (Autowired)
     */
    @Autowired
    public void setBudgetService(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    /**
     * Static inner class created for the purpose of returning JSON data instead of a raw
     * String report
     */
    public static class BudgetReport {

        public String report;

        public BudgetReport(String report) {
            this.report = report;
        }
    }

    /**
     * Static inner class created for the purpose of returning JSON data instead of raw
     * budget data
     */
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

    /**
     * Static inner class created for the purpose of returning JSON data instead of a raw
     * string list of users
     */
    public static class UserList {

        public List<String> users;

        public UserList(List<String> users) {
            this.users = users;
        }

    }

}
