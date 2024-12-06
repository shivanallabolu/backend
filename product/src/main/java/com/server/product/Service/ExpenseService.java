package com.server.product.Service;

import com.server.product.Model.DTO.ExpenseDTO;
import com.server.product.Model.Entity.Expense;
import com.server.product.Repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Save expenses for a user and return the saved expenses with ids
    public List<ExpenseDTO> saveExpenses(String userId, List<ExpenseDTO> expensesDTO) {
        List<Expense> expenses = expensesDTO.stream()
                .map(dto -> {
                    Expense expense = new Expense();
                    expense.setUserId(userId);
                    expense.setCategory(dto.getCategory());
                    expense.setAmount(dto.getAmount());
                    expense.setDate(dto.getDate());
                    expense.setNote(dto.getNote());
                    return expense;
                })
                .collect(Collectors.toList());

        // Save expenses to the database
        List<Expense> savedExpenses = expenseRepository.saveAll(expenses);

        // Convert saved expenses to DTOs
        return savedExpenses.stream()
                .map(expense -> {
                    ExpenseDTO dto = new ExpenseDTO();
                    dto.setId(expense.getId());  // Include the generated id
                    dto.setCategory(expense.getCategory());
                    dto.setAmount(expense.getAmount());
                    dto.setDate(expense.getDate());
                    dto.setNote(expense.getNote());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Update a specific expense
    public boolean updateExpense(String expenseId, ExpenseDTO expenseDTO) {
        Optional<Expense> existingExpense = expenseRepository.findById(expenseId);

        if (existingExpense.isPresent()) {
            Expense expense = existingExpense.get();
            expense.setCategory(expenseDTO.getCategory());
            expense.setAmount(expenseDTO.getAmount());
            expense.setDate(expenseDTO.getDate());
            expense.setNote(expenseDTO.getNote());
            expenseRepository.save(expense);
            return true;
        }
        return false;
    }

    // Delete a specific expense
    public boolean deleteExpense(String userId, String expenseId) {
        Optional<Expense> expense = expenseRepository.findById(expenseId);

        if (expense.isPresent()) {
            expenseRepository.delete(expense.get());
            return true;
        }
        return false;
    }

    // Fetch expenses for a specific user
    public List<Expense> getExpensesByUserId(String userId) {
        return expenseRepository.findByUserId(userId);
    }
}
