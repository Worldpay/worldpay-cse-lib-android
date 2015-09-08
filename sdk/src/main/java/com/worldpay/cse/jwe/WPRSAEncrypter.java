package com.worldpay.cse.jwe;

import java.security.Key;

import javax.crypto.Cipher;

import com.worldpay.cse.exception.WPCSEException;

/**
 * The RSA1_5 encrypter used for JWE key encryption. The encrypter is initialized with just the public key or a generic(private & public) RSA key.
 */
class WPRSAEncrypter implements WPEncrypter {

    private static final String RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    
    private Key key;

    /**
     * Creates a RSA1_5 encrypter based on the provided RSA (public) key
     * @param key the RSA key
     */
    public WPRSAEncrypter(Key key) {
        this.key = key;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new WPCSEException(e.getMessage(), e);
        }
    }
}
