package com.ydmins.JPA_SHOP.controller;

import com.ydmins.JPA_SHOP.domain.Member;
import com.ydmins.JPA_SHOP.domain.Order;
import com.ydmins.JPA_SHOP.domain.item.Item;
import com.ydmins.JPA_SHOP.repository.OrderSearch;
import com.ydmins.JPA_SHOP.service.ItemService;
import com.ydmins.JPA_SHOP.service.MemberService;
import com.ydmins.JPA_SHOP.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){
        orderService.order(memberId, itemId, count);
        return "redirect:/order";
    }


    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){
//        List<Order> orders = orderService.findOrders(orderSearch);
//        model.addAttribute("orders", orders);
        return "order/orderList";
    }
}
