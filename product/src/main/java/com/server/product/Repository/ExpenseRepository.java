package com.server.product.Repository;

import com.server.product.Model.Entity.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserId(String userId);  // Fetch expenses by userId
}
