package com.cloudcomputing.cloudcomputing.authentication;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.ExistException;
import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.config.jwtConfig.JwtTokenProvider;
import com.cloudcomputing.cloudcomputing.user.CustomUserDetail;
import com.cloudcomputing.cloudcomputing.user.DTO.UserRegisterRequest;
import com.cloudcomputing.cloudcomputing.user.User;
import com.cloudcomputing.cloudcomputing.user.UserRepository;
import com.cloudcomputing.cloudcomputing.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthenticationResponse createUser(UserRegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new ExistException("user with email: " + request.getEmail()+" have existed");
        }
        User createdUser;
        User user = User
                .builder()
                .name(request.getName())
                .dob(request.getDob())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_CUSTOMER)
                .build();


            createdUser = userRepository.save(user);

            if (createdUser != null){

                String token = jwtTokenProvider.generateToken(new CustomUserDetail(createdUser));
                return new AuthenticationResponse(token, createdUser.getRole().name());
            }else {
                throw new NotFoundException("Sign up falsely");
            }

    }

    public AuthenticationResponse login (LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.userEmail(),
                        request.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
        return new AuthenticationResponse(token, ((CustomUserDetail) authentication.getPrincipal()).getUser().getRole().name());
    }

}
