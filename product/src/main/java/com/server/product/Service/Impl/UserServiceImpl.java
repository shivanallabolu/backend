package com.server.product.Service.Impl;

import com.server.product.Model.DTO.LoginDTO;
import com.server.product.Model.DTO.UserDTO;
import com.server.product.Model.Entity.User;
import com.server.product.Repository.UserRepo;
import com.server.product.Response.LoginResponse;
import com.server.product.Service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String addUser(UserDTO userDTO) throws JSONException {
        // Check if a user already exists with the given email
        User existingUser = userRepo.findByEmail(userDTO.getUserEmail());
        if (existingUser != null) {
            // Return a response indicating the email is already in use
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "User with this email already exists.");
            return jsonResponse.toString();
        }

        // If user does not exist, proceed with creating a new user
        User user = new User(
                userDTO.getUserId(),
                userDTO.getUserName(),
                userDTO.getUserEmail(),
                userDTO.getUserPassword()
        );
        user.setUserPassword(this.passwordEncoder.encode(userDTO.getUserPassword()));

        // Save the new user
        userRepo.save(user);

        // Return the user details in the response
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("userName", user.getUserName());
        jsonResponse.put("userId", user.getUserId()); // You can return the userId as well
        return jsonResponse.toString();
    }



    @Override
    public LoginResponse loginUser(LoginDTO loginDTO) throws JSONException {
        String loginMessage = "";
        Boolean statusCode = false;
        String userName = null;
        String userId = null;

        User user = userRepo.findByEmail(loginDTO.getUserEmail());

        if (user != null) {
            if (passwordEncoder.matches(loginDTO.getUserPassword(), user.getUserPassword())) {
                statusCode = true;
                userName = user.getUserName();
                userId = user.getUserId();
                loginMessage = "Login is successful!";
            } else {
                loginMessage = "Password is incorrect, try again!";
            }
        } else {
            loginMessage = "Email doesn't exist!";
        }

        // Return LoginResponse with the added userId
        return new LoginResponse(loginMessage, statusCode, userName, userId);  // Include userId in the response
    }


    @Override
    public User getUserById(String userId) {
        return userRepo.findById(userId).orElse(null);  // Use String instead of Integer
    }


    @Override
    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Override
    public void logout() {
        // Clear the authentication when logging out
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public List<User> findAllUsers(){
        return userRepo.findAll();
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        // Fetch the existing user by ID
        User existingUser = userRepo.findById(userDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Update user details (name, email, password)
        if (userDTO.getUserName() != null && !userDTO.getUserName().isEmpty()) {
            existingUser.setUserName(userDTO.getUserName());
        }
        if (userDTO.getUserEmail() != null && !userDTO.getUserEmail().isEmpty()) {
            existingUser.setUserEmail(userDTO.getUserEmail());
        }
        if (userDTO.getUserPassword() != null && !userDTO.getUserPassword().isEmpty()) {
            // Update password only if it's provided
            existingUser.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        }

        // Save the updated user
        userRepo.save(existingUser);

        // Return the updated UserDTO
        return new UserDTO(existingUser.getUserId(), existingUser.getUserName(), existingUser.getUserEmail(), null);  // Do not return the password for security
    }

}

