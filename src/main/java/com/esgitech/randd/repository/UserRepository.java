package com.esgitech.randd.repository;

import com.esgitech.randd.dtos.Rs;
import com.esgitech.randd.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    User findUserById(Long id);

//    List<User> findUserByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String lastName, String email);
@Query("SELECT u FROM User u WHERE " +
        "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(u.userName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
List<User> searchUsers(@Param("searchTerm") String searchTerm);
}
