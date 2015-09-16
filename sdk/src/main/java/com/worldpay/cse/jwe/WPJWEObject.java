//
//  WPJWEObject.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse.jwe;

import com.worldpay.cse.exception.WPCSEException;

import org.spongycastle.util.encoders.UrlBase64;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

/**
 * The JWE Object class that does the whole JWE magic. This class encrypts the payload using AES256GCM algorithm with a random generated
 * key, which in turn is encrypted using RSA1_5 algorithm with a predefined public key. The final result is obtained from
 * {@link WPJWEObject#serialize()} which should be called after {@link WPJWEObject#encrypt()}
 */
public class WPJWEObject {

    private static final String ASCII = "ASCII";
    private static final String AES_ALGORITHM = "AES";
    private static final int BYTE_LENGTH = 8;
    private static final Character DOT = '.';

    private WPJWEHeader header;
    private String payload;
    private byte[] encryptedKey;
    private byte[] cipherText;
    private byte[] iv;
    private byte[] authTag;
    private Key key;

    /**
     * Creates a JWE object based on the provided JWE header and plain payload
     *
     * @param header a {@link WPJWEHeader} instance, that must not be null
     * @param payload a plain string payload
     */
    public WPJWEObject(WPJWEHeader header, String payload) {
        this.header = header;
        this.payload = payload;
    }

    /**
     * Encrypts the the plain payload
     *
     * @throws WPCSEException If encryption failed.
     */
    public void encrypt() throws WPCSEException {

        try {
            iv = WPKeyGen.generateKey(WPKeyGen.IV_BIT_LENGTH);
            byte[] aad = base64URLEncode(header.toString().getBytes()).getBytes(ASCII);
            byte[] cKey = WPKeyGen.generateKey(WPKeyGen.KEY_BIT_LENGTH);

            WPEncrypter cEncrypter = new WPAESEncrypter(new SecretKeySpec(cKey, AES_ALGORITHM), iv, aad);
            WPEncrypter kEncrypter = new WPRSAEncrypter(key);

            encryptedKey = kEncrypter.encrypt(cKey);
            byte[] cipher = cEncrypter.encrypt(payload.getBytes());

            int tagLength = WPKeyGen.AUTH_TAG_BIT_LENGTH / BYTE_LENGTH;
            int tagPos = cipher.length - tagLength;
            cipherText = new byte[tagPos];
            authTag = new byte[tagLength];
            System.arraycopy(cipher, 0, cipherText, 0, cipherText.length);
            System.arraycopy(cipher, tagPos, authTag, 0, authTag.length);

        } catch (Exception e) {
            throw new WPCSEException(e.getMessage(), e);
        }

    }

    private String base64URLEncode(byte[] bytes) throws UnsupportedEncodingException {
        String string = new String(UrlBase64.encode(bytes));
        //remove padding
        int index = string.indexOf(DOT);
        if (index > 0) {
            string = string.substring(0, index);
        }
        return string;
    }

    /**
     * Serialises this JWE object to its compact format consisting of
     * Base64URL-encoded parts delimited by period ('.') characters.
     *
     * <pre>
     * [header-base64url].[encryptedKey-base64url].[iv-base64url].[cipherText-base64url].[authTag-base64url]
     * </pre>
     *
     * @return The serialised JWE object.
     */
    public String serialize() throws WPCSEException {

        try {
            StringBuilder sb = new StringBuilder(base64URLEncode(header.toString().getBytes()));
            sb.append(DOT);
            sb.append(base64URLEncode(encryptedKey));
            sb.append(DOT);
            sb.append(base64URLEncode(iv));
            sb.append(DOT);
            sb.append(base64URLEncode(cipherText));
            sb.append(DOT);
            sb.append(base64URLEncode(authTag));

            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new WPCSEException("Unsupported encoding exception", e);
        }

    }

    /**
     * Set the key encryption key. Current implementation uses a RSA public key
     *
     * @param key the key (by default it should be an instance of RSAPublicKey).
     */
    public void setKey(Key key) {
        this.key = key;
    }
}
