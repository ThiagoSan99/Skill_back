package com.epw.skillswap.controller;

import com.epw.skillswap.dto.UserDTO;
import com.epw.skillswap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody UserDTO dto){
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable UUID id){
        return service.getById(id);
    }

    @GetMapping
    public List<UserDTO> getAll(){
        return service.getAll();
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable UUID id,
                          @RequestBody UserDTO dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        service.delete(id);
    }

    @GetMapping("/me")
    public UserDTO getCurrentUser(Authentication authentication) {
        UUID userId = service.getUserIdByEmail(authentication.getName());
        return service.getById(userId);
    }

    @PutMapping("/me")
    public UserDTO updateCurrentUser(@RequestBody UserDTO dto,
                                      Authentication authentication) {
        UUID userId = service.getUserIdByEmail(authentication.getName());
        return service.updateProfile(userId, dto);
    }
}