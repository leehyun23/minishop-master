package com.javalab.boot.service;

import com.javalab.boot.domain.cart_item.Cart_item;
import com.javalab.boot.domain.order.Order;
import com.javalab.boot.domain.order.OrderRepository;
import com.javalab.boot.domain.order_item.Order_item;
import com.javalab.boot.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // 주문 생성
    public void createOrder(User user){
        Order order = new Order();
        order.setUser(user);
        orderRepository.save(order);
    }

    // 주문 내역 추가
    public void order(User user, List<Cart_item> cart_items){
        List<Order_item> order_items = new ArrayList<>(); // 주문 내역에 추가할 아이템 리스트
        for (Cart_item cart_item : cart_items){
            Order_item order_item = Order_item.createOrderItem(cart_item.getItem(),cart_item.getCount());
            order_items.add(order_item);
        }
        Order order = Order.createOrder(user,order_items);
        order.setPrice(order.getTotalPrice());
        orderRepository.save(order);
    }

    // 전체 주문 내역 조회
    public List<Order> orderList(){
        return orderRepository.findAll();
    }

    // 특정 주문 조회
    public Order orderView(Integer id){
        return orderRepository.findById(id).get();
    }

    // 주문 수정
    public void orderUpdate(Integer id, Order order){
        Order tempOrder = orderRepository.findById(id).get();
        tempOrder.setStatus(order.getStatus());
        orderRepository.save(tempOrder);
    }


    // 날짜별 총 합계금액 조회
    /*public List<Object[]> getTotalAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.getTotalAmountByDateRange(startDate, endDate);
    }*/

    // 날짜별 총 합계 금액 조회
    public int calculateTodayTotalAmount() {
        // OrderRepository에서 쿼리를 사용하여 오늘의 총 주문 금액을 계산
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS); // 현재 날짜의 0시 0분 0초
        LocalDateTime tomorrow = today.plusDays(1); // 다음 날의 0시 0분 0초



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        String formattedStartDate = today.format(formatter);
        String formattedEndDate = tomorrow.format(formatter);
        List<Object[]> result = orderRepository.getTotalAmountByDateRange(formattedStartDate, formattedEndDate);

        // 결과가 비어있으면 0을 반환, 그렇지 않으면 결과의 첫 번째 열을 반환 (totalAmount)
        return result.isEmpty() ? 0 : ((Number) result.get(0)[1]).intValue();
    }

    // 날짜별 총 구매 수량 조회
    public Integer getTodayTotalItemCount() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today.toLocalDate().atStartOfDay();
        LocalDateTime endDate = today.toLocalDate().plusDays(1).atStartOfDay();

        return orderRepository.getTotalItemCountByDateRange(startDate, endDate);
    }

    public void orderRefund(Integer id) {
    }
}
