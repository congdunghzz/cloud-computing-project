package com.cloudcomputing.cloudcomputing.order;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.UnAuthorizedException;
import com.cloudcomputing.cloudcomputing.order.DTO.OrderRequest;
import com.cloudcomputing.cloudcomputing.user.CustomUserDetail;
import com.cloudcomputing.cloudcomputing.user.UserService;
import com.cloudcomputing.cloudcomputing.user.enums.UserRole;
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
    private UserService userService;
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
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> findByUser (@PathVariable Long id){
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
            userId = userDetail.getUser().getId();
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
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
            userId = user.getUser().getId();

        }

        Order order;

        if (userId != null){
            order = orderService.createOrder(orderRequest, userId);
        }else {
            throw new UnAuthorizedException("You is not permitted to do this action");
        }
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
            userId = user.getUser().getId();
        }

        if (userId == -1L)
            throw new UnAuthorizedException("You are not permitted to do this action");

        if (userService.getById(userId).role() == UserRole.ROLE_BUSINESS){
            orderService.deleteOrderForBusiness(id);
            httpStatus = HttpStatus.OK;
        }else {
            throw new UnAuthorizedException("You are not permitted to do this action");
        }

        return httpStatus;
    }
}
