package com.cloudcomputing.cloudcomputing.product;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.business.Business;
import com.cloudcomputing.cloudcomputing.business.BusinessRepository;
import com.cloudcomputing.cloudcomputing.category.Category;
import com.cloudcomputing.cloudcomputing.category.CategoryRepository;
import com.cloudcomputing.cloudcomputing.product.DTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BusinessRepository businessRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, BusinessRepository businessRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.businessRepository = businessRepository;
    }

    public Page<ProductDTO> getAllProduct(int page, int size){

        Page<Product> products = productRepository.findAll(PageRequest.of(page-1, size));
        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOPage(products);
        }else {
            return null;
        }
    }

    public Page<ProductDTO> getProductByCategory(String name, int page, int size){
        Page<Product> products;
        Pageable pageable = PageRequest.of(page-1, size);
        if (name.isBlank()) {
            products = productRepository.findAll(pageable);
        } else{
            Optional<Category> category = categoryRepository.findByName(name);
            if (category.isEmpty()) throw new NotFoundException("Category with name:"  +name+  "is not found");

            products = productRepository.findAllByCategory(category.get(), pageable);
        }
        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOPage(products);
        }else {
            return null;
        }
    }
    public Page<ProductDTO> getProductByBusiness(String name, int page, int size){
        Page<Product> products;
        Pageable pageable = PageRequest.of(page-1, size);
        if (name.isBlank()) {
            products = productRepository.findAll(pageable);
        } else{
            Optional<Business> business = businessRepository.findByName(name);
            if (business.isEmpty()) throw new NotFoundException("Business with name:"  +name+  "is not found");
            products = productRepository.findAllByBusiness(business.get(), pageable);
        }

        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOPage(products);
        }else {
            return null;
        }
    }

    public Page<ProductDTO> searchForName (String name, int page, int size){

        Page<Product> products;
        Pageable pageable = PageRequest.of(page-1,size);
        if (name.isBlank()){
            products = productRepository.findAll(pageable);
        }else {
            products = productRepository.findAllByNameStartingWithIgnoreCase(name, pageable);
        }
        return ProductMapper.TransferToProductDTOPage(products);
    }

    public ProductDTO findOneById(Long id){
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) throw new NotFoundException("Product with id: " +id+ " is not found");
        return ProductMapper.TransferToProductDTO(product.get());
    }

}
