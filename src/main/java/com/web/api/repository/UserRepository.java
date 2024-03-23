package com.web.api.repository;

import com.web.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    List<User> findByFirstname(String firstname);

    List<User> findAllByDeleted(boolean deleted);

    Optional<User> findByUsernameAndDeleted(String username,boolean deleted);

    Optional<User> findByIdAndDeleted(Long id, boolean deleted);

    boolean existsByIdAndDeleted(Long id, boolean deleted);
}
