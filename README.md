# UdeskAVSSDK-Android #

## 特别说明
   **androidx 库请使用 main 分支**

   **support 库 请使用support 分支**

## sdk下载地址

[aar包下载地址](https://pro-cs-freq.kefutoutiao.com/doc/im/tid3055/UdeskAVSSDK_1.0.61625630423976.aar)

## 目录
- [一、集成SDK](#1)
- [二、使用SDK](#2)
- [三、SDK API说明](#3)
- [四、更新日志](#4)

<h2 id="1">一、集成SDK</h2>


| Demo中libs下的文件  | 说明                 |
| ---------         | ------------         |
| UdeskAVSSDK       | 视频客服sdk业务驱动包  |        

增量说明：aar 本身大小约1M，其余为依赖库的增量。

各架构so库大小：

armeabi： 4.1M

armeabi-v7a：4.1M

arm64-v8a： 4.9M

其余依赖库共计约 3.5M

增量大小：aar大小+so库大小+其他依赖库大小


### 1、将aar包放到libs下，abi只支持armeabi，armeabi-v7a和arm64-v8a
	
	android {
	    defaultConfig {
	        ndk {
	            abiFilters "armeabi", "armeabi-v7a", "arm64-v8a"
	        }
	    }

	    compileOptions {
	        sourceCompatibility JavaVersion.VERSION_1_8
	        targetCompatibility JavaVersion.VERSION_1_8
	    }
	}
	

### 2、添加远程依赖，若原本已经存在则无需重复添加。glide 请使用4.x+版本,以免出现版本兼容bug。
	
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
    implementation 'io.reactivex.rxjava2:rxjava:2.1.16'
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
    implementation 'com.squareup.retrofit2:retrofit:2.7.1'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.7.0'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.squareup.okhttp3:okhttp:4.9.0'
    implementation 'com.squareup.okio:okio:2.8.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    implementation 'com.tencent.liteav:LiteAVSDK_TRTC:7.3.9133'
    implementation group: 'org.ccil.cowan.tagsoup', name: 'tagsoup', version: '1.2.1'
	
### 3、权限

	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
	<uses-permission android:name="android.permission.RECORD_AUDIO" />
	<uses-permission android:name="android.permission.CAMERA" />
	<uses-permission android:name="android.permission.READ_PHONE_STATE" />
	<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
	<uses-permission android:name="android.permission.BLUETOOTH" />
	<uses-feature android:name="android.hardware.camera" />
	<uses-feature android:name="android.hardware.camera.autofocus" />

### 4、混淆

SDK中使用到了rxjava、retrofit、gson、okhttp、glide，混淆时需要将对应的混淆代码加上，具体混淆代码参照官方文档。如果已存在，不必重复添加。
	
	#------腾讯视频-------------
	-keep class com.tencent.** { *; }
	
	#-----udeskappsdk----------
	-keep class cn.udesk.udeskavssdk.**{*;}
	
	
<h2 id="2">二、使用SDK</h2>

#### 1、从后台管理界面获取app id。

#### 2、生成客户的的唯一标识userId，仅支持字母、数字及下划线,禁用特殊字符。

#### 3、设置UdeskConfig配置信息。

##### 1.  **注意：** **UdeskConfig配置信息为非必须，但是如果有客服列表，则必须设置agentId**

##### 2.  **UdeskConfig内部类Builder的说明**

|属性                          | 设置方法      | 功能说明         |
| -------------               | ------        | ----------      |
|customerInfo                 |setCustomerInfo                 |创建客户信息    |
|agentInfo                    |setAgentInfo                    |创建客服信息，非必传，如果有客服列表，必须设置agentId|
|templateMessageCallBack      |setTemplateMessageCallBack      |结构化消息自定义类型回调|
|templateMessageLinkCallBack  |setTemplateMessageLinkCallBack  |结构化消息链接类型回调|
|templateMessagePhoneCallBack |setTemplateMessagePhoneCallBack |结构化消息phone类型回调|
|isUseVoice                   |setUseVoice                     |是否使用语音模式|
|isShowLogoBg                 |setShowLogoBg		           |是否在大屏界面展示logo 图片|
|logoResId                    |setLogoResId                    |logo 图片资源id|
|logoScaleType                |setLogoScaleType                |logo 图片缩放类型 默认ImageView.ScaleType.FIT_START|
|waitBgResId                  |setWaitBgResId                  |等待呼叫 图片资源id|
|waitBgScaleType			  |setWaitBgScaleType			   |等待呼叫 图片缩放类型 默认ImageView.ScaleType.FIT_XY|

示例

    private UdeskConfig.Builder makeBuilder() {
        UdeskConfig.Builder builder = new UdeskConfig.Builder();
        builder.setAgentInfo(buildAgentInfo())
                .setCustomerInfo(buildCustomerInfo())
                .setUseVoice(true)
                .setShowLogoBg(true)
                .setLogoResId(R.drawable.udesk_avs_customer_default_bg)
                .setLogoScaleType(ImageView.ScaleType.FIT_START)
                .setWaitBgResId(R.drawable.udesk_logo_test)
                .setWaitBgScaleType(ImageView.ScaleType.FIT_START)
                .setTemplateMessageLinkCallBack(new ITemplateMessageLinkCallBack() {
                    @Override
                    public void templateMsgLinkCallBack(UdeskVideoActivity activity, String url) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息链接回调");
                    }
                })
                .setTemplateMessagePhoneCallBack(new IUdeskTemplateMessagePhoneCallBack() {
                    @Override
                    public void templateMsgPhoneCallBack(UdeskVideoActivity activity, String jsonValue) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息电话回调");
                    }
                })
                .setTemplateMessageCallBack(new IUdeskTemplateMessageCallBack() {
                    @Override
                    public void templateMsgCallBack(UdeskVideoActivity activity, String jsonValue) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息自定义回调");
                    }
                });
        return builder;
    }



##### 3. 创建客户信息 

|字段         |功能说明  |
| -----------|--------- |
|nickName    |客户昵称 |
|avatar      |客户头像 |
|email       |客户邮箱 |   
|level       |客户等级 |
	
示例

    private Map<String, Object> buildAgentInfo() {
        HashMap<String, Object> hashMap = new HashMap<>(8);
		hashMap.put("nickName", “xxxxxx”);
		hashMap.put("avatar", “xxxxxx”);
		hashMap.put("email", “xxxxxx”);
		hashMap.put("level", “xxxxxx”);
        return hashMap;
    }


##### 4.创建客服信息（非必传，如果有客服列表，必须设置agentId）

|字段                |功能说明         |
| -------------     |----------      |
|customChannel      |自定义渠道（根据此配置路由） |
|agentId            |客服id|
|agentGroupId       |客服组id|   
	
示例

    private Map<String, Object> buildAgentInfo() {
        HashMap<String, Object> hashMap = new HashMap<>(8);
		hashMap.put("customChannel", “xxxxxx”);
		hashMap.put("agentId", “xxxxxx”);
		hashMap.put("agentGroupId", “xxxxxx”);
        return hashMap;
    }

#### 4、设置呼叫回调 IUdeskCallback（非必须）。

	public interface IUdeskCallback {
	    /**
	     * 呼叫是否成功 true 成功 false 失败
	     */
	    void onSuccess(boolean isSuccess);
	
	    /**
	     * 异常回调
	     */
	    void onError(String throwable);
	}


#### 5、呼叫视频客服

	 /**
     * 呼叫客服 无配置无回调
     */
    UdeskAVSSDKManager.getInstance().call(getApplicationContext(), appId, userId)
    /**
     * 呼叫客服 无配置有回调
     */
    UdeskAVSSDKManager.getInstance().call(getApplicationContext(), appId, userId, iUdeskCallback)
    /**
     * 呼叫客服 有配置无回调
     */
    UdeskAVSSDKManager.getInstance().call(getApplicationContext(), appId, userId, makeBuilder().build())

    /**
     * 呼叫客服 有配置有回调
     */
    UdeskAVSSDKManager.getInstance().call(getApplicationContext(), appId, userId, makeBuilder().build(),iUdeskCallback)

<h2 id="3">三、SDK API说明</h2>

#### 1、退出后，资源释放
 
	UdeskAVSSDKManager.getInstance().clear()

#### 2、发送消息api

在回调中中支持发送系统提示消息（目前只支持纯文本）

	udeskVideoActivity.sendSystemMessage(text);

示例：

	builder.setTemplateMessageLinkCallBack(new ITemplateMessageLinkCallBack() {
                    @Override
                    public void templateMsgLinkCallBack(UdeskVideoActivity activity, String url) {
                        activity.sendSystemMessage("点击链接成功");
                    }
                })

#### 3、查询聊天记录

方式一、使用sdk默认的

	UdeskAVSSDKManager.getInstance().queryChatRecord();	

方式二、提供UdeskChatRecordFragment展示聊天记录，自行集成到app中。


<h2 id="4">四、更新日志</h2>


#### 1.0.5 (support 分支)，1.0.6 (main分支)

1. 支持满意度评价
2. 支持客服客户设置背景图片
3. 支持设置全屏展示自定义背景缩放方式
4. 支持设置呼叫等待界面图片和缩放方式 

#### 1.0.3 (support 分支)，1.0.4 (main分支)

1. 支持查看历史消息记录
2. 增加全屏模式
3. 支持全屏展示自定义背景
4. 修复已知问题

#### 1.0.1 (support 分支)，1.0.2 (main分支)

1. 支持发送系统消息
2. 支持语音聊天功能 

#### 1.0.0 

1. 支持视频聊天功能