# LatipaySDK for Android app

Using [Latipay](http://www.latipay.net) sdk to intergrate Alipay and Wechat pay mobile app payment solution. Alipay or Wechat app is required.

![](screenshot/home.png?a)

### What you must have before using this SDK.(If you don't know what they are, please contact us)

* user id
* wallet id
* api key
* wechat app id
* alipay or wechat app installed

### 1. For security reasons in android apps, Wechat pay needs extra signature of your app.

* The signature depends on keystore and app bundle id. So please setup your keystore first for your project [Sign Your App](https://developer.android.com/studio/publish/app-signing).
* Run [this apk](https://open.weixin.qq.com/zh_CN/htmledition/res/dev/download/sdk/Gen_Signature_Android.apk) in your android phone.
* Generate your the signature with bundle id.
* Add this signature into your [Wechat Open Platform](https://open.weixin.qq.com/cgi-bin/applist?t=manage/list&lang=en_US&token=8ddaa9f124505b6f326d1bdb7addc71b153981ec) for your android app.

![](screenshot/chapter8_5_3.png)

### 2. Download [latipay.aar](https://github.com/Latipay/LatipaySDK-Android-Demo/raw/master/latipay/latipay.aar?1525301264551) module and import it into your android studio project.

Android Studio: File > New > New Module

![](screenshot/framework.png)


Add these two dependencies in build.gradle

```
dependencies {
  ...

  implementation 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
  implementation project(':latipay')
}
```

In your AndroidManifest.xml, please add this activity and it's alias for opening Wechat app.

```
//AndroidManifest.xml
<application ...>

    <activity
        android:name="net.latipay.mobile.WXPayEntryActivity"
        android:configChanges="orientation|keyboardHidden|navigation|screenSize"
        android:launchMode="singleTop"
        android:theme="@android:style/Theme.Translucent.NoTitleBar" />

    <activity-alias
        android:name="youApplicationId.wxapi.WXPayEntryActivity"
        android:exported="true"
        android:targetActivity="net.latipay.mobile.WXPayEntryActivity" />
    
</application>
```

Note that the class `.wxapi.WXPayEntryActivity` in `activity-alias` is under applicationId, rather than package. So if your applicationId is different from package name, please specify the class name with applicationId.

```
//build.gradle

defaultConfig {
    applicationId "com.android.application.id"
    ...


//AndroidManifest.xml

<activity-alias
    android:name="com.android.application.id.wxapi.WXPayEntryActivity"
    ...
```



### 3. Setup Latipay info in project, [you can get apiKey here](https://merchant.latipay.net) or [contact us](http://www.latipay.net/contact/)

```java
LatipayAPI.setup("your apiKey", "your userId", "your walletId");

```

### 4. How to use Alipay?

```java

AlipayRequest req = new AlipayRequest(this);
req.amount = "8.88";
req.merchantReference = "89439798527864287364"; //must be unique in your system
req.productName = "Fossil Women's Rose Goldtone Blane Watch"; //optional
req.callbackUrl = "https://yourwebsite.com/pay_callback";

req.setListener(new LatipayListener() {
  @Override
  public void onTransactionCompleted(HashMap<String, String> transaction, Error error) {
    //1. create a latipay transaction which is pending, the status is PENDING.
  }

  @Override
  public void onPaymentCompleted(int result) {
    //2. then alipay app will tell you the result of payment, the status could be PAID, UNPAID, or NEED_QUERY_SERVER
    //If it's NEED_QUERY_SERVER, please query from your own server.
  }
});

LatipayAPI.sendRequest(req);
```

### 5. How to use wechat pay?

```java

WechatpayRequest req = new WechatpayRequest(this);
req.amount = "8.88";
req.merchantReference = "1239127391273213132"; //must be unique in your system
req.productName = "Fossil Women's Rose Goldtone Blane Watch"; //optional
req.callbackUrl = "https://yourwebsite.com/pay_callback";

req.setListener(new LatipayListener() {
  @Override
  public void onTransactionCompleted(HashMap<String, String> transaction, Error error) {
    //1. create a latipay transaction, the status is PENDING.
  }

  @Override
  public void onPaymentCompleted(int result) {
    //2. then wechat app will tell you the result of payment, the status could be PAID, UNPAID, or NEED_QUERY_SERVER
    //If it's NEED_QUERY_SERVER, please query from your own server.
  }
});

LatipayAPI.sendRequest(req);
```

### 6. Please support the `callbackUrl` in AlipayRequest and WechatpayRequest for receiving the result of payment in your backend server. Latipay server will call this api to notify the result when status updated.

```
POST https://yourwebsite.com/pay_callback
Content-Type: application/x-www-form-urlencoded
```

Parameters:

```json
{
  "transaction_id": "43cb917ff8a6",
  "merchant_reference": "dsi39ej430sks03",
  "amount": "120.00",
  "currency": "NZD",
  "payment_method": "alipay",
  "pay_time": "2017-07-07 10:53:50",
  "status" : "paid",
  "signature": "14d5b06a2a5a2ec509a148277ed4cbeb3c43301b239f080a3467ff0aba4070e3",
}
```

[More info about this notify api](http://doc.latipay.net/v2/latipay-hosted-online.html#Payment-Result-Asynchronous-Notification)
