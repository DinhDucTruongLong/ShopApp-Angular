package com.project.shopapp.services;

import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.PermissionDenyException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        //regiter user
        String phoneNumber = userDTO.getPhoneNumber();
        // kiểm tra xem số điện thoại đã tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exsits");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("You cannot register an admin account");
        }
        //convert form userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())// password da ma hoa
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                        .build();

        newUser.setRole(role);
        //Kiem tra neu co accountId khong yeu cau mat kharu
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();
            // ma hoa mat khau
            String encodedPassword = passwordEncoder.encode(password);
            // se noi den trong phan spring security
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    public String login(String phoneNumber, String password) throws Exception{
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phoneNumber / password");
        }
      //  return optionalUser.get();// muon tra ve JWT token?
        User existingUser = optionalUser.get();
        //check password ko phai login fb gg thi moi ktr
        if(existingUser.getFacebookAccountId()==0
                && existingUser.getGoogleAccountId()==0){
                if(!passwordEncoder.matches(password, existingUser.getPassword())){
                    throw new BadCredentialsException("Wrong phone number or password");
                }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,
                existingUser.getAuthorities()
        );
        //authenticate with java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}
