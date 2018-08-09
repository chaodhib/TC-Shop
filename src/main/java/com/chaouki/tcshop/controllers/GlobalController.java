package com.chaouki.tcshop.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.ManagedBean;

@ManagedBean
@ApplicationScope
public class GlobalController {


    @Value("${stripe.public.key}")
    private String stripePublicKey;

    public String getStripePublicKey() {
        return stripePublicKey;
    }
}
