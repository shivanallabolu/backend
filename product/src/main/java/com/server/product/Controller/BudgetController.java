package com.server.product.Controller;

import com.server.product.Model.DTO.BudgetDTO;
import com.server.product.Model.Entity.Budget;
import com.server.product.Service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "https://budget-manager-1cyf.onrender.com", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // Get budgets for a specific user
    @GetMapping("/{userId}")
    public List<BudgetDTO> getBudgetsByUserId(@PathVariable String userId) {
        List<Budget> budgets = budgetService.getBudgetsByUserId(userId);
        return budgets.stream()
                .map(budget -> {
                    BudgetDTO dto = new BudgetDTO();
                    dto.setId(budget.getId());
                    dto.setCategory(budget.getCategory());
                    dto.setAmount(budget.getAmount());
                    dto.setPeriod(budget.getPeriod());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Save budgets for a user and return the saved budgets with generated IDs
    @PostMapping("/save/{userId}")
    public ResponseEntity<?> saveBudgets(@PathVariable String userId, @RequestBody List<BudgetDTO> budgetDTOs) {
        if (budgetDTOs == null || budgetDTOs.isEmpty()) {
            return ResponseEntity.badRequest().body("No budgets provided.");
        }

        List<BudgetDTO> savedBudgets = budgetService.saveBudgets(userId, budgetDTOs);
        return ResponseEntity.ok().body(savedBudgets);
    }

    // Update a specific budget by budgetId
    @PutMapping("/user/{userId}/budgets/{budgetId}")
    public ResponseEntity<?> updateBudget(@PathVariable String userId, @PathVariable String budgetId, @RequestBody BudgetDTO budgetDTO) {
        boolean isUpdated = budgetService.updateBudget(budgetId, budgetDTO);
        if (isUpdated) {
            return ResponseEntity.ok().body("Budget updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Budget not found.");
        }
    }


    // Delete a specific budget by budgetId
    @DeleteMapping("/user/{userId}/budgets/{budgetId}")
    public ResponseEntity<?> deleteBudget(@PathVariable String userId, @PathVariable String budgetId) {
        boolean isDeleted = budgetService.deleteBudget(budgetId);
        if (isDeleted) {
            return ResponseEntity.ok().body("Budget deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Budget not found.");
        }
    }

}
