package com.worldpay.cse;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;

import junit.framework.Assert;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;

import com.worldpay.cse.exception.WPCSEException;
import com.worldpay.cse.exception.WPCSEInvalidCardData;
import com.worldpay.cse.exception.WPCSEInvalidPublicKey;

import static org.mockito.Mockito.mock;

/**
 * WorldpayCSE Tester.
 *
 */
public class WorldpayCSETest {

    private WorldpayCSE worldpayCSE;

    private String validPublicKey = "2#10001#bf49edcaba456c6357e4ace484c3fba212543e78bf" +
            "72a8c2238caaa1c7ed20262956caa61d74840598d9b0707bc8" +
            "2e66f18c8b369c77ae6be0429c93323bb7511fc73d9c7f6988" +
            "72a8384370cd77c7516caa25a195d48701e3e0462d61200983" +
            "ba26cc4a20bb059d5beda09270ea6dcf15dd92084c4d5867b6" +
            "0986151717a8022e4054462ee74ab8533dda77cee227a49fda" +
            "f58eaeb95df90cb8c05ee81f58bec95339b6262633aef216f3" +
            "ae503e8be0650350c48859eef406e63d4399994b147e45aaa1" +
            "4cf9936ac6fdd7d4ec5e66b527d041750ba63a8296b3e6e774" +
            "a02ee6025c6ee66ef54c3688e4844be8951a8435e6b6e8d676" +
            "3d9ee5f16521577e159d";

    private String invalidPublicKey = "10001#bf49edcaba456c6357e4ace484c3fba212543e78bf" +
            "72a8c2238caaa1c7ed20262956caa61d74840598d9b0707bc8" +
            "2e66f18c8b369c77ae6be0429c93323bb7511fc73d9c7f6988" +
            "72a8384370cd77c7516caa25a195d48701e3e0462d61200983" +
            "ba26cc4a20bb059d5beda09270ea6dcf15dd92084c4d5867b6" +
            "0986151717a8022e4054462ee74ab8533dda77cee227a49fda" +
            "f58eaeb95df90cb8c05ee81f58bec95339b6262633aef216f3" +
            "ae503e8be0650350c48859eef406e63d4399994b147e45aaa1" +
            "4cf9936ac6fdd7d4ec5e66b527d041750ba63a8296b3e6e774" +
            "a02ee6025c6ee66ef54c3688e4844be8951a8435e6b6e8d676" +
            "3d9ee5f16521577e159d";

    @Before
    public void before() throws Exception {
        //Java 7 does not support a set of new encryption algorithms such as AES256GCM
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        worldpayCSE = new WorldpayCSE();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method setPublicKey(WPRSAPublicKey publicKey)
     */
    @Test
    public void testSetPublicKeyPlain() throws Exception {

        worldpayCSE.setPublicKey(validPublicKey);

        Assert.assertEquals(validPublicKey, worldpayCSE.getPublicKey().toString());
    }

    @Test
    public void testSetPublicKey() throws Exception {

        WPPublicKey publicKey = mock(WPPublicKey.class);
        worldpayCSE.setPublicKey(publicKey);

        Assert.assertSame(publicKey, worldpayCSE.getPublicKey());
    }

    @Test
    public void testIsValidPublicKeyPositive() throws Exception {

        boolean valid = WorldpayCSE.isValidPublicKey(validPublicKey);

        Assert.assertTrue(valid);
    }

    @Test
    public void testIsValidPublicKeyNegative() throws Exception {

        boolean valid = WorldpayCSE.isValidPublicKey(invalidPublicKey);

        Assert.assertFalse(valid);
    }


    @Test(expected=WPCSEInvalidPublicKey.class)
    public void testSetPublicKeyInvalid() throws Exception {

        worldpayCSE.setPublicKey(invalidPublicKey);

    }

    /**
     * Method encrypt()
     */
    @Test
    public void testEncrypt() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        worldpayCSE.setPublicKey(new WPPublicKey(publicKey, "2"));

        WPCardData cardData = getWPCardData();

        String encryptedData = worldpayCSE.encrypt(cardData);
        JWEObject jweObject = JWEObject.parse(encryptedData);

        RSADecrypter decrypter = new RSADecrypter(privateKey);

        jweObject.decrypt(decrypter);

        JWEHeader header = jweObject.getHeader();

        Assert.assertEquals(JWEAlgorithm.RSA1_5.getName(), header.getAlgorithm().getName());
        Assert.assertEquals(EncryptionMethod.A256GCM.getName(), header.getEncryptionMethod().getName());
        Assert.assertEquals("2", header.getKeyID());
        Assert.assertEquals("1.0", header.getCustomParam("com.worldpay.apiVersion"));
        Assert.assertEquals("1.0.0", header.getCustomParam("com.worldpay.libVersion"));
        Assert.assertEquals("android", header.getCustomParam("com.worldpay.channel"));

        String json = jweObject.getPayload().toString();
        WPCardData cardDataRet = WPCardData.parseJSON(json);

        Assert.assertEquals(cardData.getCardHolderName(), cardDataRet.getCardHolderName());
        Assert.assertEquals(cardData.getCardNumber(), cardDataRet.getCardNumber());
        Assert.assertEquals(cardData.getExpiryMonth(), cardDataRet.getExpiryMonth());
        Assert.assertEquals(cardData.getExpiryYear(), cardDataRet.getExpiryYear());
        Assert.assertEquals(cardData.getCvc(), cardDataRet.getCvc());

    }

    /**
     * Method encrypt()
     */
    @Test(expected = WPCSEInvalidCardData.class)
    public void testEncryptInvalid() throws Exception {

        WPCardData cardData = new WPCardData();
        cardData.setCardNumber("122121");
        cardData.setCvc("1");
        cardData.setExpiryMonth("");
        cardData.setExpiryYear("11");
        cardData.setCardHolderName("");

        worldpayCSE.encrypt(cardData);
    }

    /**
     * Method encrypt()
     */
    @Test(expected = WPCSEException.class)
    public void testEncryptKeyNotSet() throws Exception {

        WPCardData cardData = getWPCardData();

        worldpayCSE.encrypt(cardData);
    }

    @Test
    public void testValidateOk() throws Exception {
        WPCardData cardData = getWPCardData();

        Set<Integer> errors = worldpayCSE.validate(cardData);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testValidateEmptyValues() throws Exception {
        WPCardData cardData = new WPCardData();
        cardData.setCardNumber("");
        cardData.setCvc("");
        cardData.setExpiryMonth("");
        cardData.setExpiryYear("");
        cardData.setCardHolderName("");

        Set<Integer> errors = worldpayCSE.validate(cardData);
        //check error codes
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.EMPTY_CARD_NUMBER));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.EMPTY_EXPIRY_MONTH));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.EMPTY_EXPIRY_YEAR));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.EMPTY_CARD_HOLDER_NAME));
        //no other errors
        Assert.assertEquals(4, errors.size());
    }

    @Test
    public void testValidateShortValues() throws Exception {
        WPCardData cardData = new WPCardData();
        cardData.setCardNumber("1234123");
        cardData.setCvc("1");
        cardData.setExpiryMonth("2");
        cardData.setExpiryYear("20");
        cardData.setCardHolderName("J");

        Set<Integer> errors = worldpayCSE.validate(cardData);
        //check error codes
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CARD_NUMBER));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CVC));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_EXPIRY_MONTH));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_EXPIRY_YEAR));
        // no other errors
        Assert.assertEquals(4, errors.size());
    }

    @Test
    public void testValidateLongValues() throws Exception {
        WPCardData cardData = new WPCardData();
        cardData.setCardNumber("123456789012345678901234");
        cardData.setCvc("12333");
        cardData.setExpiryMonth("1212");
        cardData.setExpiryYear("197021");
        cardData.setCardHolderName("1234567890123456789012345678901");

        Set<Integer> errors = worldpayCSE.validate(cardData);
        //check error codes
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CARD_NUMBER));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CVC));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_EXPIRY_MONTH));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_EXPIRY_YEAR));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CARD_HOLDER_NAME));

        // no other errors
        Assert.assertEquals(5, errors.size());
    }

    @Test
    public void testValidateLogicValues() throws Exception {
        WPCardData cardData = new WPCardData();
        cardData.setCardNumber("4444333322221110");
        cardData.setCvc("123");
        cardData.setExpiryMonth("12");
        cardData.setExpiryYear("1970");
        cardData.setCardHolderName("John Smith");

        Set<Integer> errors = worldpayCSE.validate(cardData);
        //check error codes
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_EXPIRY_DATE));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CARD_NUMBER_BY_LUHN));
        // no other errors
        Assert.assertEquals(2, errors.size());
    }

    @Test
    public void testValidateLogicValues2() throws Exception {
        WPCardData cardData = new WPCardData();
        cardData.setCardNumber("8444333322221110");
        cardData.setCvc("123");
        cardData.setExpiryMonth("13");
        cardData.setExpiryYear("2020");
        cardData.setCardHolderName("John Smith");

        Set<Integer> errors = worldpayCSE.validate(cardData);
        //check error codes
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_EXPIRY_MONTH_OUT_RANGE));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CARD_NUMBER_BY_LUHN));
        // no other errors
        Assert.assertEquals(2, errors.size());
    }

    @Test
    public void testValidateNonDigitalValue() throws Exception {
        WPCardData cardData = new WPCardData();
        cardData.setCardNumber("4444oooo22221111");
        cardData.setCvc("1a3");
        cardData.setExpiryMonth("a1");
        cardData.setExpiryYear("19o1");
        cardData.setCardHolderName("John Smith");

        Set<Integer> errors = worldpayCSE.validate(cardData);
        //check error codes
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CARD_NUMBER));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_CVC));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_EXPIRY_MONTH));
        Assert.assertTrue(errors.contains(WPValidationErrorCodes.INVALID_EXPIRY_YEAR));

        // no other errors
        Assert.assertEquals(4, errors.size());
    }

    private WPCardData getWPCardData() {
        WPCardData cardData = new WPCardData();
        cardData.setCardHolderName("John Smith");
        cardData.setCardNumber("4444333322221111");
        cardData.setCvc("123");
        cardData.setExpiryMonth("12");
        cardData.setExpiryYear("2020");
        return cardData;
    }

} 
