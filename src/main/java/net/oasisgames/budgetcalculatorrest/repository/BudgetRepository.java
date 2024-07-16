package net.oasisgames.budgetcalculatorrest.repository;

import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    public void setTotalRemainder(String user, double remainder) {
        data.updateDataInRow(table, primaryKey, user, "TOTAL_REMAINDER", remainder);
    }

    public void setTaxesTaken(String user, double taxes) {
        data.updateDataInRow(table, primaryKey, user, "TAXES_TAKEN", taxes);
    }

    public void setExpenses(String user, double expenses) {
        data.updateDataInRow(table, primaryKey, user, "EXPENSES", expenses);
    }

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
    
    public Double getTotalRemainder(String user) {
        return (double) data.getSpecificData(table, primaryKey, user, "TOTAL_REMAINDER");
    }

    public Double getTaxesTaken(String user) {
        return (double) data.getSpecificData(table, primaryKey, user, "TAXES_TAKEN");
    }

    public Double getExpenses(String user) {
        return (double) data.getSpecificData(table, primaryKey, user, "EXPENSES");
    }

    public List<String> getAllUsers() {
        return data.getEntireColumn(table, "USERNAME")
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }
    
    public boolean userExists(String user) {
        return data.valueExists(table, primaryKey, user);
    }

}
