//
//  WPJWEHeader.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.cse.jwe;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * The JWE header object that contains the following json fields:
 * <pre>
 * {
 *     "alg":"RSA1_5",
 *     "enc":"A256GCM",
 *     "kid":KeySeqNo,
 *     "com.worldpay.apiVersion":"1.0",
 *     "com.worldpay.libVersion":"1.0.0",
 *     "com.worldpay.channel": "android"
 * }
 * </pre>
 */
public class WPJWEHeader {

    @SerializedName("alg")
    private String algorithm;
    @SerializedName("enc")
    private String encryption;
    @SerializedName("kid")
    private String kid;
    @SerializedName("com.worldpay.apiVersion")
    private String apiVersion;
    @SerializedName("com.worldpay.libVersion")
    private String libVersion;
    @SerializedName("com.worldpay.channel")
    private String channel;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getLibVersion() {
        return libVersion;
    }

    public void setLibVersion(String libVersion) {
        this.libVersion = libVersion;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Returns the JSON representation of the WPJWEHeader object
     *
     * @return a JSON string
     */
    public String toString() {
        return new Gson().toJson(this);
    }

}
