package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.UserLoginModel;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginModel, Long>{

	public Optional<UserLoginModel> findByUserEmail(String userEmail);
}
