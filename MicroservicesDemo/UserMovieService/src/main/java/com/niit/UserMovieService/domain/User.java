package com.niit.UserMovieService.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
    @Id
    private String email;
//    @Transient
    private String password;
    private String username;
    private String phoneNumber;
    @Transient
    private int userId;
    private List<Movie> movieList;

}

