package demo.sdk.latipay.net.latipaysdk_demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import net.latipay.mobile.AlipayOrderAndPaymentListener;
import net.latipay.mobile.AlipayRequest;
import net.latipay.mobile.LatipayAPI;
import net.latipay.mobile.WechatpayRequest;
import net.latipay.mobile.WechatpayOrderListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog dialog;

    private String wechatpayOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: setup apiKey, userId, walletId first
        LatipayAPI.setup("apiKey", "userId", "walletId");

        final MainActivity activity = this;

        ImageButton alipayBtn = findViewById(R.id.alipay_btn);
        alipayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickAlipay(activity);
            }
        });

        ImageButton wechatPayBtn = findViewById(R.id.wechat_btn);
        wechatPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickWechat(activity);
            }
        });
    }

    private void clickAlipay(final MainActivity activity) {

        activity.dialog = ProgressDialog.show(activity, null, "Lading", false, true);

        AlipayRequest req = new AlipayRequest(activity);
        req.amount = "0.01";
        req.merchantReference = "a reference";
        req.productName = "Fossil Women's Rose Goldtone Blane Watch";
        req.callbackUrl = "https://yourwebsite.com/pay_callback";

        req.setListener(new AlipayOrderAndPaymentListener() {

            @Override
            public void onOrderCompleted(HashMap<String, String> latipayOrder, Error error) {
                Log.d(TAG, "onTransactionCompleted " + String.valueOf(latipayOrder) + (error != null ? error.getMessage() : ""));
                activity.dialog.dismiss();

                if (error != null) {
                    Toast.makeText(activity, "Latipay: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Go to Alipay", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPaymentCompleted(String result, Error error) {
                Log.d(TAG, result);

                //read more https://docs.open.alipay.com/204/105301
                Toast.makeText(activity, "Alipay: " + (error != null ? error.getMessage() : result), Toast.LENGTH_LONG).show();
            }
        });

        LatipayAPI.sendRequest(req);
    }

    private void clickWechat(final MainActivity activity) {

        activity.dialog = ProgressDialog.show(activity, null, "Lading", false, true);


        //send request
        WechatpayRequest req = new WechatpayRequest(activity);
        req.amount = "0.01";
        req.merchantReference = "a reference";
        req.productName = "Fossil Women's Rose Goldtone Blane Watch";
        req.callbackUrl = "https://yourwebsite.com/pay_callback";

        req.setListener(new WechatpayOrderListener() {
            @Override
            public void onOrderCompleted(HashMap<String, String> latipayOrder, Error error) {
                Log.d(TAG, "onTransactionCompleted " + String.valueOf(latipayOrder) + (error != null ? error.getMessage() : ""));
                activity.dialog.dismiss();

                if (latipayOrder != null) {
                    wechatpayOrderId = latipayOrder.get("order_id");
                }

                if (error != null) {
                    Toast.makeText(activity, "Latipay: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Go to Wechatpay", Toast.LENGTH_LONG).show();
            }
        });

        LatipayAPI.sendRequest(req);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (wechatpayOrderId != null) {
            final MainActivity activity = this;

            LatipayAPI.getPaymentStatus(wechatpayOrderId, new LatipayAPI.PaymentStatusListener() {
                @Override
                public void onOrderStatusSuccess(String result) {
                    Log.d(TAG, result);

                    Toast.makeText(activity, "Get Pay Status Success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onOrderStatusFailed(Error error) {
                    Log.d(TAG, error.toString());

                    Toast.makeText(activity, "Get Pay Status Failed", Toast.LENGTH_SHORT).show();

                }
            });
            wechatpayOrderId = null;
        }
    }
}
