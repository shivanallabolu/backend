package com.server.product.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String userMessage;
    private Boolean statusCode;
    private String userName;
    private String userId;
}
