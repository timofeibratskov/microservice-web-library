package com.example.user.service;

import com.example.user.entity.Role;
import com.example.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
