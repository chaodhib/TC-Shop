package com.chaouki.tcshop.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.ManagedBean;
import java.io.Serializable;

@ManagedBean
@ApplicationScope
public class GlobalController implements Serializable {


    @Value("${stripe.public.key}")
    private String stripePublicKey;

    public String getStripePublicKey() {
        return stripePublicKey;
    }
}
