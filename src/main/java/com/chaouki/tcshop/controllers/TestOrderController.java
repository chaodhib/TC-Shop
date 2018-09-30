package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.services.OrderService;
import com.chaouki.tcshop.services.PurchasableItemService;
import com.chaouki.tcshop.services.StripePaymentDetailsStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PurchasableItemService purchasableItemService;

    @PostMapping("/test-order")
    public String testOrder(@RequestBody OrderDTO orderDTO) {
        Cart cart = new Cart();
        for (CartLineDTO cartLineDTO : orderDTO.getCart().getCartLineList()) {
            cart.add(purchasableItemService.findById(cartLineDTO.purchasableItemId).get(), cartLineDTO.quantity);
        }
        return orderService.createOrder(orderDTO.getCharacterId(), new StripePaymentDetailsStub(), cart).getStatus().getLabel();
    }

    private static class OrderDTO {
        private Integer characterId;
        private CartDTO cart;

        public Integer getCharacterId() {
            return characterId;
        }

        public void setCharacterId(Integer characterId) {
            this.characterId = characterId;
        }

        public CartDTO getCart() {
            return cart;
        }

        public void setCart(CartDTO cart) {
            this.cart = cart;
        }
    }

    private static class CartDTO {
        List<CartLineDTO> cartLineList;

        public List<CartLineDTO> getCartLineList() {
            return cartLineList;
        }

        public void setCartLineList(List<CartLineDTO> cartLineList) {
            this.cartLineList = cartLineList;
        }
    }

    private static class CartLineDTO {
        private Integer quantity;
        private Integer purchasableItemId;

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getPurchasableItemId() {
            return purchasableItemId;
        }

        public void setPurchasableItemId(Integer purchasableItemId) {
            this.purchasableItemId = purchasableItemId;
        }
    }
}
