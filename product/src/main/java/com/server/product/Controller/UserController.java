package com.server.product.Controller;

import com.server.product.Model.DTO.ExpenseDTO;
import com.server.product.Model.DTO.LoginDTO;
import com.server.product.Model.DTO.UserDTO;
import com.server.product.Model.Entity.Expense;
import com.server.product.Response.LoginResponse;
import com.server.product.Service.ExpenseService;
import com.server.product.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/budgetmanager/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping(path = "/register")
    public String registerUser(@RequestBody UserDTO userDTO) throws JSONException {
        String id = userService.addUser(userDTO);
        return id;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) throws JSONException {
        LoginResponse loginResponse = userService.loginUser(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<?> logoutUser() {
        userService.logout();
        return ResponseEntity.ok("Logout successful");
    }

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/{userId}/expenses")
    public List<ExpenseDTO> getExpensesByUserId(@PathVariable String userId) {
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);

        // Convert Expense to ExpenseDTO
        return expenses.stream()
                .map(expense -> {
                    ExpenseDTO dto = new ExpenseDTO();
                    dto.setCategory(expense.getCategory());
                    dto.setAmount(expense.getAmount());
                    dto.setDate(expense.getDate());
                    dto.setNote(expense.getNote());
                    // You might want to add userId here as well if it's part of the DTO
                    dto.setUserId(expense.getUserId()); // Assuming you add userId to Expense
                    return dto;
                })
                .toList();
    }


    // Endpoint to save expenses for a user
    @PostMapping("/{userId}/expenses/save")
    public String saveExpenses(@PathVariable String userId, @RequestBody List<ExpenseDTO> expenseDTOList) {
        expenseService.saveExpenses(userId, expenseDTOList);
        return "Expenses saved successfully.";
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUserDTO = userService.updateUser(userDTO);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (ResponseStatusException e) {
            // Return status and reason from ResponseStatusException
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user.");
        }
    }


}
