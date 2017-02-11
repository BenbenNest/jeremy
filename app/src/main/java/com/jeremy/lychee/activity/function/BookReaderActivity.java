package com.jeremy.lychee.activity.function;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.jeremy.lychee.R;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.DeviceUtil;
import com.jeremy.lychee.utils.FileUtil;
import com.jeremy.lychee.utils.StringUtil;
import com.jeremy.lychee.widget.BookReader.BookPageFactory;
import com.jeremy.lychee.widget.BookReader.PageFactory;
import com.jeremy.lychee.widget.BookReader.PageWidget;
import com.jeremy.lychee.widget.BookReader.PageWidget2;
import com.jeremy.lychee.widget.BookReader.TextReader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import butterknife.Bind;

/**
 * Created by benbennest on 16/6/7.
 */
public class BookReaderActivity extends Activity {

    private PageFactory mPageFactory;

    @Bind(R.id.book_reader)
    TextReader textReader;

    private PageWidget mPageWidget;
    private PageWidget2 mPageWidget2;

    private BookPageFactory pagefactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
//        setContentView(R.layout.activity_book_reader);
//        ButterKnife.bind(this);
//        String root = AppUtil.getAppSdRootPath();
//        File rootDir = Environment.getExternalStorageDirectory();
//        File[] files = rootDir.listFiles(new myFileFilter());
//        initText();

//        setContentView(new ReaderWidget(this));

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = DeviceUtil.getScreenWidth(this);
        int height = DeviceUtil.getScreenHeight(this);
        mPageWidget = new PageWidget(this, width, height);
        setContentView(mPageWidget);

        String path = AppUtil.getAppSdRootPath() + "novel.txt";
        mPageFactory = new PageFactory(width, height, displayMetrics.density);
//        mPageFactory.setBackground(BitmapFactory.decodeResource(this.getResources(), R.drawable.reader_bg_green));
        try {
            mPageFactory.openBook(path);
//            mPageFactory.onDraw(mCurPageCanvas);
            mPageFactory.onDraw(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        mPageWidget.setCurrentBitmap(mCurPageBitmap);
        mPageWidget.setPageFactory(mPageFactory);
        mPageWidget.setBitmaps(mPageFactory.mPrePageBitmap, mPageFactory.mCurPageBitmap, mPageFactory.mNextPageBitmap);


    }

    private void initText() {
        byte[] b = FileUtil.getBytesFromAssertFile(this, "cache/novel.txt");

        String text = StringUtil.getString(b);

        textReader.setText(text);


    }

    public class myFileFilter implements FileFilter {

        public boolean accept(File pathname) {
            String filename = pathname.getName().toLowerCase();
            if (filename.contains(".txt")) {
                return false;
            } else {
                return true;
            }
        }
    }

    //    /**
//     * Called when the activity is first created.
//     */
//    private BookWidget mPageWidget;
//    Bitmap mCurPageBitmap, mNextPageBitmap;
//    Canvas mCurPageCanvas, mNextPageCanvas;
//    BookPageFactory pagefactory;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        mPageWidget = new BookWidget(this);
//        setContentView(mPageWidget);
//
//        mCurPageBitmap = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
//        mNextPageBitmap = Bitmap.createBitmap(480, 800,
//                Bitmap.Config.ARGB_8888);
//
//        mCurPageCanvas = new Canvas(mCurPageBitmap);
//        mNextPageCanvas = new Canvas(mNextPageBitmap);
//        pagefactory = new BookPageFactory(480, 800);
//
//        pagefactory.setBgBitmap(BitmapFactory
//                .decodeResource(this.getResources(), R.drawable.shelf_bkg));
//
//        try {
//            String path = Environment.getExternalStorageDirectory().getPath();
//            Log.v("path", path);
//            // pagefactory.openbook("/mnt/sdcard/z8806c.txt");
//            pagefactory.openbook(path + "/z8806c.txt");
//            pagefactory.onDraw(mCurPageCanvas);
//        } catch (IOException e1) {
//            Toast.makeText(this, "电子书不存在,请将《z8806c.txt》放在SD卡根目录下,可以超过100M容量",
//                    Toast.LENGTH_LONG).show();
//        }
//
//        mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
//
//        mPageWidget.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent e) {
//                // TODO Auto-generated method stub
//
//                boolean ret = false;
//                if (v == mPageWidget) {
//                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
//                        mPageWidget.abortAnimation();
//                        mPageWidget.calcCornerXY(e.getX(), e.getY());
//
//                        pagefactory.onDraw(mCurPageCanvas);
//                        if (mPageWidget.DragToRight()) {
//                            try {
//                                pagefactory.getPrePageLines();
//                            } catch (IOException e1) {
//                                // TODO Auto-generated catch block
//                                e1.printStackTrace();
//                            }
//                            if (pagefactory.isfirstPage())
//                                return false;
//                            pagefactory.onDraw(mNextPageCanvas);
//                        } else {
//                            try {
//                                pagefactory.nextPage();
//                            } catch (IOException e1) {
//                                // TODO Auto-generated catch block
//                                e1.printStackTrace();
//                            }
//                            if (pagefactory.islastPage()) {
//                                return false;
//                            }
//                            pagefactory.onDraw(mNextPageCanvas);
//                        }
//                        mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
//                    }
//
//                    ret = mPageWidget.doTouchEvent(e);
//                    return ret;
//                }
//
//                return false;
//            }
//
//        });
//    }

}
