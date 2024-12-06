package com.server.product.Service;

import com.server.product.Model.DTO.LoginDTO;
import com.server.product.Model.DTO.UserDTO;
import com.server.product.Model.Entity.User;
import com.server.product.Response.LoginResponse;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    String addUser(UserDTO userDTO) throws JSONException;

    LoginResponse loginUser(LoginDTO loginDTO) throws JSONException;

    User getUserById(String userId); // Add this method

    void saveUser(User user); // Add this method

    void logout();

    List<User> findAllUsers();
}

