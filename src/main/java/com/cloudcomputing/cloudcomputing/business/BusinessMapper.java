package com.cloudcomputing.cloudcomputing.business;

import com.cloudcomputing.cloudcomputing.business.DTO.BusinessDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessMapper {
    public static BusinessDTO TransferToBusinessDTO (Business business){
        return new BusinessDTO(
                business.getId(),
                business.getName(),
                business.getFoundingDate(),
                business.getEmail(),
                business.getPhone());
    }

    public static List<BusinessDTO> TransferToBusinessDTOs (List<Business> businessList){
        return businessList.stream().map(BusinessMapper::TransferToBusinessDTO).toList();
    }
}
