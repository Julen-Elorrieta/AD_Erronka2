package com.elorserv.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elorserv.model.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	Optional<Users> findByEmail(String email);
}
