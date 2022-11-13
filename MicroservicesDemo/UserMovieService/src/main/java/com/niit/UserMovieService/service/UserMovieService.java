package com.niit.UserMovieService.service;

import com.niit.UserMovieService.domain.Movie;
import com.niit.UserMovieService.domain.User;
import com.niit.UserMovieService.exception.UserAlreadyExistsException;
import com.niit.UserMovieService.exception.UserNotFoundException;


public interface UserMovieService {

    public User registerUser(User user) throws UserAlreadyExistsException;

    public User saveUserMovieToList(Movie movie, String email) throws UserNotFoundException;


}
