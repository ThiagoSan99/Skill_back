package com.epw.skillswap.controller;

import com.epw.skillswap.dto.UserSkillDTO;
import com.epw.skillswap.service.UserService;
import com.epw.skillswap.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/me/skills")
@RequiredArgsConstructor
public class UserSkillController {

    private final UserSkillService userSkillService;
    private final UserService userService;

    @GetMapping
    public List<UserSkillDTO> getUserSkills(Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return userSkillService.getUserSkills(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserSkillDTO addUserSkill(@RequestBody UserSkillDTO dto,
                                     Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return userSkillService.addUserSkill(userId, dto);
    }

    @DeleteMapping("/{userSkillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserSkill(@PathVariable UUID userSkillId) {
        userSkillService.removeUserSkill(userSkillId);
    }
}
