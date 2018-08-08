package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.services.OrderCreationStatus;
import com.chaouki.tcshop.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private Integer characterId;
    private String paymentDetails;

    @Autowired
    private CartController cartController;

    @Autowired
    private OrderService orderService;

    public void startOrder() {
        Cart cart = cartController.getCart();
        if(cart.getCartLines().isEmpty()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpStatus.BAD_REQUEST.value(), "Either the resource does not exist or you are unauthorized to access it");
                return;
            } catch (IOException e) {
                LOGGER.error("exception thrown when redirecting user to error message", e);
            }
        }

        OrderCreationStatus creationStatus = orderService.createOrder(characterId, paymentDetails, cart);

        FacesContext context = FacesContext.getCurrentInstance();
        switch (creationStatus) {
            case SUCCESS:
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",  "Your order will be fulfilled as soon as possible") );

                // clean up
                cart.getCartLines().clear();
                paymentDetails = null;
                characterId = null;
                break;
            case PAYMENT_FAILED:
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",  "The payment validation failed") );
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }
}
