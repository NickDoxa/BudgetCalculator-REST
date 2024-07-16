package net.oasisgames.budgetcalculatorrest.repository;

import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PostgreSQL Repository for the Budget App
 */
@Repository
public class BudgetRepository {

    private final String table = "budget_data";
    private final String primaryKey = "USERNAME";
    private final SQLData data;
    public BudgetRepository() {
        try {
            data = new SQLData("nickdoxa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set the total remainder of a users budget in the database
     * @param user the users name
     * @param remainder the users income remainder
     */
    public void setTotalRemainder(String user, double remainder) {
        data.updateDataInRow(table, primaryKey, user, "TOTAL_REMAINDER", remainder);
    }

    /**
     * Set the amount of taxes taken from a users income in the database
     * @param user the users name
     * @param taxes the amount of taxes taken
     */
    public void setTaxesTaken(String user, double taxes) {
        data.updateDataInRow(table, primaryKey, user, "TAXES_TAKEN", taxes);
    }

    /**
     * Set the users total expenses in the database
     * @param user the users name
     * @param expenses the users total expenses
     */
    public void setExpenses(String user, double expenses) {
        data.updateDataInRow(table, primaryKey, user, "EXPENSES", expenses);
    }

    /**
     * Create a new user with budget information in the database
     * @param user the users name
     * @param remainder the users remaining balance
     * @param taxes the amount of taxes taken from the users income
     * @param expenses the users total expenses
     */
    public void setNewUserBudget(String user, double remainder, double taxes, double expenses) {
        data.addRow(table, new HashMap<>() {
            {
                put("USERNAME", user);
                put("TOTAL_REMAINDER", remainder);
                put("TAXES_TAKEN", taxes);
                put("EXPENSES", expenses);
            }
        });
    }

    /**
     * Gets the users total remainder from the database
     * @param user the users name
     * @return the users total remainder
     */
    public Double getTotalRemainder(String user) {
        return (double) data.getSpecificData(table, primaryKey, user, "TOTAL_REMAINDER");
    }

    /**
     * Gets the amount of taxes taken out of the users income from the database
     * @param user the users name
     * @return the amount of taxes taken
     */
    public Double getTaxesTaken(String user) {
        return (double) data.getSpecificData(table, primaryKey, user, "TAXES_TAKEN");
    }

    /**
     * Gets the total expenses in the users budget from the database
     * @param user the users name
     * @return the users total expenses
     */
    public Double getExpenses(String user) {
        return (double) data.getSpecificData(table, primaryKey, user, "EXPENSES");
    }

    /**
     * Gets all users in the database
     * @return list of all users
     */
    public List<String> getAllUsers() {
        return data.getEntireColumn(table, "USERNAME")
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a user exists in the database
     * @param user the users name
     * @return boolean of whether the user exists or not
     */
    public boolean userExists(String user) {
        return data.valueExists(table, primaryKey, user);
    }

}
