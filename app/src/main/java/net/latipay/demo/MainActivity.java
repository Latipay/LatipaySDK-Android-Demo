package net.latipay.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.latipay.mobile.LatipayListener;
import net.latipay.mobile.AlipayRequest;
import net.latipay.mobile.LatipayAPI;
import net.latipay.mobile.PaymentStatus;
import net.latipay.mobile.WechatpayRequest;

import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: setup apiKey, userId, walletId first
        LatipayAPI.setup("", "", "");

        final MainActivity activity = this;

        ImageButton alipayBtn = findViewById(R.id.alipay_btn);
        alipayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickAlipay(activity);
            }
        });

        ImageButton wechatPayBtn = findViewById(R.id.wechatpay_button);
        wechatPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { activity.clickWechat(activity);
            }
        });
    }

    private void clickAlipay(final MainActivity activity) {

        activity.dialog = ProgressDialog.show(activity, null, "Loading", false, true);

        AlipayRequest req = new AlipayRequest(activity);
        req.amount = "0.1";

        //merchantReference is your order id, must be unique in your system
        //using date here is only for demo.
        req.merchantReference = Long.valueOf(new Date().getTime()).toString();

        req.productName = "Fossil Women's Rose Goldtone Blane Watch"; //optional
        req.callbackUrl = "https://yourwebsite.com/pay_callback";

        req.setListener(new LatipayListener() {

            @Override
            public void onTransactionCompleted(HashMap<String, String> latipayOrder, Error error) {
                Log.d(TAG, "onTransactionCompleted " + latipayOrder + (error != null ? error.getMessage() : ""));
                activity.dialog.dismiss();

                if (error != null) {
                    Toast.makeText(activity, "Latipay: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Go to Alipay", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPaymentCompleted(int result) {
                if (result == PaymentStatus.PAID) {
                    Toast.makeText(activity, "Alipay: paid", Toast.LENGTH_LONG).show();
                }else if (result == PaymentStatus.UNPAID) {
                    Toast.makeText(activity, "Alipay: unpaid", Toast.LENGTH_LONG).show();
                }else { //PaymentStatus.UNKNOWN
                    Log.d(TAG, "PaymentStatus.UNKNOWN");
                    //search payment status from your own server
                }
            }
        });

        LatipayAPI.sendRequest(req);
    }

    private void clickWechat(final MainActivity activity) {
        activity.dialog = ProgressDialog.show(activity, null, "Loading", false, true);

        WechatpayRequest req = new WechatpayRequest(activity);
        req.amount = "0.1";

        //merchantReference is your order id, must be unique in your system
        //using date here is only for demo.
        req.merchantReference = Long.valueOf(new Date().getTime()).toString();

        req.productName = "Fossil Women's Rose Goldtone Blane Watch"; //optional
        req.callbackUrl = "https://yourwebsite.com/pay_callback";

        req.setListener(new LatipayListener() {

            @Override
            public void onTransactionCompleted(HashMap<String, String> transaction, Error error) {
                Log.d(TAG, "onTransactionCompleted " + transaction + (error != null ? error.getMessage() : ""));
                activity.dialog.dismiss();

                if (error != null) {
                    Toast.makeText(activity, "Latipay: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Go to Wechat", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPaymentCompleted(int result) {
                if (result == PaymentStatus.PAID) {
                    Toast.makeText(activity, "WechatPay: paid", Toast.LENGTH_LONG).show();
                }else if (result == PaymentStatus.UNPAID) {
                    Toast.makeText(activity, "WechatPay: unpaid", Toast.LENGTH_LONG).show();
                }else { //PaymentStatus.UNKNOWN
                    Log.d(TAG, "PaymentStatus.UNKNOWN");
                    //search payment status from your own server
                }
            }
        });

        LatipayAPI.sendRequest(req);
    }
}
