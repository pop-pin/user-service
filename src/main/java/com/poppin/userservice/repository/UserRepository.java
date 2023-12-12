package com.poppin.userservice.repository;

import com.poppin.userservice.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernumber(String usernumber);
    @Query("SELECT a.role FROM User AS a  WHERE a.id = :userId")
    User findUserRole(@Param("userId") Long userId);

}
