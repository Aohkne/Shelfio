package com.huukhoa.backend.service;

import com.huukhoa.backend.dto.request.RoleCreationRequest;
import com.huukhoa.backend.dto.response.PermissionResponse;
import com.huukhoa.backend.dto.response.RoleResponse;
import com.huukhoa.backend.entity.Role;
import com.huukhoa.backend.mapper.RoleMapper;
import com.huukhoa.backend.repository.PermissionRepository;
import com.huukhoa.backend.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;


    public RoleResponse createRole(RoleCreationRequest request) {
        Role role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
