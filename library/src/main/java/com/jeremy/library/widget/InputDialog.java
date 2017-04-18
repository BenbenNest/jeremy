package com.jeremy.library.widget;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;

/**
 * 横屏写聊天信息的Dialog
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class InputDialog extends DialogFragment {
//    private String TAG = "InputDialog";
//    private View mMenuView;
//    private Button mBtnSendMessage;
//    private ChatEditText mEditMessage;
//    private OnMessageSendListener mOnSend;
//    //切换输入法广播
//    private BroadcastReceiver mReceiver;
//    //是不是在切换输入法
//    private boolean isInputChange = false;
//    private String mPlaceholder, mCacheInput;
//    private int mCountLimit;
//    private int usableHeightPrevious;
//    private boolean mIsKeyBoardShow = false;
//
//    public interface OnMessageSendListener {
//        void sendMessage(InputDialog dialog, String message);
//    }
//
//    public InputDialog() {
//
//    }
//
//    public void setOnSend(OnMessageSendListener mOnSend) {
//        this.mOnSend = mOnSend;
//    }
//
//    public void setPlaceholder(String placeholder) {
//        this.mPlaceholder = placeholder;
//    }
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.e(TAG, "onCreate");
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
//        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        registerReceiver();
//    }
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    @Override
//    public void onStart() {
//        super.onStart();
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
//    }
//
//    /**
//     * 弹出软键盘
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void showKeyBoard() {
//        if (null != mEditMessage) {
//            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(mEditMessage, 0);
//        }
//    }
//
//    /**
//     * 隐藏软键盘
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void hideKeyBoard() {
//        if (null != getActivity()) {
//            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
//        }
//    }
//
//    /**
//     * 注册输入法切换广播
//     */
//    private void registerReceiver() {
//        mReceiver = new BroadcastReceiver() {
//            public void onReceive(Context context, Intent intent) {
//                isInputChange = true;
//                Log.e(TAG, "android.intent.action.INPUT_METHOD_CHANGED");
//            }
//        };
//        IntentFilter filter = new IntentFilter("android.intent.action.INPUT_METHOD_CHANGED");
//        getActivity().registerReceiver(mReceiver, filter);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mMenuView = inflater.inflate(R.layout.input_dialog_layout, container);
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getDialog().getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//
//        mBtnSendMessage = (Button) mMenuView.findViewById(R.id.live_chat_room_send_btn);
//        mEditMessage = (ChatEditText) mMenuView.findViewById(R.id.live_chat_room_send_edit);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
//
//        mEditMessage.requestFocus();
//        showKeyBoard();
//        mEditMessage.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showKeyBoard();
//            }
//        }, 100);
//        mBtnSendMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnSend != null) {
//                    mOnSend.sendMessage(InputDialog.this, getEditCommentText());
//                }
//            }
//        });
//
//        mBtnSendMessage.setTextColor(0xFFB0BBC0);
//        mBtnSendMessage.setEnabled(false);
//
//        //mEditComment内容变化监听
//        mEditMessage.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (mEditMessage.getText().toString().equals("")) {
//                    mBtnSendMessage.setTextColor(0xFFB0BBC0);
//                    mBtnSendMessage.setEnabled(false);
//                } else {
//                    mBtnSendMessage.setTextColor(0xFF09BCC7);
//                    mBtnSendMessage.setEnabled(true);
//                }
//            }
//        });
//
//        if (!TextUtils.isEmpty(mPlaceholder)) {
//            mEditMessage.setHint(mPlaceholder);
//        }
//
//        if (!TextUtils.isEmpty(mCacheInput)) {
//            mEditMessage.setText(mCacheInput);
//            mEditMessage.setSelection(mEditMessage.getText().length());
//        }
//
//        if (mCountLimit != 0) {
//            mEditMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mCountLimit)});
//        }
//
//        //点击阴影消失
//        mMenuView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                int top = mMenuView.findViewById(R.id.scrollview).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < top) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
//
//        mMenuView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
//        return mMenuView;
//    }
//
//    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
//            possiblyResizeChildOfContent();
//        }
//    };
//
//    private void possiblyResizeChildOfContent() {
//        int usableHeightNow = computeUsableHeight();
//        if (usableHeightNow != usableHeightPrevious) {
//            int usableHeightSansKeyboard = mMenuView.getRootView().getHeight();
//            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//            Log.e(TAG, "heightDifference=" + heightDifference);
//            if (heightDifference > (usableHeightSansKeyboard / 4)) {
//                Log.e(TAG, "键盘弹出了");
//                mIsKeyBoardShow = true;
//            } else {
//                if (mIsKeyBoardShow) {
//                    Log.e(TAG, "键盘收起了");
//                    mIsKeyBoardShow = false;
//                    mEditMessage.clearFocus();
//                    RunOnUIUtils.runOnUI(new Runnable() {
//                        @Override
//                        public void run() {
//                            dismiss();
//                            hideKeyBoard();
//                        }
//                    }, 100);
//                }
//            }
//            mMenuView.requestLayout();
//            usableHeightPrevious = usableHeightNow;
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.CUPCAKE)
//    private int computeUsableHeight() {
//        Rect r = new Rect();
//        mMenuView.getWindowVisibleDisplayFrame(r);
//        return (r.bottom - r.top);
//    }
//
//    public String getEditCommentText() {
//        return mEditMessage.getText().toString();
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void dismiss() {
//        hideKeyBoard();
//        mMenuView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
//        if (null != mReceiver && null != getActivity()) {
//            getActivity().unregisterReceiver(mReceiver);
//        }
//        super.dismiss();
//    }

}
