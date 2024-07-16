package net.oasisgames.budgetcalculatorrest.services;

import net.oasisgames.budgetcalculatorrest.components.Calculate;
import net.oasisgames.budgetcalculatorrest.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for the Budget App logic
 */
@Service
public class BudgetService {

    private BudgetRepository budgetRepository;
    private Calculate calculate;

    /**
     * Gets the Calculate object
     * @return calculate object
     */
    public Calculate getCalculate() {
        return calculate;
    }

    /**
     * Autowired Calculate object setter
     * @param calculate calculate object
     */
    @Autowired
    public void setCalculate(Calculate calculate) {
        this.calculate = calculate;
    }

    /**
     * Gets the BudgetRepository object
     * @return BudgetRepository object
     */
    public BudgetRepository getBudgetRepository() {
        return budgetRepository;
    }

    /**
     * Autowired BudgetRepository object setter
     * @param budgetRepository BudgetRepository object
     */
    @Autowired
    public void setBudgetRepository(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    /**
     * Calculate a budget report for the user and save their information using the repository
     * @param user the users name
     * @param income the users income
     * @param monthly the users monthly_expenses
     * @param weekly the users weekly_expenses
     * @return Budget Report as a String
     */
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

}
