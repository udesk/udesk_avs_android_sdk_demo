package cn.udesk.udeskavsdemo;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import cn.udesk.udeskavssdk.UdeskAVSSDKManager;
import cn.udesk.udeskavssdk.callback.ITemplateMessageLinkCallBack;
import cn.udesk.udeskavssdk.callback.IUdeskCallback;
import cn.udesk.udeskavssdk.callback.IUdeskTemplateMessageCallBack;
import cn.udesk.udeskavssdk.callback.IUdeskTemplateMessagePhoneCallBack;
import cn.udesk.udeskavssdk.configs.UdeskConfig;
import cn.udesk.udeskavssdk.utils.PermissionUtil;
import cn.udesk.udeskavssdk.utils.ToastUtils;

/**
 * @author admin
 */
public class UdeskSettingActivity extends DemoBaseActivity implements View.OnClickListener {

    private String appId;
    private String userId;
    private EditText customChannel, agentId, agentGroupId, nickName, avatar, email, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udesk_setting);
        Intent intent = getIntent();
        appId = intent.getStringExtra(APPID);
        userId = intent.getStringExtra(USERID);
        initView();
    }

    private void initView() {
        customChannel = findViewById(R.id.customChannel);
        agentId = findViewById(R.id.agentId);
        agentGroupId = findViewById(R.id.agentGroupId);
        nickName = findViewById(R.id.nickName);
        avatar = findViewById(R.id.avatar);
        email = findViewById(R.id.email);
        level = findViewById(R.id.level);
        findViewById(R.id.setting_call).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appId)) {
            ToastUtils.showToast(getApplicationContext(), getString(R.string.udesk_avs_complete_info));
            return;
        }
        if (R.id.setting_call == v.getId()) {
            boolean ret = PermissionUtil.checkPermission(this);
            if (ret) {
                call();
            }
        }
    }

    @Override
    protected void call() {
        try {
            showloadingDialog(getString(R.string.udesk_avs_ready_to_call));
            UdeskAVSSDKManager.getInstance().call(getApplicationContext(), appId, userId, makeBuilder().build(), new IUdeskCallback() {
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


    private UdeskConfig.Builder makeBuilder() {
        UdeskConfig.Builder builder = new UdeskConfig.Builder();
        builder.setAgentInfo(buildAgentInfo())
                .setCustomerInfo(buildCustomerInfo())
                .setTemplateMessageLinkCallBack(new ITemplateMessageLinkCallBack() {
                    @Override
                    public void templateMsgLinkCallBack(Context context, String url) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息链接回调");
                    }
                })
                .setTemplateMessagePhoneCallBack(new IUdeskTemplateMessagePhoneCallBack() {
                    @Override
                    public void templateMsgPhoneCallBack(Context context, String jsonValue) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息电话回调");
                    }
                })
                .setTemplateMessageCallBack(new IUdeskTemplateMessageCallBack() {
                    @Override
                    public void templateMsgCallBack(Context context, String jsonValue) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息自定义回调");
                    }
                });
        return builder;
    }

    /**
     * 创建客服信息，非必传，如果有客服列表，必须设置agentId
     *
     * @return
     */
    private Map<String, Object> buildAgentInfo() {
        HashMap<String, Object> hashMap = new HashMap<>(8);
        if (!TextUtils.isEmpty(customChannel.getText().toString())) {
            hashMap.put("customChannel", customChannel.getText().toString());
        }
        if (!TextUtils.isEmpty(agentId.getText().toString())) {
            hashMap.put("agentId", agentId.getText().toString());
        }
        if (!TextUtils.isEmpty(agentGroupId.getText().toString())) {
            hashMap.put("agentGroupId", agentGroupId.getText().toString());
        }

        return hashMap;
    }

    /**
     * 创建客户信息，目前只支持这4个字段
     *
     * @return
     */
    private Map<String, Object> buildCustomerInfo() {
        HashMap<String, Object> hashMap = new HashMap<>(8);
        if (!TextUtils.isEmpty(nickName.getText().toString())) {
            hashMap.put("nickName", nickName.getText().toString());
        }
        if (!TextUtils.isEmpty(avatar.getText().toString())) {
            hashMap.put("avatar", avatar.getText().toString());
        }
        if (!TextUtils.isEmpty(email.getText().toString())) {
            hashMap.put("email", email.getText().toString());
        }
        if (!TextUtils.isEmpty(level.getText().toString())) {
            hashMap.put("level", level.getText().toString());
        }

        return hashMap;
    }
}