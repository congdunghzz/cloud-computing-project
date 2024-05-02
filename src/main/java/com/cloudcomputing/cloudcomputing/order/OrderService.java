package com.cloudcomputing.cloudcomputing.order;

import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.ExceptionHandler.ProductStockException;
import com.cloudcomputing.cloudcomputing.ExceptionHandler.UnAuthorizedException;
import com.cloudcomputing.cloudcomputing.business.BusinessMapper;
import com.cloudcomputing.cloudcomputing.order.DTO.OrderDetailRequestDTO;
import com.cloudcomputing.cloudcomputing.order.DTO.OrderRequest;
import com.cloudcomputing.cloudcomputing.orderDetail.OrderDetail;
import com.cloudcomputing.cloudcomputing.product.Product;
import com.cloudcomputing.cloudcomputing.product.ProductRepository;
import com.cloudcomputing.cloudcomputing.business.Business;
import com.cloudcomputing.cloudcomputing.business.BusinessRepository;
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
    private final BusinessRepository businessRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        BusinessRepository businessRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
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

    public List<Order> findAllByUser(Long businessId){
        return orderRepository.findByBusinessIdOrderByOrderDateDesc(businessId);
    }




    public Order createOrder(OrderRequest request){

        // check user if it is present
        Optional<Business> business = businessRepository.findById(request.getBusinessId());
        if (business.isEmpty())
            throw new NotFoundException("Business with id: " +request.getBusinessId()+" was not found");

        Order newOrder = new Order();
        newOrder.setBusiness(business.get());
        List<OrderDetail> details = new ArrayList<>();

        List<Product> products = new ArrayList<>();
        double totalCost = 0;

        // get order detail list

        for (OrderDetailRequestDTO item : request.getOrderDetailRequestDTOs()){
            Optional<Product> product = productRepository.findById(item.productId());

            //check product if it is present
            if (product.isEmpty())
                throw new NotFoundException("Product with id: " +item.productId()+ " is not found");
            if (!product.get().getBusiness().getName().equals(business.get().getName())){
                throw new NotFoundException("Business: " +business.get().getName()+ " have no product with name: " + product.get().getName());            }

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



    public void deleteOrderForBusiness(Long orderId, Long businessId){
        Optional<Business> business = businessRepository.findById(businessId);
        if (business.isEmpty()) throw new NotFoundException("Business with id: " +businessId+ " is not found");
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty())
            throw new NotFoundException("Order with id: " +orderId+ " is not found");
        if (!order.get().getBusiness().getName().equals(business.get().getName())){
            throw new UnAuthorizedException("You are not permitted to delete this order");
        }
        try{
            orderRepository.deleteById(orderId);
        }catch (Exception e){
            throw new NotFoundException("Cant delete this order: " + e);
        }
    }


}
