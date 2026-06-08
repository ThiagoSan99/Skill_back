package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.UserDTO;
import com.epw.skillswap.entity.User;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.UserRepository;
import com.epw.skillswap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserDTO create(UserDTO dto) {

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .build();

        return mapToDTO(repository.save(user));
    }

    @Override
    public UserDTO getById(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToDTO(user);
    }

    @Override
    public List<UserDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public UserDTO update(UUID id, UserDTO dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        return mapToDTO(repository.save(user));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public UUID getUserIdByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getUserId();
    }

    @Override
    public UserDTO updateProfile(UUID id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }

        return mapToDTO(repository.save(user));
    }

    private UserDTO mapToDTO(User user){
        return UserDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .currentCreditBalance(user.getCurrentCreditBalance())
                .reputationScore(user.getReputationScore())
                .bio(user.getBio())
                .build();
    }
}