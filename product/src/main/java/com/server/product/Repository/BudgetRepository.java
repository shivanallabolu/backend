package com.server.product.Repository;

import com.server.product.Model.Entity.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findByUserId(String userId); // Fetch budgets by userId
}
