package com.letrasypapeles.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.service.UserService;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/user")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<BaseUser> getAllUsers() {
    List<BaseUser> users = userService.findAllUsers();
    if (users.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
}
