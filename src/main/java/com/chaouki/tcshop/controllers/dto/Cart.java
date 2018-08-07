package com.chaouki.tcshop.controllers.dto;

import com.chaouki.tcshop.entities.ItemTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartLine> cartLines;

    public Cart() {
        this.cartLines = new ArrayList<>();
    }

    public List<CartLine> getCartLines() {
        return cartLines;
    }

    public void add(ItemTemplate itemTemplateEntry, int addQuantity, BigDecimal itemPricePerUnit) {
        for (CartLine cartLine : cartLines) {
            if(cartLine.getItem().getEntry().equals(itemTemplateEntry.getEntry())) {
                cartLine.setQuantity(cartLine.getQuantity() + addQuantity);
                return;
            }
        }

        CartLine cartLine = new CartLine();
        cartLine.setItem(itemTemplateEntry);
        cartLine.setQuantity(addQuantity);
        cartLine.setPricePerUnit(itemPricePerUnit);
        cartLines.add(cartLine);
    }

    public void removeLine(CartLine cartLine) {
        cartLines.remove(cartLine);
    }

    public void incQuantity(CartLine cartLine) {
        cartLine.setQuantity(cartLine.getQuantity()+1);
    }


    public void decQuantity(CartLine cartLine) {
        if(cartLine.getQuantity() == 1) {
            cartLines.remove(cartLine);
        } else {
            cartLine.setQuantity(cartLine.getQuantity() - 1);
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartLine cartLine : cartLines) {
            total = total.add(cartLine.getSubtotal());
        }

        return total;
    }
}
