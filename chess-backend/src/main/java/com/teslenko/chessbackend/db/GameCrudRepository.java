package com.teslenko.chessbackend.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.User;

public interface GameCrudRepository extends CrudRepository<Game, Long>{
	Optional<Game> findByCreator(User creator); 
	Optional<Game> findByOpponent(User opponent); 
}
