package com.example.ProfieService.Service;

import com.example.ProfieService.DTO.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserProfile(Long userId);
    UserDTO updateProfile(Long userId, UserDTO userDTO);
    List<UserDTO> getAllUsers();
}
