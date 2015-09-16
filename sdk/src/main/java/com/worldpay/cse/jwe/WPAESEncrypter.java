//
//  WPAESEncrypter.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse.jwe;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESFastEngine;
import org.spongycastle.crypto.modes.GCMBlockCipher;
import org.spongycastle.crypto.params.AEADParameters;
import org.spongycastle.crypto.params.KeyParameter;

import java.security.Key;

import com.worldpay.cse.exception.WPCSEException;

/**
 * AES256GCM encrypter. This implementation uses SpongyCastle (BouncyCastle for Android) API, because the AES256GCM standard is supported
 * only from Java 7/8 or Android 4.4+.
 */
class WPAESEncrypter implements WPEncrypter {

    private Key key;
    private byte[] aad;
    private byte[] iv;

    /**
     * Creates an AESGCM encrypter with specified key parameters
     *
     * @param key the encryption key
     * @param iv the standard Initialisation Vector (IV)
     * @param aad the additional authenticated data
     *
     */
    public WPAESEncrypter(Key key, byte[] iv, byte[] aad) {
        this.iv = iv.clone();
        this.key = key;
        this.aad = aad.clone();
    }

    @Override
    public byte[] encrypt(byte[] data) {
        // encrypt
        AEADParameters parameters = new AEADParameters(new KeyParameter(key.getEncoded()), WPKeyGen.AUTH_TAG_BIT_LENGTH, iv, aad);
        GCMBlockCipher gcmEngine = new GCMBlockCipher(new AESFastEngine());
        gcmEngine.init(true, parameters);

        byte[] cipherText = new byte[gcmEngine.getOutputSize(data.length)];
        int encLen = gcmEngine.processBytes(data, 0, data.length, cipherText, 0);
        try {
            gcmEngine.doFinal(cipherText, encLen);
        } catch (InvalidCipherTextException e) {
            throw new WPCSEException(e.getLocalizedMessage(), e);
        }

        return cipherText;
    }
}
