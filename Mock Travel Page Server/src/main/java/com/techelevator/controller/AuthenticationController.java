package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.LoginDto;
import com.techelevator.model.LoginResponseDto;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;
import com.techelevator.security.jwt.TokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * AuthenticationController is a class used for handling requests to authenticate Users.

 * It depends on an instance of a UserDao for retrieving and storing user data. This is provided
 * through dependency injection.
 */
@RestController
@CrossOrigin
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
    }

    //LOGIN USER
    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public LoginResponseDto login(@Valid @RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            if(authentication.isAuthenticated()){
                String jwt = tokenProvider.createToken(authentication, false);
                User user = userDao.getUserByUsername(loginDto.getUsername());
                return new LoginResponseDto(jwt, user);
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login error.");
        }
    }

    //REGISTER USER
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public User register(@Valid @RequestBody RegisterUserDto newUser) {

        if(!newUser.isPasswordsMatch()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and confirmed password do not match");
        }

        try {
            User user = userDao.createUser(new User(newUser.getUsername(), newUser.getPassword(), newUser.getAddress(),
                    newUser.getCity(), newUser.getStateCode(), newUser.getZIP(), newUser.getRole()));
            return user;
        }
        catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User has been created already.");
        }
    }

    //VIEW USERS AS AN ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.GET)
    public User get(@PathVariable int userId) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            return user;
        }
    }

}


