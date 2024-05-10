package com.cloudcomputing.cloudcomputing.order;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.UnAuthorizedException;
import com.cloudcomputing.cloudcomputing.order.DTO.OrderRequest;
import com.cloudcomputing.cloudcomputing.business.CustomUserDetail;
import com.cloudcomputing.cloudcomputing.business.BusinessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;
    @Autowired
    private BusinessService businessService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrderForAdmin (
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size){
        Page<Order> result = orderService.findAll(page.orElse(1), size.orElse(999));
        return ResponseEntity.ok(result);
    }
    @GetMapping("/business/{id}")
    public ResponseEntity<List<Order>> findByBusiness (@PathVariable Long id){
        List<Order> result = orderService.findAllByUser(id);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/myOrders")
    public ResponseEntity<List<Order>> getMyOrders (
            @CurrentSecurityContext(expression="authentication") Authentication authentication
    ){
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails){
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            userId = userDetail.getBusiness().getId();
        }
        List<Order> result;
        if (userId != null){

            result = orderService.findAllByUser(userId);
        }else {
            throw new UnAuthorizedException("Not login");
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById (@PathVariable Long id){
        Order order = orderService.findOneById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> generateOrder(@RequestBody OrderRequest orderRequest,
                                               @CurrentSecurityContext(expression="authentication") Authentication authentication){
        long userId = -1L;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
            userId = user.getBusiness().getId();
        }

        if (userId == -1L)
            throw new UnAuthorizedException("You are not permitted to do this action");
        Order order = orderService.createOrder(orderRequest, userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    public HttpStatus delete(
            @PathVariable Long id,
            @CurrentSecurityContext(expression="authentication") Authentication authentication
    ){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        long userId = -1L;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
            userId = user.getBusiness().getId();
        }

        if (userId == -1L)
            throw new UnAuthorizedException("You are not permitted to do this action");

        orderService.deleteOrderForBusiness(id, userId);
        httpStatus = HttpStatus.OK;


        return httpStatus;
    }
}
