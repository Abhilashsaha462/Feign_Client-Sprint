package com.niit.authenticationservice.AuthenticationService.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.niit.authenticationservice.AuthenticationService.domain.User;
import com.niit.authenticationservice.AuthenticationService.exception.UserNotFoundException;
import com.niit.authenticationservice.AuthenticationService.service.SecurityTokenGenerator;
import com.niit.authenticationservice.AuthenticationService.service.UserService;
import lombok.SneakyThrows;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/")
public class UserController {

    private ResponseEntity responseEntity;
    private UserService userService;
    private SecurityTokenGenerator securityTokenGenerator;

    @Autowired
    public UserController(UserService userService, SecurityTokenGenerator securityTokenGenerator) {
        this.userService = userService;
                this.securityTokenGenerator = securityTokenGenerator;
    }

//    @SneakyThrows
    @PostMapping("/login")
    @HystrixCommand(fallbackMethod = "fallbackLogin",commandKey = "loginKey",groupKey = "login")
    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    public ResponseEntity<?> loginUser(@RequestBody User user) throws InvalidCredentialsException {
       User retrievedUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        System.out.println("Application paused");
        try {
            Thread.sleep(70000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (retrievedUser==null)
       {
           throw new InvalidCredentialsException();
       }
       Map<String,String> map = securityTokenGenerator.generateToken(user);
       return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<?> fallbackLogin(@RequestBody User user) throws InvalidCredentialsException {
        String msg = "login failed";
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity saveUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return responseEntity = new ResponseEntity("User Created", HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/userservice/users")
    public ResponseEntity getAllUsers(HttpServletRequest request) {
        List<User> list = userService.getAllUsers();
        responseEntity = new ResponseEntity(list, HttpStatus.OK);
        return responseEntity;
    }

}
