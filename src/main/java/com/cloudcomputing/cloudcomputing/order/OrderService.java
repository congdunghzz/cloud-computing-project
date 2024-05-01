package com.cloudcomputing.cloudcomputing.order;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.ExceptionHandler.ProductStockException;
import com.cloudcomputing.cloudcomputing.order.DTO.OrderDetailRequestDTO;
import com.cloudcomputing.cloudcomputing.order.DTO.OrderRequest;
import com.cloudcomputing.cloudcomputing.orderDetail.OrderDetail;
import com.cloudcomputing.cloudcomputing.product.Product;
import com.cloudcomputing.cloudcomputing.product.ProductRepository;
import com.cloudcomputing.cloudcomputing.user.User;
import com.cloudcomputing.cloudcomputing.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Order findOneById (Long orderId){
        Optional<Order> result = orderRepository.findById(orderId);
        if (result.isEmpty())
            throw new NotFoundException("Order with id: " +orderId+ " is not found");
        return result.get();
    }

    public Page<Order> findAll (int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findAllByOrderByOrderDateDesc(pageable);
    }

    public List<Order> findAllByUser(Long userId){
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }




    public Order createOrder(OrderRequest request, Long userId){

        // check user if it is present
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException("User with id: " +userId+" was not found");

        Order newOrder = new Order();
        newOrder.setUser(user.get());
        List<OrderDetail> details = new ArrayList<>();

        List<Product> products = new ArrayList<>();
        double totalCost = 0;

        // get order detail list

        for (OrderDetailRequestDTO item : request.getOrderDetailRequestDTOs()){
            Optional<Product> product = productRepository.findById(item.productId());

            //check product if it is present
            if (product.isEmpty())
                throw new NotFoundException("Product with id: " +item.productId()+ " is not found");
            products.add(product.get());

            totalCost += product.get().getPrice() * item.quantity();
            details.add(OrderDetail
                    .builder()
                    .productId(product.get().getId())
                    .productName(product.get().getName())
                    .productPrice(product.get().getPrice())
                    .quantity(item.quantity())
                    .order(newOrder)
                    .cost(product.get().getPrice() * item.quantity())
                    .build());
        }
        newOrder.setName(request.getName());
        newOrder.setAddress(request.getAddress());
        newOrder.setPhone(request.getPhone());
        newOrder.setOrderDate(new Date(System.currentTimeMillis()));
        newOrder.setTotalCost(totalCost);
        newOrder.setOrderDetails(details);

        user.get().getOrders().add(newOrder);

        products.forEach(product -> {
            int quantityOfItem = 0;

            for (OrderDetailRequestDTO item : request.getOrderDetailRequestDTOs()) {

                if (product.getId().equals(item.productId())){
                    if (product.getStock() < item.quantity()){
                        throw new ProductStockException("The product: " + product.getName()+ " is out of stock");
                    }
                    quantityOfItem = item.quantity();
                    break;
                }
            }
            product.setStock(product.getStock() - quantityOfItem);
            productRepository.save(product);
        });
        return orderRepository.save(newOrder);
    }



    public void deleteOrderForBusiness(Long orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty())
            throw new NotFoundException("Order with id: " +orderId+ " is not found");
        try{
            orderRepository.deleteById(orderId);
        }catch (Exception e){
            throw new NotFoundException("Order with id: " +orderId+ " is not found");
        }
    }


}
