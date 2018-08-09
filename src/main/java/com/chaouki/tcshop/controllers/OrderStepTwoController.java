package com.chaouki.tcshop.controllers;

import com.chaouki.tcshop.controllers.dto.Cart;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.services.CreateOrderResult;
import com.chaouki.tcshop.services.OrderCreationStatus;
import com.chaouki.tcshop.services.OrderService;
import com.chaouki.tcshop.services.StripePaymentDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

@ManagedBean
@RequestScoped
public class OrderStepTwoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStepTwoController.class);

    @Autowired
    private OrderStepOneController orderStepOneController;

    @Autowired
    private CartController cartController;

    @Autowired
    private OrderService orderService;

    private String outcome;

    @PostConstruct
    public void initMyBean(){
        Map<String,String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(requestParams.containsKey("stripeToken") && requestParams.containsKey("stripeTokenType") && requestParams.containsKey("stripeEmail")) {
            String stripeToken = requestParams.get("stripeToken");
            String stripeTokenType = requestParams.get("stripeTokenType");
            String stripeEmail = requestParams.get("stripeEmail");

            Character character = orderStepOneController.getCharacter();
            Cart cart = cartController.getCart();
            StripePaymentDetails stripePaymentDetails = new StripePaymentDetails(stripeToken, stripeTokenType, stripeEmail);

            if(cart.getCartLines().isEmpty()) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpStatus.BAD_REQUEST.value(), "Either the resource does not exist or you are unauthorized to access it");
                    return;
                } catch (IOException e) {
                    LOGGER.error("exception thrown when redirecting user to error message", e);
                }
            }

            CreateOrderResult createOrderResult = orderService.createOrder(character.getId(), stripePaymentDetails, cart);

            outcome = createOrderResult.getStatus().getLabel();
            switch (createOrderResult.getStatus()) {
                case SUCCESS:
                    // clean up
                    cart.getCartLines().clear();
                    orderStepOneController.setCharacterId(null);
                    orderStepOneController.setCharacter(null);
                    break;
                case PAYMENT_FAILED:
                    outcome += ". Message from payment system: " + createOrderResult.getErrorMessage() + ".";
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpStatus.BAD_REQUEST.value(), "Either the resource does not exist or you are unauthorized to access it");
            } catch (IOException e) {
                LOGGER.error("exception thrown when redirecting user to error message", e);
            }
        }
    }

    public String getOutcome() {
        return outcome;
    }
}
