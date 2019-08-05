# Worldpay Client Side Encryption (CSE) Android SDK


[Worldpay CSE](http://support.worldpay.com/support/kb/gg/client-side-encryption/Content/A%20-%20Home/Home.htm) Android SDK is a library created to help you integrate Worldpay client side encryption into your mobile applications. For more detailed documentation please follow this [link](http://support.worldpay.com/support/kb/gg/client-side-encryption/Content/D%20-%20Integration/Client%20Side%20Integration.htm).


## Proguard

Encryption might cause problems if the app is using proguard. Disable it for this SDK by adding:

`-keep class com.worldpay.cse.** { *; }`
