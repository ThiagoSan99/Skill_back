package com.epw.skillswap.controller;

import com.epw.skillswap.dto.TeacherAvailabilityDTO;
import com.epw.skillswap.service.TeacherAvailabilityService;
import com.epw.skillswap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/me/availability")
@RequiredArgsConstructor
public class TeacherAvailabilityController {

    private final TeacherAvailabilityService availabilityService;
    private final UserService userService;

    @GetMapping
    public List<TeacherAvailabilityDTO> getMyAvailability(Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return availabilityService.getMyAvailability(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherAvailabilityDTO addAvailability(@RequestBody TeacherAvailabilityDTO dto,
                                                   Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return availabilityService.addAvailability(userId, dto);
    }

    @PutMapping("/{availabilityId}")
    public TeacherAvailabilityDTO updateAvailability(@PathVariable UUID availabilityId,
                                                      @RequestBody TeacherAvailabilityDTO dto,
                                                      Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return availabilityService.updateAvailability(availabilityId, userId, dto);
    }

    @DeleteMapping("/{availabilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAvailability(@PathVariable UUID availabilityId,
                                    Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        availabilityService.removeAvailability(availabilityId, userId);
    }
}
