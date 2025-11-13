package com.project.shopapp.controllers;


import com.project.shopapp.dtos.*;
import com.project.shopapp.models.User;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    //can we register an "admin" vs "user"
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result){
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors() // Lấy danh sách lỗi
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList()); // Chuyển thành List<String>

                return ResponseEntity.badRequest().body(errorMessages);
            }
            // ktr sai mat khau nhap lai
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword()))
            {
                return ResponseEntity.badRequest().body("Password does not match");
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(user);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (
            @Valid @RequestBody UserloginDTO userloginDTO)
    {
        // kiem tra thong tin danw nhap vaf sinh token
        try {
            // lay token xac thục từ UserService
            String token = userService.login(userloginDTO.getPhoneNumber(), userloginDTO.getPassword());
            // tra ve token trong reposen
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

}
