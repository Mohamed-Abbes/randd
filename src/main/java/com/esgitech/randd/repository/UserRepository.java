package com.esgitech.randd.repository;

import com.esgitech.randd.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
