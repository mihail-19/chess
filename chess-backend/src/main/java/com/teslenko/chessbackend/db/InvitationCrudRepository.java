package com.teslenko.chessbackend.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.teslenko.chessbackend.entity.Invitation;

public interface InvitationCrudRepository extends CrudRepository<Invitation, Long>{
}
