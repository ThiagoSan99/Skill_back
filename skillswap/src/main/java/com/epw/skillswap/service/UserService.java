package com.epw.skillswap.service;
import com.epw.skillswap.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO create(UserDTO dto);

    UserDTO getById(UUID id);

    List<UserDTO> getAll();

    UserDTO update(UUID id, UserDTO dto);

    void delete(UUID id);

    UUID getUserIdByEmail(String email);

    UserDTO updateProfile(UUID id, UserDTO dto);
}