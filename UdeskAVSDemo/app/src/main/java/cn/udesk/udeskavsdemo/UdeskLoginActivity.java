
package cn.udesk.udeskavsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.udesk.udeskavssdk.UdeskAVSSDKManager;
import cn.udesk.udeskavssdk.callback.IUdeskCallback;
import cn.udesk.udeskavssdk.utils.PermissionUtil;
import cn.udesk.udeskavssdk.utils.ToastUtils;

/**
 * @author admin
 */
public class UdeskLoginActivity extends DemoBaseActivity implements View.OnClickListener {
    public static final String TAG = "LoginActivity";
    private EditText appId;
    private EditText userId;
    private EditText domain;
    private LinearLayout domainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appId = findViewById(R.id.appId);
        userId = findViewById(R.id.userId);
        domain = findViewById(R.id.domain);
        domainContainer = findViewById(R.id.domainContainer);
        findViewById(R.id.call).setOnClickListener(this);
        findViewById(R.id.set_up).setOnClickListener(this);
        findViewById(R.id.set_up).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                domainContainer.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(appId.getText().toString()) || TextUtils.isEmpty(userId.getText().toString())) {
            ToastUtils.showToast(getApplicationContext(), getString(R.string.udesk_avs_complete_info));
            return;
        }
        if (domainContainer.isShown()){
            String baseUrl = domain.getText().toString();
            if (!TextUtils.isEmpty(baseUrl) && baseUrl.startsWith("http")){
                UdeskAVSSDKManager.getInstance().setBaseUrl(baseUrl+"/");
            }
        }

        if (R.id.call == v.getId()) {
            boolean ret = PermissionUtil.checkPermission(this);
            if (ret) {
                call();
            }
        } else if (R.id.set_up == v.getId()) {
            Intent intent = new Intent(this, UdeskSettingActivity.class);
            intent.putExtra(APPID, appId.getText().toString());
            intent.putExtra(USERID, userId.getText().toString());
            startActivity(intent);
        }
    }

    @Override
    protected void call() {
        try {
            showloadingDialog(getString(R.string.udesk_avs_ready_to_call));
            UdeskAVSSDKManager.getInstance().init(getApplicationContext(), appId.getText().toString(), userId.getText().toString());
            UdeskAVSSDKManager.getInstance().call(getApplicationContext(), appId.getText().toString(), userId.getText().toString(), new IUdeskCallback() {
                @Override
                public void onSuccess(boolean success) {
                    hideLoadingDialog();
                }

                @Override
                public void onError(String throwable) {
                    hideLoadingDialog();
                    ToastUtils.showToast(getApplicationContext(), throwable);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}