package com.cloudcomputing.cloudcomputing.business;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.business.DTO.BusinessDTO;
import com.cloudcomputing.cloudcomputing.business.DTO.BusinessRegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessService implements UserDetailsService {

    private final BusinessRepository businessRepository;

    @Autowired
    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Business> user = businessRepository.findByEmail(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("Email is not found");
        }else {
            return new CustomUserDetail(user.get());
        }
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Optional<Business> user = businessRepository.findById(id);
        if (user.isEmpty()){
            throw new  UsernameNotFoundException(id.toString());
        }
        return new CustomUserDetail(user.get());
    }


    public BusinessDTO getById (Long userId){
        Optional<Business> user = businessRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("User with id: " +userId + " is not found");
        return BusinessMapper.TransferToBusinessDTO(user.get());
    }

    public List<BusinessDTO> getAllUser(){
        return BusinessMapper.TransferToBusinessDTOs(businessRepository.findAll());
    }




    public BusinessDTO editInfo(Long id, BusinessRegisterRequest request){
        Optional<Business> user = businessRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User with id: " +id+ " is not found");
        }

        user.get().setName(request.getName());
        user.get().setFoundingDate(request.getFoundingDate());
        user.get().setPhone(request.getPhone());

        Business updatedBusiness = businessRepository.save(user.get());

        return BusinessMapper.TransferToBusinessDTO(updatedBusiness);
    }

    public boolean deleteUser(Long id){
        Optional<Business> user = businessRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User with id: " +id+ " is not found");
        }
        businessRepository.deleteById(id);
        return true;
    }


}
