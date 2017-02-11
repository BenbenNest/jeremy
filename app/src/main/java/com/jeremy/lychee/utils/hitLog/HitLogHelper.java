package com.jeremy.lychee.utils.hitLog;

import android.text.TextUtils;

import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.utils.AppUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rx.Observable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by daibangwen-xy on 15/12/18.
 */
public class HitLogHelper {
    private final static String TAG = "HitLogHelper";
    private  final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static int sMaxLine = 1000;

    public  static HitLogHelper hitLogHelper = null;

    public static HitLogHelper getHitLogHelper() {
        if (hitLogHelper == null) {
            hitLogHelper = new HitLogHelper();
        }
        return hitLogHelper;
    }
    public void hitLog(final String logString, Action0 onCompleted){
        if (Constants.HILOG_ENABLE) {
            //异步将日志写入本地文件中
            Observable.just(logString)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            s -> {
                                Logger.t("hitlog").d("write to file");
                                writeToFile(lock, logString);
                                if (onCompleted != null) onCompleted.call();
                            },
                            e -> {
                                e.printStackTrace();
                                if (onCompleted != null) onCompleted.call();
                            },
                            () -> {
                                if (onCompleted != null) onCompleted.call();
                            }
                    );
        }
    }


    //将日志上传到网络
    public void postLogToNet(){
        if (Constants.HILOG_ENABLE) {
            Logger.t("hitlog").d("post to net");
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .map(s -> listToSB(readLogsFromFile()))
                    .filter(s -> s != null && !TextUtils.isEmpty(s))
                    .flatMap(this::postLog)
                    .subscribe(s -> {
                                if (s == 0) {
                                    Logger.v("打点上传成功");
                                }
                            },
                            Throwable::printStackTrace
                    );


            //1.获取本地数据
            //readLogsFromFile();
            //2.讲日志list转为string
            //String logString = listToSB();
            //3.上传
        }
    }


    public Observable<Integer> postLog(final String logString) {
        String sign = AppUtil.hitLogSignString(logString);
        return OldRetroAdapter.getService().postHitLogs(logString, sign)
                .map(ModelBase::getErrno)
                .subscribeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace);
    }

    public  void writeToFile(ReentrantReadWriteLock lock,String log){
        try {
            lock.writeLock().lock();
            File fileDir = new File(Constants.APP_ROOT_PATH);
            if (!fileDir.isDirectory()){
               boolean ss = fileDir.mkdir();
            }
            File file = new File(Constants.APP_ROOT_PATH +Constants.HITLOG_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            //content=System.currentTimeMillis()+content;
            Logger.t(TAG).d("HitLog: " + log);
            bw.write(log);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    //从文件获取日志
    public synchronized List<String> readLogsFromFile(){
        StringBuilder sb = new StringBuilder();
        List<String> logList = new ArrayList<>();
        try {
            lock.readLock().lock();
            File fileDir = new File(Constants.APP_ROOT_PATH);
            if (!fileDir.isDirectory()){
                fileDir.mkdir();
            }
            File file = new File(Constants.APP_ROOT_PATH +Constants.HITLOG_FILE);
            if (!file.exists()){
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(Constants.APP_ROOT_PATH + Constants.HITLOG_FILE);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line="";
            int i=0;
            while ((line= br.readLine())!=null) {
                logList.add(line);
                i++;
                if (i> sMaxLine){    //最多获取maxline条数据 剩下的丢弃
                    break;
                }
            }
            br.close();
            isr.close();
            fis.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            File file = new File(Constants.APP_ROOT_PATH +Constants.HITLOG_FILE);
            if (file.exists()){
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lock.readLock().unlock();
        }
        return logList;
    }

    public  String listToSB(List<String> list){
        final StringBuilder sb = new StringBuilder();
        if (list.size()>0){
            for(int i =0;i<list.size();i++){
                sb.append(list.get(i));
                if (i!=list.size()-1){
                    sb.append("\n");
                }
            }
        }
        return  sb.toString();
    }

}
