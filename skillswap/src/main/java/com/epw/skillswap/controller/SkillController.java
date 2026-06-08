package com.epw.skillswap.controller;


import com.epw.skillswap.dto.SkillDTO;
import com.epw.skillswap.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillDTO create(@RequestBody SkillDTO dto){
        return service.create(dto);
    }

    @GetMapping
    public List<SkillDTO> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SkillDTO getById(@PathVariable UUID id){
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public SkillDTO update(@PathVariable UUID id,
                           @RequestBody SkillDTO dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        service.delete(id);
    }
}