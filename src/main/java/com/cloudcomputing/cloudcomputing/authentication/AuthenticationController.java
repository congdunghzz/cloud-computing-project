package com.cloudcomputing.cloudcomputing.authentication;

import com.cloudcomputing.cloudcomputing.business.DTO.BusinessRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerNewUser (@RequestBody BusinessRegisterRequest request){

        System.out.println("Register controller: " + request);
        AuthenticationResponse response = authenticationService.createBusiness(request);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody LoginRequest request){
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

}
