package com.cloudcomputing.cloudcomputing.user;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.user.DTO.UserDTO;
import com.cloudcomputing.cloudcomputing.user.DTO.UserRegisterRequest;
import com.cloudcomputing.cloudcomputing.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("Email is not found");
        }else {
            return new CustomUserDetail(user.get());
        }
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new  UsernameNotFoundException(id.toString());
        }
        return new CustomUserDetail(user.get());
    }


    public UserDTO getById (Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("User with id: " +userId + " is not found");
        return UserMapper.TransferToUserDTO(user.get());
    }

    public List<UserDTO> getAllUser(){
        return UserMapper.TransferToUserDTOs(userRepository.findAll());
    }




    public UserDTO editInfo(Long id, UserRegisterRequest request){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User with id: " +id+ " is not found");
        }

        user.get().setName(request.getName());
        user.get().setDob(request.getDob());
        user.get().setGender(request.getGender());
        user.get().setPhone(request.getPhone());

        User updatedUser = userRepository.save(user.get());

        return UserMapper.TransferToUserDTO(updatedUser);
    }

    public boolean deleteUser(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User with id: " +id+ " is not found");
        }
        userRepository.deleteById(id);
        return true;
    }

    public UserDTO changeUserRoleToBusiness (Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id: "+userId+"is not found or this user is already a admin");
        }
        user.get().setRole(UserRole.ROLE_BUSINESS);
        User updatedUser = userRepository.save(user.get());
        return UserMapper.TransferToUserDTO(updatedUser);
    }
}
