package com.example.Viazmus.repos;

import com.example.Viazmus.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User,Long> {

    User findByUsername(String username);
}
