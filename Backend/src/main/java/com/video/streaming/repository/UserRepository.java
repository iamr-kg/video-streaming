package com.video.streaming.repository;

import com.video.streaming.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
Optional<User> findByEmailAddress(String email);

Optional<User> findBySub(String sub);
}
