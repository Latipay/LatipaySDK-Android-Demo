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
import net.latipay.mobile.PaymentStatus;

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
    }

    private void clickAlipay(final MainActivity activity) {

        activity.dialog = ProgressDialog.show(activity, null, "Loading", false, true);

        AlipayRequest req = new AlipayRequest(activity);
        req.amount = "0.1";
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
            public void onPaymentCompleted(int result) {
                if (result == PaymentStatus.PAID) {
                    Toast.makeText(activity, "Alipay: paid", Toast.LENGTH_LONG).show();
                }else if (result == PaymentStatus.UNPAID) {
                    Toast.makeText(activity, "Alipay: unpaid", Toast.LENGTH_LONG).show();
                }else { //PaymentStatus.UNKNOWN

                    //search payment status from your own server
                }
            }
        });

        LatipayAPI.sendRequest(req);
    }
}
