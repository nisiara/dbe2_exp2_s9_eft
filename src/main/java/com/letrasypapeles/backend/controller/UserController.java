package com.letrasypapeles.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
  public List<BaseUser> obtenerTodos() {
    return userService.obtenerTodos();
  }
  
}
