package com.niit.UserMovieService.service;


import com.niit.UserMovieService.domain.Movie;
import com.niit.UserMovieService.domain.User;
import com.niit.UserMovieService.exception.UserAlreadyExistsException;
import com.niit.UserMovieService.exception.UserNotFoundException;
import com.niit.UserMovieService.proxy.UserAuthProxy;
import com.niit.UserMovieService.repository.UserMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserMovieServiceImpl implements UserMovieService{

    private UserMovieRepository userMovieRepository;
    private UserAuthProxy userAuthProxy;

    @Autowired
    public UserMovieServiceImpl(UserMovieRepository userMovieRepository, UserAuthProxy userAuthProxy)
    {
       this.userMovieRepository = userMovieRepository;
       this.userAuthProxy = userAuthProxy;
    }

    @Override
    public User registerUser(User user) throws UserAlreadyExistsException {
        if (userMovieRepository.findById(user.getEmail()).isPresent())
        {
            throw new UserAlreadyExistsException();
        }
        ResponseEntity<?> response = userAuthProxy.saveUser(user);
        if (response.getStatusCodeValue()==201) {
            return userMovieRepository.save(user);
        }
        else {
            return null;
        }
    }

    @Override
    public User saveUserMovieToList(Movie movie, String email) throws UserNotFoundException {
//  Retrieving the user object with same email id. If the return optionalobject is empty then return
//  UserNotFoundException
        if (userMovieRepository.findById(email).isEmpty())
        {
            throw new UserNotFoundException();
        }
//  If the user found then store it as an reference variable
        User user = userMovieRepository.findByEmail(email);
//  Check does the user has any movie stored in the list
        if (user.getMovieList() == null)
        {
//  If not store the first movie object for the use
            user.setMovieList(Arrays.asList(movie));
        }
        else
        {
//  Control come here if the user has a list of favourite movies. Retrieve the list.
            List<Movie> movies = user.getMovieList();
//  Store the new movie object in the list.
            movies.add(movie);
//  Update the movieList property of the user object.
            user.setMovieList(movies);
        }
//  The save method will update the existing user object in the mongo.
        return userMovieRepository.save(user);
    }
}
