package com.epam.esm.services;

import com.epam.esm.entities.Role;
import com.epam.esm.entities.User;
import com.epam.esm.repositories.RoleRepository;
import com.epam.esm.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class RoleService implements CrudService<Role> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll(Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        List<Role> roles = roleRepository.findAll(startPosition, itemsPerPage);
        return roles;
    }

    @Override
    public void save(Role role) {

    }

    @Override
    public Optional<Role> get(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void update(Role role) {

    }

    @Override
    public void delete(Long id) {

    }
}
