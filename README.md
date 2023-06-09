# UdeskAVSSDK-Android #

## 特别说明
   **androidx 库请使用 main 分支**

   **support 库 请使用support 分支**

## sdk下载地址

[aar包下载地址](https://pro-cs-freq.kefutoutiao.com/doc/im/tid3055/UdeskAVSSDK_1.0.131655811477093.aar)

[demo下载地址](https://pro-cs-freq.kefutoutiao.com/doc/im/tid3055/udeskavs-1.0.131655811453092.apk)

## 目录
- [一、集成SDK](#1)
- [二、使用SDK](#2)
- [三、SDK API说明](#3)
- [四、更新日志](#4)
- [五、常见问题](#5)

<h2 id="1">一、集成SDK</h2>


| Demo中libs下的文件  | 说明                 |
| ---------         | ------------         |
| UdeskAVSSDK       | 视频客服sdk业务驱动包  |        

增量说明：aar 本身大小约1M，其余为依赖库的增量。

各架构so库大小：

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
	
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    implementation 'com.android.support:design:28.0.0'
    implementation 'com.android.support.constraint:constraint-layout:2.0.4'
    implementation 'android.arch.lifecycle:extensions:1.1.1'

    implementation 'io.reactivex.rxjava2:rxjava:2.1.16'
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
    implementation 'com.squareup.retrofit2:retrofit:2.7.1'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.7.0'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.squareup.okhttp3:okhttp:4.9.0'
    implementation 'com.squareup.okio:okio:2.8.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'

    implementation 'com.github.bumptech.glide:glide:4.9.0'
    implementation 'com.tencent.liteav:LiteAVSDK_TRTC:10.0.0.11953'
    implementation group: 'org.ccil.cowan.tagsoup', name: 'tagsoup', version: '1.2.1'
	
### 3、权限

	使用第三方腾讯sdk 需要以下敏感权限：
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
	<uses-permission android:name="android.permission.RECORD_AUDIO" />
	<uses-permission android:name="android.permission.CAMERA" />
	<uses-permission android:name="android.permission.READ_PHONE_STATE" />
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
|localViewRotation            |setLocalViewRotation            |本地预览画面的顺时针旋转角度，默认 UdeskVideoRotation.UdeskVideoRotation_0|
|localViewFillMode            |setLocalViewFillMode            |本地预览画面的填充模式，默认 UdeskVideoFillMode.UdeskVideoFillMode_Fill|
|remoteViewRotation           |setRemoteViewRotation           |远端视频画面的逆时针旋转角度，默认 UdeskVideoRotation.UdeskVideoRotation_0|
|remoteViewFillMode           |setRemoteViewFillMode           |远端视频画面的填充模式，默认 UdeskVideoFillMode.UdeskVideoFillMode_Fill|
|noteInfo                     |setNoteInfo                     |设置业务记录|
|useScreenShare				  |setUseScreenShare               |是否使用屏幕分享|
|screenShareCallBack          |setScreenShareCallBack          |屏幕分享回调|
|useZoom                      |setUseZoom                      |是否使用缩放功能|
|zoomCallBack                 |setZoomCallBack                 |缩放按钮点击回调|

示例

    private UdeskConfig.Builder makeBuilder() {
        UdeskConfig.Builder builder = new UdeskConfig.Builder();
        builder.setAgentInfo(buildAgentInfo())
                .setNoteInfo(buildNoteInfo())
                .setCustomerInfo(buildCustomerInfo())
                .setUseVoice(useVoice.isChecked())
                .setShowLogoBg(showLogo.isChecked())
                .setLogoResId(R.drawable.udesk_logo_test)
                .setLogoScaleType(ImageView.ScaleType.FIT_START)
                .setRemoteViewFillMode(fillMode)
                .setRemoteViewRotation(rotation)
                .setLocalViewFillMode(fillMode)
                .setLocalViewRotation(rotation)
                .setUseScreenShare(showScreenShare.isChecked())
                .setUseZoom(showZoom.isChecked())
                .setTemplateMessageLinkCallBack(new ITemplateMessageLinkCallBack() {
                    @Override
                    public void templateMsgLinkCallBack(UdeskVideoActivity activity, String url) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息链接回调");
                        activity.sendSystemMessage("点击链接成功");
                    }
                })
                .setTemplateMessagePhoneCallBack(new IUdeskTemplateMessagePhoneCallBack() {
                    @Override
                    public void templateMsgPhoneCallBack(UdeskVideoActivity activity, String jsonValue) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息电话回调");
                        activity.sendSystemMessage("点击电话成功");
                    }
                })
                .setTemplateMessageCallBack(new IUdeskTemplateMessageCallBack() {
                    @Override
                    public void templateMsgCallBack(UdeskVideoActivity activity, String jsonValue) {
                        ToastUtils.showToast(getApplicationContext(), "这个是结构化消息自定义回调");
                        activity.sendSystemMessage("点击自定义消息成功");
                    }
                })
                .setZoomCallBack(new IUdeskZoomCallBack() {
                    @Override
                    public void zoomCallBack(UdeskVideoActivity activity) {
                        ToastUtils.showToast(getApplicationContext(), "缩放回调");
                    }
                })
                .setScreenShareCallBack(new IUdeskScreenShareCallBack() {
                    @Override
                    public void screenShareCallBack(UdeskVideoActivity activity, boolean status) {
                        if (status){
                            ToastUtils.showToast(getApplicationContext(), "开启屏幕分享回调");
                        }else {
                            ToastUtils.showToast(getApplicationContext(), "关闭屏幕分享回调");
                        }
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
|customFields |客户自定义字段 |
	
示例

    private Map<String, Object> buildCustomerInfo() {
        HashMap<String, Object> hashMap = new HashMap<>(8);
		hashMap.put("nickName", “xxxxxx”);
		hashMap.put("avatar", “xxxxxx”);
		hashMap.put("email", “xxxxxx”);
		hashMap.put("level", “xxxxxx”);
		HashMap<String, Object> customerInfoMap = new HashMap<>();
		 /**
	      * 文本型字段：举例 "field_name": "TextField_684", 取“field_name”对应的value值作为自定义字段key值进行赋值
	      */
		customerInfoMap.put("TextField_684", "xxxxxxx");
		/**
	      * 选择型字段：举例 "field_name": "SelectField_457",
		  *	"options": [
		  *		{
		  *   	  "0": "男"
		  * 	}, 
		  * 	{
		  *    	  "1": "女"
		  *  	}
		  *  ] , 取“field_name”对应的value值作为自定义字段key值进行赋值,取"options"中的某一项key值作为value
	      */
		customerInfoMap.put("SelectField_457", "1");
		hashMap.put("customFields",customerInfoMap);
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

##### 6. 创建业务记录

|字段                |功能说明         |
| -------------     |----------      |
|noteInfo           |业务记录对象 |
|noteInfo.content   |业务记录主题|
|noteInfo.customFields       |业务记录自定义字段|  

    private Map<String, Object> buildNoteInfo() {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> noteInfoMap = new HashMap<>();
        //设置业务记录主题
        noteInfoMap.put("content",noteInfoContent.getText().toString());
        //设置业务记录自定义字段
        HashMap<Object, Object> customMap = new HashMap<>();
        //业务记录自定义字段TextField 管理员后台拿key
		customMap.put(key1, value1);
        //业务记录自定义字段TextField 管理员后台拿key,value 是角标字符串数组
		customMap.put(key2, value2);
        noteInfoMap.put("customFields", customMap);
        hashMap.put("noteInfo",noteInfoMap);
        return hashMap;
    }


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

#### 1.0.13 (support 分支)，1.0.14 (main分支)

1. 支持屏幕分享

#### 1.0.11 (support 分支)，1.0.12 (main分支)

1. 支持排队提示语和背景自定义
2. 支持业务记录传参
3. 支持询前表单
4. 支持摄像头朝向自定义
5. 支持视频logo自定义
6. 视频聊天界面优化

#### 1.0.9 (support 分支)，1.0.10 (main分支)

1. 支持log日志上传
2. 修复已知问题及优化

#### 1.0.7 (support 分支)，1.0.8 (main分支)

1. 支持最大排队人数设置
2. 支持设置视频界面旋转
3. 修改权限声明
4. 修改图片消息显示样式
5. 修复已知问题 


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

<h2 id="5">五、常见问题</h2>

#### 1、屏幕分享在Android targetSdkVersion设置为30，屏幕分享出现崩溃解决方案

在使用SDK时，将将targetSdkVersion设置为30，进行屏幕分享时会出现崩溃，这主要是因为谷歌隐私策略导致的

崩溃堆栈
	
		Caused by: java.lang.SecurityException: Media projections require a foreground service of type ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
解决步骤

第一步
创建一个service ，并绑定一个Notification使其作为前台service
	
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Call Start foreground with notification
            Intent notificationIntent = new Intent(this, MediaService.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Starting Service")
                    .setContentText("Starting monitoring service")
                    .setContentIntent(pendingIntent);
            Notification notification = notificationBuilder.build();
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NOTIFICATION_CHANNEL_DESC);
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(channel);
            startForeground(1, notification); //必须使用此方法显示通知，不能使用notificationManager.notify，否则还是会报上面的错误
        }

第二步
在AndroidManifest.xml中配置

1.加入以下权限

	<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

2.设置service的type为mediaProjection

	<service
            android:name=".MediaService"
            android:enabled="true"
            android:exported="true"
            android:foregroundServiceType="mediaProjection" />

最后一步

在录屏直播前启动service