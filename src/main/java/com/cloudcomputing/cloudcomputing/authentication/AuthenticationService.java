package com.cloudcomputing.cloudcomputing.authentication;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.ExistException;
import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.config.jwtConfig.JwtTokenProvider;
import com.cloudcomputing.cloudcomputing.business.Business;
import com.cloudcomputing.cloudcomputing.business.CustomUserDetail;
import com.cloudcomputing.cloudcomputing.business.DTO.BusinessRegisterRequest;
import com.cloudcomputing.cloudcomputing.business.BusinessRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthenticationService(BusinessRepository businessRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.businessRepository = businessRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthenticationResponse createBusiness(BusinessRegisterRequest request){
        if (businessRepository.existsByEmail(request.getEmail())){
            throw new ExistException("Business with email: " + request.getEmail()+" have existed");
        }
        Business createdBusiness;
        Business business = Business
                .builder()
                .name(request.getName())
                .foundingDate(request.getFoundingDate())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();


        createdBusiness = businessRepository.save(business);

            if (createdBusiness != null){

                String token = jwtTokenProvider.generateToken(new CustomUserDetail(createdBusiness));
                return new AuthenticationResponse(token, createdBusiness.getId());
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
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(user);

        return new AuthenticationResponse(token, user.getBusiness().getId());
    }

}
