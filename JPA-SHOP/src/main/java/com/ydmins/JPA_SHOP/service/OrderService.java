package com.ydmins.JPA_SHOP.service;

import com.ydmins.JPA_SHOP.domain.Delivery;
import com.ydmins.JPA_SHOP.domain.Member;
import com.ydmins.JPA_SHOP.domain.Order;
import com.ydmins.JPA_SHOP.domain.OrderItem;
import com.ydmins.JPA_SHOP.domain.item.Item;
import com.ydmins.JPA_SHOP.repository.ItemRepository;
import com.ydmins.JPA_SHOP.repository.MemberRepository;
import com.ydmins.JPA_SHOP.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /*
     * Order
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // Order Item
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // Order
        Order order = Order.createOrder(member, delivery, orderItem);

        // Order save
        orderRepository.save(order);
        return order.getId();
    }

    /*
     * Order Cancellation
     */
    @Transactional
    public void cancelOrder(Long orderid){
        Order order = orderRepository.findOne(orderid);
        order.cancel();
    }

    /*
     * Order Search
     */
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }
}
