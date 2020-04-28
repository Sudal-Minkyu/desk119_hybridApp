package kr.co.broadwave.kict119;
import android.app.Activity;
import android.widget.Toast;

import androidx.core.widget.TextViewCompat;

/**
 * @author Minkyu
 * Date : 2020-04-14
 * Time :
 * Remark : BackPressCloseHandler
 */

// 뒤로가기 두번누르면 종료되는 클래스
class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    BackPressCloseHandler(Activity activity) {
        this.activity = activity;
    }

    void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(activity,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}