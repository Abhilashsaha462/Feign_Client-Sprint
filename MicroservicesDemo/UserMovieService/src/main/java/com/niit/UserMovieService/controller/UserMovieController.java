package com.niit.UserMovieService.controller;

import com.niit.UserMovieService.domain.Movie;
import com.niit.UserMovieService.domain.User;
import com.niit.UserMovieService.exception.UserAlreadyExistsException;
import com.niit.UserMovieService.exception.UserNotFoundException;
import com.niit.UserMovieService.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class UserMovieController {

    private UserMovieService userMovieService;
    private ResponseEntity<?> responseEntity;

    @Autowired
    public UserMovieController(UserMovieService userMovieService)
    {
        this.userMovieService = userMovieService;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws UserAlreadyExistsException {
        try {
            responseEntity = new ResponseEntity<>(userMovieService.registerUser(user), HttpStatus.CREATED);
        }
        catch (UserAlreadyExistsException e)
        {
            throw new UserAlreadyExistsException();
        }
        return responseEntity;
    }

    @PostMapping("/user/{email}/movies")
    public ResponseEntity<?> saveUserMovieToList(@RequestBody Movie movie, @PathVariable String email) throws UserNotFoundException {
        try{
            responseEntity =new ResponseEntity<>(userMovieService.saveUserMovieToList(movie,email), HttpStatus.CREATED);
        }
        catch (UserNotFoundException e)
        {
            throw new UserNotFoundException();
        }
        return responseEntity;
    }

}
