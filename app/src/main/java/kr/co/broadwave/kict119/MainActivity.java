package kr.co.broadwave.kict119;

import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @author Minkyu
 * Date : 2020-04-13
 * Time :
 * Remark : MainActivity
 */
public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;

    WebView mWebView;
    TextView errorVeiw;

    @Override
    public void onBackPressed() {
//        Log.i(this.getClass().getName(),mWebView.getUrl()); //로그찍기
        if (mWebView.getUrl().equals("http://192.168.0.131:8080/")) {  //현재접속되있는 페이지의 url을 가져온다.
            backPressCloseHandler.onBackPressed();
        }else{
            mWebView.goBack();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //타이틀바 가리기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        backPressCloseHandler = new BackPressCloseHandler(this);

        errorVeiw = findViewById(R.id.net_error_view);
        mWebView = findViewById(R.id.activity_main_webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d("FCM Log","Refreshed token: "+ token);


        mWebView.setDownloadListener (new DownloadListener()  {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                try {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("다운로드중...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "가이드라인을 다운로드완료", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        } else {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                    }
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

//            //네트워크연결에러
//            @Override
//            public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
//
//                switch(errorCode) {
//                    case ERROR_AUTHENTICATION: break;               // 서버에서 사용자 인증 실패
//                    case ERROR_BAD_URL: break;                           // 잘못된 URL
//                    case ERROR_CONNECT: break;                          // 서버로 연결 실패
//                    case ERROR_FAILED_SSL_HANDSHAKE: break;    // SSL handshake 수행 실패
//                    case ERROR_FILE: break;                                  // 일반 파일 오류
//                    case ERROR_FILE_NOT_FOUND: break;               // 파일을 찾을 수 없습니다
//                    case ERROR_HOST_LOOKUP: break;           // 서버 또는 프록시 호스트 이름 조회 실패
//                    case ERROR_IO: break;                              // 서버에서 읽거나 서버로 쓰기 실패
//                    case ERROR_PROXY_AUTHENTICATION: break;   // 프록시에서 사용자 인증 실패
//                    case ERROR_REDIRECT_LOOP: break;               // 너무 많은 리디렉션
//                    case ERROR_TIMEOUT: break;                          // 연결 시간 초과
//                    case ERROR_TOO_MANY_REQUESTS: break;     // 페이지 로드중 너무 많은 요청 발생
//                    case ERROR_UNKNOWN: break;                        // 일반 오류
//                    case ERROR_UNSUPPORTED_AUTH_SCHEME: break; // 지원되지 않는 인증 체계
//                    case ERROR_UNSUPPORTED_SCHEME: break;          // URI가 지원되지 않는 방식
//                }
//
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                mWebView.setVisibility(View.GONE);
//                errorVeiw.setVisibility(View.VISIBLE);
//            }
        });

        mWebView.loadUrl("http://192.168.0.131:8080");

    }

}

