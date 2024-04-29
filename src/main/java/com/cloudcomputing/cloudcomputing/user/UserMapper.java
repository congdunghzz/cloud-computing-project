package com.cloudcomputing.cloudcomputing.user;

import com.cloudcomputing.cloudcomputing.user.DTO.UserDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {
    public static UserDTO TransferToUserDTO (User user){
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getGender(),
                user.getDob(),
                user.getEmail(),
                user.getPhone(),
                user.getRole());
    }

    public static List<UserDTO> TransferToUserDTOs (List<User> userList){
        return userList.stream().map(UserMapper::TransferToUserDTO).toList();
    }
}
