# LatipaySDK for Android app

Using [Latipay](http://www.latipay.net) sdk to intergrate Alipay payment solution, Wechat pay coming soon.

![](screenshot/home.png?a)

### 1. Download [latipay.aar](https://github.com/Latipay/LatipaySDK-Android-Demo/raw/master/latipay/latipay.aar) module and import it into your android studio project. 

Android Studio: New > New Module..

![](screenshot/framework.png)


Add latipay dependency into your project's build.gradle

```
dependencies {
  ...
  implementation project(':latipay')``
}
```

### 2. Setup Latipay info in project, [you can get apiKey here](https://merchant.latipay.co.nz/user/regist.action) or [contact us](http://www.latipay.net/contact/)

```java
LatipayAPI.setup("your apiKey", "your userId", "your walletId");

```

### 3. App user purchases with goods using alipay app

```java

AlipayRequest req = new AlipayRequest(this);
req.amount = "8.88";
req.merchantReference = "a reference";
req.productName = "Fossil Women's Rose Goldtone Blane Watch";
req.callbackUrl = "https://yourwebsite.com/pay_callback";

req.setListener(new AlipayOrderAndPaymentListener() {
  @Override
  public void onOrderCompleted(HashMap<String, String> latipayOrder, Error error) {
    //1. create a latipay order which is unpaid.
  }
    
  @Override
  public void onPaymentCompleted(String result, Error error) {
    //2. then alipay app will tell you the result of payment
  }
});
	
LatipayAPI.sendRequest(req);
```

--

### 5. In your web server, please support the below api for notifying when payment finished.

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


