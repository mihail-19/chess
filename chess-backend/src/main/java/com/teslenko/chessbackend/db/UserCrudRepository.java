package com.teslenko.chessbackend.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.teslenko.chessbackend.entity.User;

public interface UserCrudRepository extends CrudRepository<User, Long>{
	Optional<User> findByUsername(String username);
}
