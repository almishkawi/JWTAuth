package com.mo.JWTAuth.web.response;

import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
}
