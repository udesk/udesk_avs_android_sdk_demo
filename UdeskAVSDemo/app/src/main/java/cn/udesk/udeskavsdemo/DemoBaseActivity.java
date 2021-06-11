package cn.udesk.udeskavsdemo;

import android.content.pm.PackageManager;

import android.support.annotation.NonNull;

import cn.udesk.udeskavssdk.ui.activity.UdeskBaseActivity;
import cn.udesk.udeskavssdk.utils.ToastUtils;
import cn.udesk.udeskavssdk.widget.LoadingDialog;

/**
 * @author admin
 */
public abstract class DemoBaseActivity extends UdeskBaseActivity {
    private LoadingDialog loadingdialog;
    public static final String APPID = "appId";
    public static final String USERID = "userId";


    protected void showloadingDialog(String dialogTitle) {
        try {
            hideLoadingDialog();
            loadingdialog = new LoadingDialog(this, R.style.UdeskAVSDialogStyle,
                    R.layout.udesk_avs_dialog_request_view, dialogTitle);
            if (!loadingdialog.isShowing()) {
                loadingdialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideLoadingDialog() {
        try {
            if (loadingdialog != null) {
                loadingdialog.dismiss();
                loadingdialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected abstract void call();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (grantResults.length > 0) {
                boolean ret = true;
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        ret = false;
                        break;
                    }
                }
                handlePermissionResult(ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handlePermissionResult(boolean ret) {
        try {
            if (!ret) {
                ToastUtils.showToast(getApplicationContext(), getString(R.string.udesk_avs_toast_grant_permission));
            } else {
                call();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
