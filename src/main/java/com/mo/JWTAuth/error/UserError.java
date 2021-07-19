package com.mo.JWTAuth.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserError {
    private Set<String> errors = new HashSet<>();

    public void addError(String errorMsg) {
        errors.add(errorMsg);
    }
}
