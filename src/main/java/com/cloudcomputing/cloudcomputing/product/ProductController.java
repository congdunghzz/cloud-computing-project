package com.cloudcomputing.cloudcomputing.product;

import com.cloudcomputing.cloudcomputing.product.DTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
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
}
