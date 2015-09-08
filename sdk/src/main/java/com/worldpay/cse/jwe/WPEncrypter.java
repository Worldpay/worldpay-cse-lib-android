package com.worldpay.cse.jwe;

/**
 * Encrypter interface. Implemented by {@link WPRSAEncrypter} and {@link WPAESEncrypter}
 */
interface WPEncrypter {

    /**
     * Encrypts the <code>data</code>
     *
     * @param data the plain data as an array of bytes
     * @return the encrypted data (cipher) as an array of bytes
     */
    byte[] encrypt(byte[] data);
}
