package com.jeremy.lychee.widget.BookReader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import com.jeremy.lychee.utils.DisplayUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

/**
 * Created by benbennest on 16/7/2.
 */
public class PageFactory {
//    0000 1010	10	0A	LF	换行键  /n
//    0000 1101	13	0D	CR	回车键  /r
//    换行符‘\n’和回车符‘\r’
//    顾名思义，换行符就是另起一行，回车符就是回到一行的开头，所以我们平时编写文件的回车符应该确切来说叫做回车换行符
//    '\n' 10 换行（newline）
//            '\r' 13 回车（return）
//
//    也可以表示为'\x0a'和'\x0d'.(16进制)
//    在windows系统下，回车换行符号是"\r\n".但是在Linux等系统下是没有"\r"符号的。
//    在解析文本或其他格式的文件内容时，常常要碰到判定回车换行的地方，这个时候就要注意既要判定"\r\n"又要判定"\n"。
//    写程序时可能得到一行,将其进行trim掉'\r',这样能得到你所需要的string了。

    private String mName = "绿野仙踪";
    private int mHeaderHeight;
    private int mWidth, mHeight, mFontSize, mTitleFontSize;
    private MappedByteBuffer mMappedByteBuffer = null;
    private long mFileLength;
    private Paint mPaint;
    private boolean mIsFirstPage = true, mIsLastPage;
    private String mEncode = Constants.ENCODE_UTF8;
    private int mPaddingLeft = 15, mPaddingtop = 15, mPaddingRight = 5, mPaddingBottom = 15;
    private int mTextWidth, mTextHeight, mLineCount, mLineSpacing = 15;
    private int mBufferBegin = 0, mBufferEnd = 0;
    private int mPreBufferBegin = 0, mPreBufferEnd = 0;
    private int mCurBufferBegin = 0, mCurBufferEnd = 0;
    private int mNextBufferBegin = 0, mNextBufferEnd = 0;

    private Vector<String> mLines = new Vector<String>();
    private Bitmap mBackground = null;
    private int mBackgroundColor = 0xffff9e85; // 背景颜色
    public Bitmap mPrePageBitmap, mCurPageBitmap, mNextPageBitmap;
    public Canvas mPrePageCanvas, mCurPageCanvas, mNextPageCanvas;
    private int mCurrentPage = 0;

    public PageFactory(int width, int height, float density) {
        mHeaderHeight = DisplayUtil.dip2px(Constants.DEFAULT_HEADER_HEIGHT, density);
        mTitleFontSize = DisplayUtil.sp2px(Constants.DEFAULT_TITLE_FONT_SIZE, density);
        mFontSize = DisplayUtil.sp2px(Constants.DEFAULT_FONT_SIZE, density);
        mWidth = width;
        mHeight = height;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(mFontSize);
        mPaint.setColor(Color.BLACK);
        mTextWidth = mWidth - mPaddingLeft - mPaddingRight;
        mTextHeight = mHeight - mTextHeight * 2 - mHeaderHeight;
        mLineCount = (int) (mTextHeight / (mFontSize + mLineSpacing)); // 可显示的行数
        mPrePageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCurPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mPrePageCanvas = new Canvas(mPrePageBitmap);
        mCurPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
    }

    public void openBook(String path) throws IOException {
        File file = new File(path);
        mFileLength = file.length();
        mMappedByteBuffer = new RandomAccessFile(file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, mFileLength);
//        nextPage();
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public boolean isLastPage() {
        if (mCurBufferEnd >= mFileLength) {
            mIsLastPage = true;
        } else {
            mIsLastPage = false;
        }
        return mIsLastPage;
    }

    public boolean isFirstPage() {
        if (mCurBufferBegin <= 0) {
            mIsFirstPage = true;
        } else {
            mIsFirstPage = false;
        }
        return mIsFirstPage;
    }

    public Vector<String> getPrePageLines() {
        Vector<String> lines = new Vector<String>();
        mPreBufferEnd = mPreBufferBegin;
        String strParagraph = "";
        while (lines.size() < mLineCount && mPreBufferBegin > 0) {
            Vector<String> paraLines = new Vector<String>();
            byte[] paraBuf = readPreParagraph(mPreBufferBegin);
            mPreBufferBegin -= paraBuf.length;
            try {
                strParagraph = new String(paraBuf, mEncode);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "");
            strParagraph = strParagraph.replaceAll("\n", "");

            if (strParagraph.length() == 0) {
                paraLines.add(strParagraph);
            }
            while (strParagraph.length() > 0) {
                int size = mPaint.breakText(strParagraph, true, mTextWidth, null);
                paraLines.add(strParagraph.substring(0, size));
                strParagraph = strParagraph.substring(size);
//                if (mLines.size() >= mLineCount) {
//                    break;
//                }
            }
            lines.addAll(0, paraLines);
        }
        while (lines.size() > mLineCount) {
            try {
                mPreBufferBegin += lines.get(0).getBytes(mEncode).length;
                lines.remove(0);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return lines;
    }

    public void goNextPage() {
        mCurrentPage += 1;
        mPrePageBitmap = mCurPageBitmap;
        mCurPageBitmap = mNextPageBitmap;
        mPreBufferBegin = mCurBufferBegin;
        mPreBufferEnd = mCurBufferEnd;
        mCurBufferBegin = mNextBufferBegin;
        mCurBufferEnd = mNextBufferEnd;

        mNextPageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
        initPage(mNextPageCanvas);
        drawNextPage(mNextPageCanvas);
    }

    public void goPrePage() {
        mCurrentPage -= 1;
        mNextPageBitmap = mCurPageBitmap;
        mCurPageBitmap = mPrePageBitmap;
        mNextBufferBegin = mCurBufferBegin;
        mNextBufferEnd = mCurBufferEnd;
        mCurBufferBegin = mPreBufferBegin;
        mCurBufferEnd = mPreBufferEnd;

        mPrePageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mPrePageCanvas = new Canvas(mPrePageBitmap);
        initPage(mPrePageCanvas);
        drawPrePage(mPrePageCanvas);
    }

    public void nextPage() {
        if (mBufferEnd >= mFileLength) {
            mIsLastPage = true;
            return;
        } else {
            mIsLastPage = false;
        }
        getNextPageLines();
    }

    public Vector<String> getPageLines(int start) {
        Vector<String> lines = new Vector<String>();
        mBufferBegin = mBufferEnd;

        String paragraph = "";
        readNextParagraph(0);
        while (mLines.size() < mLineCount && mBufferEnd < mFileLength) {
            byte[] paragraphByte = readNextParagraph(mBufferEnd);
            mBufferEnd += paragraphByte.length;
            try {
                paragraph = new String(paragraphByte, mEncode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String strReturn = "";
            if (paragraph.indexOf("\r\n") != -1) {
                strReturn = "\r\n";
                paragraph = paragraph.replaceAll("\r\n", "");
            } else if (paragraph.indexOf("\n") != -1) {
                strReturn = "\n";
                paragraph = paragraph.replaceAll("\n", "");
            }
            if (paragraph.length() == 0) {
                mLines.add(paragraph);
            }
            while ((paragraph.length() > 0)) {
                int size = mPaint.breakText(paragraph, true, mTextWidth, null);
                mLines.add(paragraph.substring(0, size));
                paragraph = paragraph.substring(size);
                if (mLines.size() >= mLineCount) {
                    break;
                }
            }
            if (paragraph.length() != 0) {
                try {
                    mBufferEnd -= (paragraph + strReturn).getBytes(mEncode).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }

    public Vector<String> getNextPageLines() {
        mLines.clear();
        mBufferBegin = mBufferEnd;
//        mLines = pageDown();
        String paragraph = "";
        readNextParagraph(0);
        while (mLines.size() < mLineCount && mBufferEnd < mFileLength) {
            byte[] paragraphByte = readNextParagraph(mBufferEnd);
            mBufferEnd += paragraphByte.length;
            try {
                paragraph = new String(paragraphByte, mEncode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String strReturn = "";
            if (paragraph.indexOf("\r\n") != -1) {
                strReturn = "\r\n";
                paragraph = paragraph.replaceAll("\r\n", "");
            } else if (paragraph.indexOf("\n") != -1) {
                strReturn = "\n";
                paragraph = paragraph.replaceAll("\n", "");
            }
            if (paragraph.length() == 0) {
                mLines.add(paragraph);
            }
            while ((paragraph.length() > 0)) {
                int size = mPaint.breakText(paragraph, true, mTextWidth, null);
                mLines.add(paragraph.substring(0, size));
                paragraph = paragraph.substring(size);
                if (mLines.size() >= mLineCount) {
                    break;
                }
            }
            if (paragraph.length() != 0) {
                try {
                    mBufferEnd -= (paragraph + strReturn).getBytes(mEncode).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return mLines;
    }

    // 读取上一段落
    protected byte[] readPreParagraph(int start) {
        int end = start;
        int i;
        byte b0, b1;
        if (mEncode.equals("UTF-16LE")) {
            i = end - 2;
            while (i > 0) {
                b0 = mMappedByteBuffer.get(i);
                b1 = mMappedByteBuffer.get(i + 1);
                if (b0 == 0x0a && b1 == 0x00 && i != end - 2) {
                    i += 2;
                    break;
                }
                i--;
            }

        } else if (mEncode.equals("UTF-16BE")) {
            i = end - 2;
            while (i > 0) {
                b0 = mMappedByteBuffer.get(i);
                b1 = mMappedByteBuffer.get(i + 1);
                if (b0 == 0x00 && b1 == 0x0a && i != end - 2) {
                    i += 2;
                    break;
                }
                i--;
            }
        } else {
            i = end - 1;
            while (i > 0) {
                b0 = mMappedByteBuffer.get(i);
                if (b0 == 0x0a && i != end - 1) {
                    i++;
                    break;
                }
                i--;
            }
        }
        if (i < 0)
            i = 0;
        int size = end - i;
        int j;
        byte[] buf = new byte[size];
        for (j = 0; j < size; j++) {
            buf[j] = mMappedByteBuffer.get(i + j);
        }
        return buf;
    }

    public byte[] readNextParagraph(int start) {
        int i = start;
        byte b0, b1;
        String s = "";
        if (mEncode.equals("UTF-16LE")) {
            while (i < mFileLength - 1) {
                b0 = mMappedByteBuffer.get(i++);
                b1 = mMappedByteBuffer.get(i++);
                if (b0 == 0x0a && b1 == 0x00) {
                    break;
                }
            }
        } else if (mEncode.equals("UTF-16BE")) {
            while (i < mFileLength - 1) {
                b0 = mMappedByteBuffer.get(i++);
                b1 = mMappedByteBuffer.get(i++);
                if (b0 == 0x00 && b1 == 0x0a) {
                    break;
                }
            }
        } else {
            while (i < mFileLength) {
                b0 = mMappedByteBuffer.get(i++);
                if (b0 == 0x0a) {
                    break;
                }
            }
        }
        int size = i - start;
        byte[] bytes = new byte[size];
        for (i = 0; i < size; i++) {
            bytes[i] = mMappedByteBuffer.get(start + i);
        }
        return bytes;
    }

    public void onDraw(int page) {
        mCurrentPage = page;
        //draw background
        if (mBackground == null) {
            mPrePageCanvas.drawColor(mBackgroundColor);
            mCurPageCanvas.drawColor(mBackgroundColor);
            mNextPageCanvas.drawColor(mBackgroundColor);
        } else {
            Matrix matrix = new Matrix();
            matrix.postScale((float) mWidth / mBackground.getWidth(), (float) mHeight / mBackground.getHeight());
            Rect srcRect = new Rect(0, 0, mWidth, mHeight);
            Rect destRect = new Rect(0, 0, mWidth, mHeight);
//                matrix.setSkew(0.5f, 0);
            Bitmap bitmap = Bitmap.createBitmap(mBackground, 0, 0, mBackground.getWidth(), mBackground.getHeight(), matrix, false);
//                Bitmap bitmap = Bitmap.createBitmap(m_book_bg, 0, 0, mWidth, mHeight);
            mPrePageCanvas.drawBitmap(bitmap, srcRect, destRect, mPaint);
            mCurPageCanvas.drawBitmap(bitmap, srcRect, destRect, mPaint);
            mNextPageCanvas.drawBitmap(bitmap, srcRect, destRect, mPaint);
        }

        TextPaint paint = new TextPaint();
        paint.setTextSize(mTitleFontSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        double height = Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        mPrePageCanvas.drawText(mName, mPaddingLeft, (float) (mHeaderHeight + height) / 2, paint);
        mCurPageCanvas.drawText(mName, mPaddingLeft, (float) (mHeaderHeight + height) / 2, paint);
        mNextPageCanvas.drawText(mName, mPaddingLeft, (float) (mHeaderHeight + height) / 2, paint);
        //首次加载没有上一页,只有当前页和下一页
//        drawPrePage(mPrePageCanvas);
        drawCurPage(mCurPageCanvas);
        drawNextPage(mNextPageCanvas);
    }

    public void initPage(Canvas canvas) {
        //draw background
        if (mBackground == null) {
            canvas.drawColor(mBackgroundColor);
        } else {
            Matrix matrix = new Matrix();
            matrix.postScale((float) mWidth / mBackground.getWidth(), (float) mHeight / mBackground.getHeight());
            Rect srcRect = new Rect(0, 0, mWidth, mHeight);
            Rect destRect = new Rect(0, 0, mWidth, mHeight);
//                matrix.setSkew(0.5f, 0);
            Bitmap bitmap = Bitmap.createBitmap(mBackground, 0, 0, mBackground.getWidth(), mBackground.getHeight(), matrix, false);
//                Bitmap bitmap = Bitmap.createBitmap(m_book_bg, 0, 0, mWidth, mHeight);
            canvas.drawBitmap(bitmap, srcRect, destRect, mPaint);
        }

        TextPaint paint = new TextPaint();
        paint.setTextSize(mTitleFontSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        double height = Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        canvas.drawText(mName, mPaddingLeft, (float) (mHeaderHeight + height) / 2, paint);
    }

    public void drawPrePage(Canvas canvas) {
        if (mCurrentPage == 0) {
            return;
        } else {
//            Vector<String> lines = getPrePageLines();
            Vector<String> lines = new Vector<String>();
            mPreBufferEnd = mCurBufferBegin;
            mPreBufferBegin = mPreBufferEnd;
            String strParagraph = "";
            while (lines.size() < mLineCount && mPreBufferBegin > 0) {
                Vector<String> paraLines = new Vector<String>();
                byte[] paraBuf = readPreParagraph(mPreBufferBegin);
                mPreBufferBegin -= paraBuf.length;
                try {
                    strParagraph = new String(paraBuf, mEncode);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                strParagraph = strParagraph.replaceAll("\r\n", "");
                strParagraph = strParagraph.replaceAll("\n", "");

                if (strParagraph.length() == 0) {
                    paraLines.add(strParagraph);
                }
                while (strParagraph.length() > 0) {
                    int size = mPaint.breakText(strParagraph, true, mTextWidth, null);
                    paraLines.add(strParagraph.substring(0, size));
                    strParagraph = strParagraph.substring(size);
//                if (mLines.size() >= mLineCount) {
//                    break;
//                }
                }
                lines.addAll(0, paraLines);
            }
            while (lines.size() > mLineCount) {
                try {
                    mPreBufferBegin += lines.get(0).getBytes(mEncode).length;
                    lines.remove(0);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (lines.size() > 0) {
                int y = mHeaderHeight;
                for (String line : lines) {
                    y += mFontSize + mLineSpacing;
                    canvas.drawText(line, mPaddingLeft, y, mPaint);
                }
            }
        }
    }


    public void drawCurPage(Canvas canvas) {
//        nextPage();
        Vector<String> lines = new Vector<String>();
        mCurBufferBegin = mCurBufferEnd;

        String paragraph = "";
        while (lines.size() < mLineCount && mBufferEnd < mFileLength) {
            byte[] paragraphByte = readNextParagraph(mCurBufferEnd);
            mCurBufferEnd += paragraphByte.length;
            try {
                paragraph = new String(paragraphByte, mEncode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String strReturn = "";
            if (paragraph.indexOf("\r\n") != -1) {
                strReturn = "\r\n";
                paragraph = paragraph.replaceAll("\r\n", "");
            } else if (paragraph.indexOf("\n") != -1) {
                strReturn = "\n";
                paragraph = paragraph.replaceAll("\n", "");
            }
            if (paragraph.length() == 0) {
                lines.add(paragraph);
            }
            while ((paragraph.length() > 0)) {
                int size = mPaint.breakText(paragraph, true, mTextWidth, null);
                lines.add(paragraph.substring(0, size));
                paragraph = paragraph.substring(size);
                if (lines.size() >= mLineCount) {
                    break;
                }
            }
            if (paragraph.length() != 0) {
                try {
                    mCurBufferEnd -= (paragraph + strReturn).getBytes(mEncode).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (lines.size() > 0) {
            int y = mHeaderHeight;
            for (String line : lines) {
                y += mFontSize + mLineSpacing;
                canvas.drawText(line, mPaddingLeft, y, mPaint);
            }
        }
    }

    public void drawNextPage(Canvas canvas) {
//        nextPage();
        Vector<String> lines = new Vector<String>();
        mNextBufferBegin = mCurBufferEnd;
        mNextBufferEnd = mNextBufferBegin;
        String paragraph = "";
        readNextParagraph(0);
        while (lines.size() < mLineCount && mNextBufferEnd < mFileLength) {
            byte[] paragraphByte = readNextParagraph(mNextBufferEnd);
            mNextBufferEnd += paragraphByte.length;
            try {
                paragraph = new String(paragraphByte, mEncode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String strReturn = "";
            if (paragraph.indexOf("\r\n") != -1) {
                strReturn = "\r\n";
                paragraph = paragraph.replaceAll("\r\n", "");
            } else if (paragraph.indexOf("\n") != -1) {
                strReturn = "\n";
                paragraph = paragraph.replaceAll("\n", "");
            }
            if (paragraph.length() == 0) {
                lines.add(paragraph);
            }
            while ((paragraph.length() > 0)) {
                int size = mPaint.breakText(paragraph, true, mTextWidth, null);
                lines.add(paragraph.substring(0, size));
                paragraph = paragraph.substring(size);
                if (lines.size() >= mLineCount) {
                    break;
                }
            }
            if (paragraph.length() != 0) {
                try {
                    mNextBufferEnd -= (paragraph + strReturn).getBytes(mEncode).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (lines.size() > 0) {
            int y = mHeaderHeight;
            for (String line : lines) {
                y += mFontSize + mLineSpacing;
                canvas.drawText(line, mPaddingLeft, y, mPaint);
            }
        }
    }

    public void onDraw(Canvas canvas) {
        //draw background
        if (mBackground == null) {
            canvas.drawColor(mBackgroundColor);
        } else {
            Matrix matrix = new Matrix();
            matrix.postScale((float) mWidth / mBackground.getWidth(), (float) mHeight / mBackground.getHeight());
            Rect srcRect = new Rect(0, 0, mWidth, mHeight);
            Rect destRect = new Rect(0, 0, mWidth, mHeight);
//                matrix.setSkew(0.5f, 0);
            Bitmap bitmap = Bitmap.createBitmap(mBackground, 0, 0, mBackground.getWidth(), mBackground.getHeight(), matrix, false);
//                Bitmap bitmap = Bitmap.createBitmap(m_book_bg, 0, 0, mWidth, mHeight);
            canvas.drawBitmap(bitmap, srcRect, destRect, mPaint);
        }

        //draw title
//        canvas.drawText(mName,);
        TextPaint paint = new TextPaint();
        paint.setTextSize(mTitleFontSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        double height = Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        canvas.drawText(mName, mPaddingLeft, (float) (mHeaderHeight + height) / 2, paint);

        if (mLines.size() == 0) {
            nextPage();
        }
        if (mLines.size() > 0) {
            int y = mHeaderHeight;
            for (String line : mLines) {
                y += mFontSize + mLineSpacing;
                canvas.drawText(line, mPaddingLeft, y, mPaint);
            }
        }

//        float fPercent = (float) (m_mbBufBegin * 1.0 / m_mbBufLen);
//        String strPercent = df.format(fPercent * 100) + "%";
//        int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
//        c.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint);
    }

    public void setBackground(Bitmap bitmap) {
        mBackground = bitmap;
    }


    public interface PageSwitch {
        void onPrePage();

        void onNextPage();
    }

}
