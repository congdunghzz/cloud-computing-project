package com.cloudcomputing.cloudcomputing.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;

    @Autowired
    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }
}
