package com.server.product.Service;

import com.server.product.Model.DTO.BudgetDTO;
import com.server.product.Model.Entity.Budget;
import com.server.product.Repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    // Save budgets for a user and return the saved budgets with IDs
    public List<BudgetDTO> saveBudgets(String userId, List<BudgetDTO> budgetDTOs) {
        List<Budget> budgets = budgetDTOs.stream()
                .map(dto -> {
                    Budget budget = new Budget();
                    budget.setUserId(userId);
                    budget.setCategory(dto.getCategory());
                    budget.setAmount(dto.getAmount());
                    budget.setPeriod(dto.getPeriod());
                    return budget;
                })
                .collect(Collectors.toList());

        List<Budget> savedBudgets = budgetRepository.saveAll(budgets);

        return savedBudgets.stream()
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

    // Update a specific budget
    public boolean updateBudget(String budgetId, BudgetDTO budgetDTO) {
        Optional<Budget> existingBudget = budgetRepository.findById(budgetId);

        if (existingBudget.isPresent()) {
            Budget budget = existingBudget.get();
            budget.setCategory(budgetDTO.getCategory());
            budget.setAmount(budgetDTO.getAmount());
            budget.setPeriod(budgetDTO.getPeriod());
            budgetRepository.save(budget);
            return true;
        }
        return false;
    }

    // Delete a specific budget
    public boolean deleteBudget(String budgetId) {
        Optional<Budget> budget = budgetRepository.findById(budgetId);

        if (budget.isPresent()) {
            budgetRepository.delete(budget.get());
            return true;
        }
        return false;
    }

    // Fetch budgets for a specific user
    public List<Budget> getBudgetsByUserId(String userId) {
        return budgetRepository.findByUserId(userId);
    }
}
