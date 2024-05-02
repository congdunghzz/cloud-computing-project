package com.cloudcomputing.cloudcomputing.business;


import com.cloudcomputing.cloudcomputing.ExceptionHandler.UnAuthorizedException;
import com.cloudcomputing.cloudcomputing.business.DTO.BusinessDTO;
import com.cloudcomputing.cloudcomputing.business.DTO.BusinessRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @GetMapping
    public ResponseEntity<List<BusinessDTO>> getALlUser (){
        List<BusinessDTO> businessDTOS = businessService.getAllUser();
        return new ResponseEntity<>(businessDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDTO> getById (@PathVariable Long id){
        BusinessDTO businessDTO = businessService.getById(id);

        if (businessDTO != null){
            return new ResponseEntity<>(businessDTO, HttpStatus.OK);
        }else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/myProfile")
    public ResponseEntity<BusinessDTO> getMyProfile (
            @CurrentSecurityContext(expression="authentication") Authentication authentication){

        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
            userId = user.getBusiness().getId();

        }

        BusinessDTO businessDTO;
        if (userId != null){
            businessDTO = businessService.getById(userId);
        }else {
            throw new UnAuthorizedException("You have not login yet");
        }

        if (businessDTO != null){
            return new ResponseEntity<>(businessDTO, HttpStatus.OK);
        }else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }



    @PutMapping("/myProfile")
    public ResponseEntity<BusinessDTO> editInfo(
            @CurrentSecurityContext(expression="authentication") Authentication authentication,
            @RequestBody BusinessRegisterRequest request){
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
            userId = user.getBusiness().getId();

        }
        BusinessDTO updatedUser;
        if (userId != null){
            updatedUser = businessService.editInfo(userId, request);
        }else {
            throw new UnAuthorizedException("You have not login yet");
        }

        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteUser(@PathVariable Long id){
        Map<String,String> result = new HashMap<>();
        HttpStatus status;
        if (businessService.deleteUser(id)){
            result.put("message", "deleted");
            status = HttpStatus.ACCEPTED;
        }else {
            result.put("message", "some thing went wrong");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(result, status);
    }




}
