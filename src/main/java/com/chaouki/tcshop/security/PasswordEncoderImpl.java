package com.chaouki.tcshop.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordEncoderImpl implements PasswordEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoderImpl.class);

    @Override
    public String encode(CharSequence rawPassword) {
        return null; //noop. we don't need to create accounts in this applications. only authenticate.
    }

    @Override
    public boolean matches(CharSequence userSubmittedPwd, String fromUserDetailsServiceImpl) {
        String[] tokens = fromUserDetailsServiceImpl.split("@");

        if(tokens.length != 2)
            throw new IllegalArgumentException("this case shouldn't happen!");

        String username = tokens[0];
        String secondStep = ":";
        String hashedPassword = tokens[1];

        String pwdToTest = userSubmittedPwd.toString().toUpperCase();

        try {
            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");

            msdDigest.update(username.getBytes("UTF-8"), 0, username.length());
            msdDigest.update(secondStep.getBytes("UTF-8"), 0, secondStep.length());
            msdDigest.update(pwdToTest.getBytes("UTF-8"), 0, pwdToTest.length());

            String sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
            return sha1.equals(hashedPassword);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            LOGGER.error("unable to decode user's password", e);
            return false;
        }
    }
}
