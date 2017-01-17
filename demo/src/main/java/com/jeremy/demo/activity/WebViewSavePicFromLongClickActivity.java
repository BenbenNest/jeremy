package com.jeremy.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.jeremy.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewSavePicFromLongClickActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_save_pic_from_long_click);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        webview.loadUrl("http://www.baidu.com");
        // 长按点击事件
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = webview.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    // 弹出保存图片的对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebViewSavePicFromLongClickActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("保存图片到本地");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String url = hitTestResult.getExtra();
                            // 下载图片到本地
//                            DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener(){
//
//                                @Override
//                                public void getDownPath(String s) {
//                                    Toast.makeText(context,"下载完成",Toast.LENGTH_LONG).show();
//                                    Message msg = Message.obtain();
//                                    msg.obj=s;
//                                    handler.sendMessage(msg);
//                                }
//                            });

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // 自动dismiss
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });
    }


}
