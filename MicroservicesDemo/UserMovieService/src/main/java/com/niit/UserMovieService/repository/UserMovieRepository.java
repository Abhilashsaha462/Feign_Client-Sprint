package com.niit.UserMovieService.repository;

import com.niit.UserMovieService.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMovieRepository extends MongoRepository<User,String> {

    User findByEmail(String email);

}
