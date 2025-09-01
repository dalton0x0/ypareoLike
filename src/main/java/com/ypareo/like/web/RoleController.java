package com.ypareo.like.web;

import com.ypareo.like.dtos.RoleRequestDto;
import com.ypareo.like.dtos.RoleResponseDto;
import com.ypareo.like.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("")
    public ResponseEntity<List<RoleResponseDto>> getRoles() {
        List<RoleResponseDto> allRoles = roleService.getAllRoles();
        return allRoles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allRoles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping("")
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody RoleRequestDto roleRequestDto) {
        return ResponseEntity.ok(roleService.createRole(roleRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
