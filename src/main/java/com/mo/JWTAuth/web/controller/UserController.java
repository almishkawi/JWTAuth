package com.mo.JWTAuth.web.controller;

import com.mo.JWTAuth.service.UserService;
import com.mo.JWTAuth.shared.dto.UserDTO;
import com.mo.JWTAuth.web.request.CreateUserRequest;
import com.mo.JWTAuth.web.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest userRequest) {
        UserDTO userRequestDto = modelMapper.map(userRequest, UserDTO.class);

        UserDTO createdUser = userService.createUser(userRequestDto);

        UserResponse userResponse = modelMapper.map(createdUser, UserResponse.class);

        return new ResponseEntity(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/authenticated")
    public ResponseEntity<UserResponse> getAuthenticatedUser(Authentication authentication) {
        String userEmail = (String) authentication.getPrincipal();
        if (userEmail != null) {
            UserDTO authenticatedUser = userService.getUserByEmail(userEmail);
            UserResponse userResponse = modelMapper.map(authenticatedUser, UserResponse.class);

            if (userResponse == null) return new ResponseEntity(HttpStatus.FORBIDDEN);

            return ResponseEntity.ok(userResponse);
        }

        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}
