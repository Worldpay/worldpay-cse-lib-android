//
//  MainActivity.java
//  WorldpayCSE
//
//  Created by Alexandru Catariov on 23/07/2015.
//  Copyright (c) 2015 WorldPay. All rights reserved.
//
//  License information can be found in the LICENSE file

package com.worldpay.demoapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.worldpay.cse.WPCardData;
import com.worldpay.cse.WPValidationErrorCodes;
import com.worldpay.cse.WorldpayCSE;
import com.worldpay.cse.demoapp.R;
import com.worldpay.cse.exception.WPCSEException;
import com.worldpay.cse.exception.WPCSEInvalidCardData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    public static final String DEFAULT_PUBLIC_KEY = "1#10001#bf49edcaba456c6357e4ace484c3fba212543e78bf" +
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

    //the card data form fields
    private EditText cardHolder;
    private EditText cardNumber;
    private EditText cardExpiryMonth;
    private EditText cardExpiryYear;
    private EditText cardCVC;
    private TextView encryptedData;

    private Map<Integer, String> errorList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        cardHolder = (EditText) findViewById(R.id.editTextCardHolder);
        cardNumber = (EditText) findViewById(R.id.editTextCardNumber);
        cardExpiryMonth = (EditText) findViewById(R.id.editTextExpiryMonth);
        cardExpiryYear = (EditText) findViewById(R.id.editTextExpiryYear);
        cardCVC = (EditText) findViewById(R.id.editTextCVC);
        encryptedData = (TextView) findViewById(R.id.editTextEncryptedData);

        Button buttonEncrypt = (Button) findViewById(R.id.buttonEncrypt);
        buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encryptFormData();
            }
        });
    }

    private void encryptFormData() {
        WPCardData cardData = new WPCardData();

        cardData.setCardHolderName(cardHolder.getText().toString());
        cardData.setCardNumber(cardNumber.getText().toString());
        cardData.setCvc(cardCVC.getText().toString());
        cardData.setExpiryMonth(cardExpiryMonth.getText().toString());
        cardData.setExpiryYear(cardExpiryYear.getText().toString());

        encryptedData.setText(encryptCardData(cardData));

    }

    //Does the encryption of the card data. In case the operation fails, it displays the error details.
    //The might be two kind of errors: Invalid card fields, or other generic error
    private String encryptCardData(WPCardData cardData) {

        WorldpayCSE worldpayCSE = new WorldpayCSE();
        try {
            worldpayCSE.setPublicKey(DEFAULT_PUBLIC_KEY);
            return worldpayCSE.encrypt(cardData);
        } catch (WPCSEInvalidCardData e) {
            //Alternatively to catching this exception, there is WorldpayCSE#validate(WPCardData) that
            //can be used for similar purpose
            displayFormFieldErrors(e.getErrorCodes());
        } catch (WPCSEException e) {
            //If the exception is not card validation related, just show a toast with the message
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    //Load the errors from resources. Each error has a code that is separated by " - " from the text
    //message. It also caches the result, so, the second call is very fast
    private Map<Integer, String> getErrors() {
        if (errorList.isEmpty()) {
            String[] errors = this.getResources().getStringArray(R.array.errors);
            for (String error: errors) {
                String[] comp = error.split(" - ");
                int errorCode = Integer.parseInt(comp[0]);
                String errorMessage = comp[1];
                errorList.put(errorCode, errorMessage);
            }
        }
        return errorList;
    }

    //Returns the error message for the provided 'errorCodes' parameter that a contained in 'errorRange'
    private String getErrorMessage(Set<Integer> errorCodes, int...errorRange) {
        for (int code: errorRange) {
            if (errorCodes.contains(code)) {
                return getErrors().get(code);
            }
        }
        return null;
    }

    //Matches the error codes obtained after card data validation with the corresponding fields
    private void displayFormFieldErrors(Set<Integer> errorCodes) {

        cardHolder.setError(getErrorMessage(errorCodes,
                WPValidationErrorCodes.EMPTY_CARD_HOLDER_NAME,
                WPValidationErrorCodes.INVALID_CARD_HOLDER_NAME));

        cardNumber.setError(getErrorMessage(errorCodes,
                WPValidationErrorCodes.EMPTY_CARD_NUMBER,
                WPValidationErrorCodes.INVALID_CARD_NUMBER_BY_LUHN,
                WPValidationErrorCodes.INVALID_CARD_NUMBER));

        cardExpiryMonth.setError(getErrorMessage(errorCodes,
                WPValidationErrorCodes.EMPTY_EXPIRY_MONTH,
                WPValidationErrorCodes.INVALID_EXPIRY_MONTH,
                WPValidationErrorCodes.INVALID_EXPIRY_MONTH_OUT_RANGE,
                WPValidationErrorCodes.INVALID_EXPIRY_DATE));

        cardExpiryYear.setError(getErrorMessage(errorCodes,
                WPValidationErrorCodes.EMPTY_EXPIRY_YEAR,
                WPValidationErrorCodes.INVALID_EXPIRY_YEAR,
                WPValidationErrorCodes.INVALID_EXPIRY_DATE));

        cardCVC.setError(getErrorMessage(errorCodes, WPValidationErrorCodes.INVALID_CVC));

    }
}
