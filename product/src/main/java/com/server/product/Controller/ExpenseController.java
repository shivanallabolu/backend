package com.server.product.Controller;

import com.server.product.Model.DTO.ExpenseDTO;
import com.server.product.Model.Entity.Expense;
import com.server.product.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "https://budget-manager-1cyf.onrender.com", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Get expenses for a specific user
    @GetMapping("/{userId}")
    public List<ExpenseDTO> getExpensesByUserId(@PathVariable String userId) {
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        return expenses.stream()
                .map(expense -> {
                    ExpenseDTO dto = new ExpenseDTO();
                    dto.setId(expense.getId());  // Include the generated ID
                    dto.setCategory(expense.getCategory());
                    dto.setAmount(expense.getAmount());
                    dto.setDate(expense.getDate());
                    dto.setNote(expense.getNote());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Save expenses for a user and return the saved expenses with generated IDs
    @PostMapping("/save/{userId}")
    public ResponseEntity<?> saveExpenses(@PathVariable String userId, @RequestBody List<ExpenseDTO> expensesDTO) {
        if (expensesDTO == null || expensesDTO.isEmpty()) {
            return ResponseEntity.badRequest().body("No expenses provided.");
        }

        List<ExpenseDTO> savedExpenses = expenseService.saveExpenses(userId, expensesDTO);
        return ResponseEntity.ok().body(new SavedExpensesResponse(savedExpenses)); // Return saved expenses with ids
    }

    // Update a specific expense by expenseId
    @PutMapping("/user/{userId}/expenses/{expenseId}")
    public ResponseEntity<?> updateExpense(@PathVariable String userId, @PathVariable String expenseId, @RequestBody ExpenseDTO expenseDTO) {
        boolean isUpdated = expenseService.updateExpense(expenseId, expenseDTO);
        if (isUpdated) {
            List<ExpenseDTO> updatedExpenses = expenseService.getExpensesByUserId(userId).stream()
                    .map(expense -> {
                        ExpenseDTO dto = new ExpenseDTO();
                        dto.setId(expense.getId());
                        dto.setCategory(expense.getCategory());
                        dto.setAmount(expense.getAmount());
                        dto.setDate(expense.getDate());
                        dto.setNote(expense.getNote());
                        return dto;
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(new SavedExpensesResponse(updatedExpenses)); // Return updated expenses
        } else {
            return ResponseEntity.status(404).body("Expense not found.");
        }
    }

    // Delete a specific expense by expenseId
    @DeleteMapping("/user/{userId}/expenses/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable String userId, @PathVariable String expenseId) {
        boolean isDeleted = expenseService.deleteExpense(userId, expenseId);
        if (isDeleted) {
            List<ExpenseDTO> remainingExpenses = expenseService.getExpensesByUserId(userId).stream()
                    .map(expense -> {
                        ExpenseDTO dto = new ExpenseDTO();
                        dto.setId(expense.getId());
                        dto.setCategory(expense.getCategory());
                        dto.setAmount(expense.getAmount());
                        dto.setDate(expense.getDate());
                        dto.setNote(expense.getNote());
                        return dto;
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(new SavedExpensesResponse(remainingExpenses)); // Return remaining expenses
        } else {
            return ResponseEntity.status(404).body("Expense not found.");
        }
    }

    // Response class to wrap saved expenses
    public static class SavedExpensesResponse {
        private List<ExpenseDTO> savedExpenses;

        public SavedExpensesResponse(List<ExpenseDTO> savedExpenses) {
            this.savedExpenses = savedExpenses;
        }

        public List<ExpenseDTO> getSavedExpenses() {
            return savedExpenses;
        }

        public void setSavedExpenses(List<ExpenseDTO> savedExpenses) {
            this.savedExpenses = savedExpenses;
        }
    }
}
