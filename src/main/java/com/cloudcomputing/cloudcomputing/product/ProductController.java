package com.cloudcomputing.cloudcomputing.product;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.UnAuthorizedException;
import com.cloudcomputing.cloudcomputing.business.CustomUserDetail;
import com.cloudcomputing.cloudcomputing.product.DTO.ProductDTO;
import com.cloudcomputing.cloudcomputing.product.DTO.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    public Long getUserId(Authentication authentication){
        long userId = -1L;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
            userId = user.getBusiness().getId();
        }

        if (userId == -1L)
            throw new UnAuthorizedException("You are not permitted to do this action");

        return userId;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProduct(
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size
    ){
        return new ResponseEntity<>(
                productService.getAllProduct(page.orElse(1), size.orElse(1000)),
                HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProduct(
            @RequestParam(value = "key", required = false) String name,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size
    ){
        return new ResponseEntity<>(
                productService.searchForName(name ,page.orElse(1), size.orElse(1000)),
                HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findOne(@PathVariable Long id) {
        ProductDTO result = productService.findOneById(id);
        if (result != null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/business")
    public ResponseEntity<Page<ProductDTO>> getAllProductByBrand(@RequestParam(value = "name", required = false) String name,
                                                                 @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                                 @RequestParam(value = "size", required = false) Optional<Integer> size
    ){
        return new ResponseEntity<>(
                productService.getProductByBusiness(name, page.orElse(1), size.orElse(1000)),
                HttpStatus.OK);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> addProduct(@ModelAttribute ProductRequest request,
                                                 @CurrentSecurityContext(expression="authentication") Authentication authentication) throws IOException {
        long userId = getUserId(authentication);
        ProductDTO result = productService.addProduct(userId, request);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @ModelAttribute ProductRequest productRequest,
                                                    @CurrentSecurityContext(expression="authentication") Authentication authentication) throws IOException {
        Long businessId = getUserId(authentication);
        ProductDTO result = productService.editProduct(id,productRequest, businessId);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                @CurrentSecurityContext(expression="authentication") Authentication authentication) {
        Long businessId = getUserId(authentication);
        boolean result = productService.deleteProduct(id, businessId);
        if (result){
            return new ResponseEntity<>("delete successfully", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Encounter an error", HttpStatus.BAD_REQUEST);
        }
    }
}
