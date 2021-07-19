package com.mo.JWTAuth.web.request;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String email;
    private String password;
}
