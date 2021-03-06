package cn.udesk.udeskavsdemo;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

import cn.udesk.udeskavssdk.UdeskAVSSDKManager;
import cn.udesk.udeskavssdk.callback.ITemplateMessageLinkCallBack;
import cn.udesk.udeskavssdk.callback.IUdeskCallback;
import cn.udesk.udeskavssdk.callback.IUdeskTemplateMessageCallBack;
import cn.udesk.udeskavssdk.callback.IUdeskTemplateMessagePhoneCallBack;
import cn.udesk.udeskavssdk.configs.UdeskConfig;
import cn.udesk.udeskavssdk.configs.UdeskVideoFillMode;
import cn.udesk.udeskavssdk.configs.UdeskVideoRotation;
import cn.udesk.udeskavssdk.ui.activity.UdeskVideoActivity;
import cn.udesk.udeskavssdk.utils.PermissionUtil;
import cn.udesk.udeskavssdk.utils.ToastUtils;

/**
 * @author admin
 */
public class UdeskSettingActivity extends DemoBaseActivity implements View.OnClickListener {

    private String appId;
    private String userId;
    private EditText customChannel, agentId, agentGroupId, nickName, avatar, email, level;
    private CheckBox useVoice,showLogo;
    private RadioGroup rotationGroup,fillModeGroup;
    private UdeskVideoRotation rotation = UdeskVideoRotation.UdeskVideoRotation_0;
    private UdeskVideoFillMode fillMode = UdeskVideoFillMode.UdeskVideoFillMode_Fill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udesk_setting);
        Intent intent = getIntent();
        appId = intent.getStringExtra(APPID);
        userId = intent.getStringExtra(USERID);
        UdeskAVSSDKManager.getInstance().init(getApplicationContext(), appId, userId);
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
        useVoice = findViewById(R.id.useVoice);
        showLogo = findViewById(R.id.showLogo);
        rotationGroup = findViewById(R.id.rotationGroup);
        fillModeGroup = findViewById(R.id.fillModeGroup);
        rotationGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rotation_0) {
                rotation = UdeskVideoRotation.UdeskVideoRotation_0;
            } else if (checkedId == R.id.rotation_90) {
                rotation = UdeskVideoRotation.UdeskVideoRotation_90;
            } else if (checkedId == R.id.rotation_180) {
                rotation = UdeskVideoRotation.UdeskVideoRotation_180;
            } else if (checkedId == R.id.rotation_270) {
                rotation = UdeskVideoRotation.UdeskVideoRotation_270;
            }
        });
        fillModeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.mode_fill) {
                fillMode = UdeskVideoFillMode.UdeskVideoFillMode_Fill;
            } else if (checkedId == R.id.mode_fit) {
                fillMode = UdeskVideoFillMode.UdeskVideoFillMode_Fit;
            }
        });
        findViewById(R.id.setting_call).setOnClickListener(this);
        findViewById(R.id.history_message).setOnClickListener(this);
        testData();
    }

    private void testData() {
        customChannel.setText("hu");
        agentId.setText("258031");
//        agentId.setText("17");
        agentGroupId.setText("2_4");
        nickName.setText("customer");
        avatar.setText("https://pro-cs-freq.kefutoutiao.com/doc/im/tid3055/Group%2016_1615542625814_p3fj4.png?x-oss-process=image/auto-orient,1/resize,h_300,w_300");
        email.setText("123@udesk.cn");
        level.setText("4");
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
        }else if (R.id.history_message == v.getId()){
            UdeskAVSSDKManager.getInstance().queryChatRecord();
        }
    }

    @Override
    protected void call() {
        try {
            showloadingDialog(getString(R.string.udesk_avs_ready_to_call));
            UdeskAVSSDKManager.getInstance().call(getApplicationContext(), appId, userId,makeBuilder().build(), new IUdeskCallback() {
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
                .setUseVoice(useVoice.isChecked())
                .setShowLogoBg(showLogo.isChecked())
                .setLogoResId(R.drawable.udesk_logo_test)
                .setLogoScaleType(ImageView.ScaleType.FIT_START)
                .setRemoteViewFillMode(fillMode)
                .setRemoteViewRotation(rotation)
                .setLocalViewFillMode(fillMode)
                .setLocalViewRotation(rotation)
                .setTemplateMessageLinkCallBack(new ITemplateMessageLinkCallBack() {
                    @Override
                    public void templateMsgLinkCallBack(UdeskVideoActivity activity, String url) {
                        ToastUtils.showToast(getApplicationContext(), "????????????????????????????????????");
                        activity.sendSystemMessage("??????????????????");
                    }
                })
                .setTemplateMessagePhoneCallBack(new IUdeskTemplateMessagePhoneCallBack() {
                    @Override
                    public void templateMsgPhoneCallBack(UdeskVideoActivity activity, String jsonValue) {
                        ToastUtils.showToast(getApplicationContext(), "????????????????????????????????????");
                        activity.sendSystemMessage("??????????????????");
                    }
                })
                .setTemplateMessageCallBack(new IUdeskTemplateMessageCallBack() {
                    @Override
                    public void templateMsgCallBack(UdeskVideoActivity activity, String jsonValue) {
                        ToastUtils.showToast(getApplicationContext(), "???????????????????????????????????????");
                        activity.sendSystemMessage("???????????????????????????");
                    }
                });
        return builder;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????agentId
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
     * ???????????????????????????????????????4?????????
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