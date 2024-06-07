package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.models.UserEntity;

public interface UserRepository  extends CrudRepository<UserEntity, Long>{
	
	Optional<UserEntity> findByUsername(String username);
	
	
	@Query("select u from UserEntity u where u.username=?1")
	Optional<UserEntity> getname(String username);
	
	
	

}
