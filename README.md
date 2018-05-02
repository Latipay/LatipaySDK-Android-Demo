# LatipaySDK for Android app

Using [Latipay](http://www.latipay.net) sdk to intergrate Alipay and Wechat pay payment solution.

![](screenshot/home.png?a)

### 1. For security reasons, Wechatpay needs signature for your android app.

* Wechatpay Android app signature depends on keystore and bundle id. So please setup your keystore first for your project [Sign Your App](https://developer.android.com/studio/publish/app-signing)
* Run [this apk](https://open.weixin.qq.com/zh_CN/htmledition/res/dev/download/sdk/Gen_Signature_Android.apk) in your mobile phone
* Generate your app's signature with bundle id.
* All is done. Please tell us your signature.

![](https://pay.weixin.qq.com/wiki/doc/api/img/chapter8_5_3.png)

### 2. Download [latipay.aar](https://github.com/Latipay/LatipaySDK-Android-Demo/raw/master/latipay/latipay.aar) module and import it into your android studio project. 

Android Studio: New > New Module..

![](screenshot/framework.png)


Add latipay dependency into your project's build.gradle

```
dependencies {
  ...
  
  implementation 'com.tencent.mm.opensdk:wechat-sdk-android-with-mta:+'
  implementation project(':latipay')
}
```

### 3. Setup Latipay info in project, [you can get apiKey here](https://merchant.latipay.net) or [contact us](http://www.latipay.net/contact/)

```java
LatipayAPI.setup("your apiKey", "your userId", "your walletId");

```

### 4. App user purchases with goods using alipay app

```java

AlipayRequest req = new AlipayRequest(this);
req.amount = "8.88";
req.merchantReference = "89439798527864287364";
req.productName = "Fossil Women's Rose Goldtone Blane Watch"; //optional
req.callbackUrl = "https://yourwebsite.com/pay_callback";

req.setListener(new LatipayListener() {
  @Override
  public void onOrderCompleted(HashMap<String, String> latipayOrder, Error error) {
    //1. create a latipay order which is pending.
  }
    
  @Override
  public void onPaymentCompleted(String result, Error error) {
    //2. then alipay app will tell you the result of payment
  }
});
	
LatipayAPI.sendRequest(req);
```

### 5. App user purchases with goods using wechat app

```java

WechatpayRequest req = new WechatpayRequest(this);
req.amount = "8.88";
req.merchantReference = "1239127391273213132";
req.productName = "Fossil Women's Rose Goldtone Blane Watch"; //optional
req.callbackUrl = "https://yourwebsite.com/pay_callback";

req.setListener(new LatipayListener() {
  @Override
  public void onOrderCompleted(HashMap<String, String> latipayOrder, Error error) {
    //1. create a latipay order which is pending.
  }
    
  @Override
  public void onPaymentCompleted(String result, Error error) {
    //2. then wechat app will tell you the result of payment
  }
});
	
LatipayAPI.sendRequest(req);
```

--

### 6. In your web server, please support the below api for notifying when payment finished.

```
POST https://yourwebsite.com/pay_callback
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


