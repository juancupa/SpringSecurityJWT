package com.example.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.models.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity,Long> {

}
