package com.mo.JWTAuth.service;

import com.mo.JWTAuth.data.entity.User;
import com.mo.JWTAuth.data.repository.UserRepository;
import com.mo.JWTAuth.shared.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }
        User userWithGivenEmail = userRepository.findByEmail(userDTO.getEmail());

        if (userWithGivenEmail != null) {
            throw new RuntimeException("Something went wrong");
        }

        String hashedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(hashedPassword);

        User user = modelMapper.map(userDTO, User.class);

        User createdUser = userRepository.save(user);

        if (createdUser == null) {
            throw new RuntimeException("User SIGNUP failed");
        }

        userDTO.setId(createdUser.getId());

        return userDTO;
    }

    public UserDTO getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return userDTO;
    }
}
