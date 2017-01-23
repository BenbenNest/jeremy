package com.jeremy.demo.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.demo.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckUpdateActivity extends AppCompatActivity {
    static final int UPDATA_CLIENT = 1;
    static final int GET_UNDATAINFO_ERROR = 2;
    static final int DOWN_ERROR = 3;
    static final int DOWN_FINISH = 4;
    private String TAG = "CheckUpdateActivity";
    private UpdataInfo info;

    @BindView(R.id.tv_update)
    TextView tvUpdate;
    CheckVersionTask checkVersionTask;
    private static int state = 0;
    File file;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_SHORT).show();
                    break;
                case DOWN_FINISH:
                    installApk(file);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_update);
        ButterKnife.bind(this);
        checkVersionTask = new CheckVersionTask();
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(checkVersionTask)).start();
            }
        });
    }

    /*
     * 从服务器获取xml解析并进行比对版本号
     */
    public class CheckVersionTask implements Runnable {

        public void run() {
            try {
                state++;
                if (state % 2 == 0) {
                    Log.i(TAG, "版本号相同无需升级");
                    Looper.prepare();
                    Toast.makeText(CheckUpdateActivity.this, "版本号相同无需升级", Toast.LENGTH_SHORT).show();
                    Looper.loop();
//                    ToastUtils.show(CheckUpdateActivity.this, "版本号相同无需升级", Toast.LENGTH_SHORT);
//                    Toast.makeText(CheckUpdateActivity.this, "版本号相同无需升级", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "版本号不同 ,提示用户升级 ");
                    Message msg = new Message();
                    msg.what = UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }
                //从资源文件获取服务器 地址
//                String path = getResources().getString(R.string.serverurl);
//                String path = "http://download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk";
//                //包装成url的对象
//                URL url = new URL(path);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setConnectTimeout(5000);
//                InputStream is = conn.getInputStream();
//                info = getUpdataInfo(is);
//
//                if (info.getVersion().equals(getVersionName())) {
//                    Log.i(TAG, "版本号相同无需升级");
//
//                } else {
//                    Log.i(TAG, "版本号不同 ,提示用户升级 ");
//                    Message msg = new Message();
//                    msg.what = UPDATA_CLIENT;
//                    handler.sendMessage(msg);
//                }
            } catch (Exception e) {
                // 待处理
//                Message msg = new Message();
//                msg.what = GET_UNDATAINFO_ERROR;
//                handler.sendMessage(msg);
//                e.printStackTrace();
            }
        }
    }

    /*
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤：
     * 	1.创建alertDialog的builder.
     *	2.要给builder设置属性, 对话框的内容,样式,按钮
     *	3.通过builder 创建一个对话框
     *	4.对话框show()出来
     */
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("版本升级");
//        builer.setMessage(info.getDescription());
        builer.setMessage("发现新版本，是否现在升级？");

        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
                downLoadApk();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer("http://download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk", pd);
//                    sleep(3000);
//                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }


    /*
    * 获取当前程序的版本号
    */
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    public File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            pd.dismiss();
            handler.sendEmptyMessage(DOWN_FINISH);
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /*
 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
 */
    public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");//设置解析的数据源
        int type = parser.getEventType();
        UpdataInfo info = new UpdataInfo();//实体
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        info.setVersion(parser.nextText());    //获取版本号
                    } else if ("url".equals(parser.getName())) {
                        info.setUrl(parser.nextText());    //获取要升级的APK文件
                    } else if ("description".equals(parser.getName())) {
                        info.setDescription(parser.nextText());    //获取该文件的信息
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }

    static class UpdataInfo {
        private String version;
        private String url;
        private String description;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


//    <?xml version="1.0" encoding="utf-8"?>
//    <info>
//    <version>2.0</version>
//    <url>http://192.168.1.187:8080/mobilesafe.apk</url>
//    <description>检测到最新版本，请及时更新！</description>
//    </info>

}
