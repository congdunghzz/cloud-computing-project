package com.cloudcomputing.cloudcomputing.product;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.business.Business;
import com.cloudcomputing.cloudcomputing.business.BusinessRepository;


import com.cloudcomputing.cloudcomputing.product.DTO.ProductDTO;
import com.cloudcomputing.cloudcomputing.product.DTO.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BusinessRepository businessRepository;
    private final ProductImageService productImageService;

    @Autowired
    public ProductService(ProductRepository productRepository, BusinessRepository businessRepository, ProductImageService productImageService) {
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.productImageService = productImageService;
    }

    public Page<ProductDTO> getAllProduct(int page, int size){

        Page<Product> products = productRepository.findAll(PageRequest.of(page-1, size));
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

    public ProductDTO addProduct(Long businessId, ProductRequest request) throws IOException {

        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .stock(request.stock())
                .build();


        Optional<Business> business = businessRepository.findById(businessId);
        // if brand is present, add product into it. if it is not, break the function
        if (business.isPresent()){
            product.setBusiness(business.get());
        }else {
            throw new NotFoundException("Business with id: " +businessId+ " is not found");
        }
        // if images are present, add them
        if (request.image() != null){
            product.setImage(productImageService.addImage(request.image()));
        }
        Product createdProduct = productRepository.save(product);
        return ProductMapper.TransferToProductDTO(createdProduct);
    }


    public ProductDTO editProduct(Long id, ProductRequest productRequest, Long businessId) throws IOException {
        ProductDTO result;
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()){
            throw new NotFoundException("Product with id: " +id+ " is not found");
        }

        Optional<Business> business = businessRepository.findById(businessId);

        if (!product.get().getBusiness().getName().equals(business.get().getName())) {
            throw new NotFoundException("Business: " + business.get().getName() + " have no product with name: " + product.get().getName());
        }

        //Update name, price.
        Product updatedProduct = product.get();
        updatedProduct.setName(productRequest.name());
        updatedProduct.setPrice(productRequest.price());
        updatedProduct.setStock(productRequest.stock());
        // check category if it is present

        if (productRequest.image() != null){
            if (productImageService.deleteImg(product.get().getImage())){
                updatedProduct.setImage(productImageService.addImage(productRequest.image()));
            }
        }
        result = ProductMapper.TransferToProductDTO(productRepository.save(updatedProduct));
        return result;
    }




    public boolean deleteProduct (Long id, Long businessId){
        Optional<Product> product =productRepository.findById(id);
        if (product.isEmpty())
            throw new NotFoundException("Product with id: " +id+ " is not found");

        Optional<Business> business = businessRepository.findById(businessId);
        if (!product.get().getBusiness().getName().equals(business.get().getName())) {
            throw new NotFoundException("Business: " + business.get().getName() + " have no product with name: " + product.get().getName());
        }

        if(product.get().getImage() != null) {
                try {
                    productImageService.deleteImg(product.get().getImage());
                } catch (IOException e) {
                    throw new NotFoundException("Image cant not be removed");
                }
        }
        try{
            productRepository.deleteById(id);
            return true;
        }catch (Exception e ){
            throw new DataIntegrityViolationException("This data have some relation with other object");
        }

    }

}
