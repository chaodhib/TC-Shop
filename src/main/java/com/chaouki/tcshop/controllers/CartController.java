package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.controllers.dto.CartLine;
import com.chaouki.tcshop.entities.PurchasableItem;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class CartController implements Serializable {

    private Cart cart;

    @PostConstruct
    public void init(){
        cart = new Cart();
    }

    public void addItem(PurchasableItem purchasableItem) {
        cart.add(purchasableItem, 1);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",  "The selected item has been added to your cart") );
    }

    public void removeLine(CartLine cartLine) {
        cart.removeLine(cartLine);
    }

    public void incQuantity(CartLine cartLine) {
        cart.incQuantity(cartLine);
    }

    public void decQuantity(CartLine cartLine) {
        cart.decQuantity(cartLine);
    }

    public Cart getCart() {
        return cart;
    }
}
