package com.jeremy.lychee.widget.BookReader;

import com.jeremy.lychee.base.BaseApplication;

/**
 * Created by benbennest on 16/6/20.
 */
public class SettingContent {

    private static SettingContent setting = null;
    private static int densityDpi;
    private static float density;

    private SettingContent() {
    }

    /**
     * 回收资源
     */
    public static void recycle() {
        setting = null;
    }

    public static synchronized SettingContent getInstance() {
        if (setting != null) {
            return setting;
        }

        setting = new SettingContent();
        density = BaseApplication.baseContext.getResources().getDisplayMetrics().density;
        densityDpi = BaseApplication.baseContext.getResources().getDisplayMetrics().densityDpi;



        return setting;
    }




//    /**
//     * 动画翻页模式,和动画列表
//     */
//    public static int ANIMATION_MODE_NONE = 0;
//    public static int ANIMATION_MODE_HORIZONTAL = 1;
//    public static int ANIMATION_MODE_3D = 2;
//
//    public final static int SORT_BY_FILE_NAME = 0;
//    public final static int SORT_BY_LIB_TIME = 1;
//    public final static int SORT_BY_READ_TIME = 2;
//
//    public final static int FOLDER_IN_FRONT = 0;
//    public final static int FOLDER_IN_BACK = 1;
//
//    public final static int BOOKSHELF_LIST_MODE = 0;
//    public final static int BOOKSHELF_SHELF_MODE = 1;
//
//    public static final int MODE_DAY = 0;
//    public static final int MODE_NIGHT = 1;
//
//    public static final int PAGINGMODE_LR = 1;// 左右翻页模式，LF=leftOrRight
//    public static final int PAGINGMODE_UD = 0;// 上下翻页模式，UD=upOrDown
//
//    public static final int READ_SCREEN_PORT = 0;
//    public static final int READ_SCREEN_LAND = 1;
//    public static final int READ_SCREEN_AUTO = 2;
//    private static final int[] DEFAULT_SCREEN_BRIGHTNESS = {70, BrightnessRegulatorFit.getInstance().isNightFit() ? BrightnessRegulatorFit.getInstance().getNightBrightness() : 100};
//    public static final int DEFAULT_READ_SCREEN = READ_SCREEN_PORT;
//
//    public static final int FLAG_COLOR_CHANGE_NONE = 0;
//    public static final int FLAG_COLOR_CHANGING = 1;
//    public static final int FLAG_COLOR_CHANGED = 2;
//
//    public static final float PARAGROUPH_DEFAULT_DISTANCE = 1.5f;
//
//    public static final int INDEX_CUSTOM_COLOR = -1;
//
//    public static final int BASE_FONT_SIZE = 12;//字体大小�?小�??
//    public static final int MAX_FONT_SIZE = 60;//字体大小�?大�??
//    public static final String DEFAULTBG = "default.jpg";
//    public static final int DEFAULTTEXTSIZE = 8;
//    public static final int DEFAULTHSPACING = 2;
//    public static final int DEFAULTVSPACING = 20;
//    public static final int DEFAULTCOLOR = 0xff330000;
//    public static final int DEFAULTCOLOR_DAYMODE = 0xFF4a3b30;
//    public static final int DEFAULTCOLOR_NIGHTMODE = 0xFF45515E;
//    public static final String BACKGROUND_IAMGE_PATH_DEFAULT = "";
//    public static final String BACKGROUND_IAMGE_PATH_DEFAULT_DAYMODE = "";
//    public static final String BACKGROUND_IAMGE_PATH_DEFAULT_NIGHTMODE = "";
//    public static final int DEFAULT_BACKGROUND_COLOR = 0xfffaf3d9;
//    public static final int DEFAULT_BACKGROUND_COLOR_DAYMODE = 0xfffaf3d9;
//    public static final int DEFAULT_BACKGROUND_COLOR_NIGHTMODE = 0xff0c0d11;
//    public static final int DEFAULT_PAGETURNING_MODE = 1;
//    public static final int DEFAULT_PAGETURNING_ANIMATION_MODE = 1;
//    public static final boolean DEFAULT_PAGETURNING_ALWAYS_TURN_NEXT = false;
//    public static final int DEFAULT_NOTE_COLOR = 1;
//    public static final String DEFAULT_SCHEME_NAME = "default";
//    private static final int DEFAULT_SORT = SORT_BY_READ_TIME;
//    private static final int DEFAULT_FOLDER_SITE = FOLDER_IN_FRONT;
//    private static final String CODE_SORT = "sort";
//    private static final String CODE_BOOKSHELF_DISPLAY_MODE = "bookshelf_display_mode";
//    private static final String CODE_FOLDER_SITE = "folder_site";
//    public static final String SETTING_SCHEME_PATH = "SettingScheme/";
//    public static final int DEFAULT_BACKGROUND_TYPE = SettingScheme.BACKGROUND_TYPE_COLOR;
//
//    private static final int[] DEFAULT_COLOR_DAYMODE = {0xff00002c, 0xffe0f1d4};
//    private static final int[] DEFAULT_COLOR_NIGHTMODE = {0xff007cb8, 0xff160000};
//
//    private static final PointF[] DEFAULT_POINT_DAYMODE = {new PointF(79.0f, 168.0f), new PointF(-91.0f, -170.0f)};
//    private static final PointF[] DEFAULT_POINT_NIGHTMODE = {new PointF(15.0f, 48.0f), new PointF(-154.0f, 187.0f)};
//
//    private static final float[] DEFAULT_TRANSPARENT_DAYMODE = {0.0f, 0.0f};
//    private static final float[] DEFAULT_TRANSPARENT_NIGHTMODE = {0.0f, 0.0f};
//
//    private static final boolean DEFAULT_FOLLOW_SYSTEM_BRIGHTNESS = true;
//
//    public static final int ROLL_PIXEL_MODE = 0;
//    public static final int ROLL_LINE_MODE = ROLL_PIXEL_MODE + 1;
//    public static final int ROLL_PAGE_UP_DOWN_MODE = ROLL_LINE_MODE + 1;
//    public static final int ROLL_PAGE_LEFT_RIGHT_MODE = ROLL_PAGE_UP_DOWN_MODE + 1;
//
//    public static final int DEFAULT_EYESTRAIN_TYPE = 3;
//    public static final int DEFAULT_SETTINGSPACE_TYPE = 2;
//
//    public static final int INDENT_DISABLE = 0;
//    public static final int INDENT_ONE_CHAR = 1;
//    public static final int INDENT_TWO_CHAR = 2;
//
//    public static final int BENCHMARK = 480;
//
//    public static final int MARGIN_BOOK_MODE_LEFT = 14;
//    public static final int MARGIN_BOOK_MODE_RIGHT = 14;
//    public static final int MARGIN_BOOK_MODE_TOP = 10;
//
//    public static volatile SoftReference<Drawable> read_bg = null;
//
//    private String curSkin;
//
//    public int textSize = DEFAULTTEXTSIZE;
//    public int textStyleIndex = 0;
//    public String textStyleName;
//    public int vSpacing = DEFAULTVSPACING;
//    public int hSpacing = DEFAULTHSPACING;
//    public String boldFlag;
//    public String underLineFlag;
//    public String italicFlag;
//    public int backgroundColorIndex;
//    public int textColor;
//    public int textColorIndex;
//    public int backgroundPicIndex;
//    public String backgroundPic;
//    public boolean keepOneLine;
//    public int indentMode = INDENT_TWO_CHAR;
//    public float paragraphDistance = PARAGROUPH_DEFAULT_DISTANCE;
//
//    public int marginLeft = 5;
//    public int marginRight = 5;
//    public int marginTop = 0;
//    public int marginBottom = 0;
//
//    public int marginBookModeLeft = MARGIN_BOOK_MODE_LEFT;
//    public int marginBookModeRight = MARGIN_BOOK_MODE_RIGHT;
//    public int marginBookModeTop = MARGIN_BOOK_MODE_TOP;
//    public int marginBookModeBottom = 0;
//
//    private static int[] rollSpeed = new int[4];
//    private static int[][] rollTimes = new int[4][];
//
//    public int rollStyle;
//    public int backgroundColor;
//    public boolean connect_auto;
//    public boolean isShowTips;
//    public boolean isOptimal;
//    private boolean isSavePower;
//    private int mode;
//    private int read_screen_set;
//    /* 屏幕亮度 */
//    private static final String[] TAG_BRIGHTNESS = {"screenBrightness", "screenNightBrightness"};
//    private int[] screenBrightness = {-1, -1};
//
//    private static final String TAG_FOLLOW_SYSTEM_BRIGHTNESS = "follow_system_brightness";
//    private boolean mIsFollowSystemBrightness = DEFAULT_FOLLOW_SYSTEM_BRIGHTNESS;
//
//    /**
//     * 眼保健操
//     */
//    private static final String TAG_EYESTRAIN = "eyestrain_cumulate";
//    /**
//     * 空行设置*
//     */
//    private static final String TAG_SETTINGSPACE = "setting_space";
//    /* 使用方案*/
//    private boolean isUseScheme;
//    /* 翻页效果*/
//    private int pageturningMode;
//    /**
//     * 翻页动画模式
//     */
//    private int pageTurningAnimationMode;
//    /**
//     * 全部区域（除菜单区域）翻下一�?
//     */
//    private boolean pageTurnAlwaysTurnNext;
//
//    /* 笔记默认颜色 */
//    private int defaultNoteColor;
//
//    private int sort;
//    private int folderSite;
//    private int bookshelfDisplayMode;
//
//    /* 是否白天模式 */
//    private boolean isDayMode = true;
//
//    //白天模式设置项：
//    public int textSizeDayMode = DEFAULTTEXTSIZE;
//    public int textStyleIndexDayMode = 0;
//    public String textStyleNameDayMode;
//    public int vSpacingDayMode = DEFAULTVSPACING;
//    public int hSpacingDayMode = DEFAULTHSPACING;
//    public String boldFlagDayMode;
//    public String underLineFlagDayMode;
//    public String italicFlagDayMode;
//    public int backgroundColorIndexDayMode;
//    public int textColorDayMode;
//    public int textColorIndexDayMode;
//    public int backgroundPicIndexDayMode;
//    public String backgroundPicDayMode;
//    public int backgroundColorDayMode;
//    public PointF[] pointDayMode = getDefaultPointDayMode();
//    public float[] tansPercentDayMode = getDefaultTransparentDayMode();
//
//    //夜间模式设置项：
//    public int textSizeNightMode = DEFAULTTEXTSIZE;
//    public int textStyleIndexNightMode = 1;
//    public String textStyleNameNightMode;
//    public int vSpacingNightMode = DEFAULTVSPACING;
//    public int hSpacingNightMode = DEFAULTHSPACING;
//    public String boldFlagNightMode;
//    public String underLineFlagNightMode;
//    public String italicFlagNightMode;
//    public int backgroundColorIndexNightMode;
//    public int textColorNightMode;
//    public int textColorIndexNightMode;
//    public int backgroundPicIndexNightMode;
//    public String backgroundPicNightMode;
//    public int backgroundColorNightMode;
//    public PointF[] pointNightMode = getDefaultPointNightMode();
//    public float[] tansPercentNightMode = getDefaultTransparentNightMode();
//
//    public int eyestrainType = Integer.MIN_VALUE;
//    public int settingspaceType = Integer.MIN_VALUE;
//    private boolean isNeedOpenLastRead = true;
//    private boolean signAlarmEnabled = false;
//
//    //是否已经将Assets中的配色方案导入到本地的SettingScheme文件夹内
//    private boolean _isLoadedSchemeToLocal = false;
//    private boolean isReadSettingChange = false;
//    private int pickerColorchanged;
//
//
//    public boolean IsLoadedSchemeToLocal() {
//        if (!_isLoadedSchemeToLocal) {
//            SharedPreferences sps = baseContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
//            _isLoadedSchemeToLocal = sps.getBoolean("isLoadedSchemeToLocal", false);
//        }
//
//        return _isLoadedSchemeToLocal;
//    }
//
//    public void SetLoadedSchemeToLocal(boolean isLoaded) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isLoadedSchemeToLocal", isLoaded);
//        ed.commit();
//
//        this._isLoadedSchemeToLocal = isLoaded;
//    }
//
//    //版本2.0新增配色方案是否已导入到本地
//    private boolean _isAddedSchemeToLocalForVer2_0 = false;
//
//    public boolean isAddedSchemeToLocalForVer2_0() {
//        if (!_isAddedSchemeToLocalForVer2_0) {
//            SharedPreferences sps = baseContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
//            _isAddedSchemeToLocalForVer2_0 = sps.getBoolean("isAddedSchemeToLocalForVer2_0", false);
//        }
//
//        return _isAddedSchemeToLocalForVer2_0;
//    }
//
//
//    public void setAddedSchemeToLocalForVer2_0(boolean isAdded) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isAddedSchemeToLocalForVer2_0", isAdded);
//        ed.commit();
//
//        this._isAddedSchemeToLocalForVer2_0 = isAdded;
//    }
//
//    public boolean isFollowSystemBright() {
//        return mIsFollowSystemBrightness;
//    }
//
//    public void setFollowSystemBright(boolean isFollowSystemBright) {
//        mIsFollowSystemBrightness = isFollowSystemBright;
//
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean(TAG_FOLLOW_SYSTEM_BRIGHTNESS, mIsFollowSystemBrightness);
//        ed.commit();
//    }
//
//    public int getScreenBrightness() {
//        int mode = getDayNeightMode();
//
//        if (screenBrightness[mode] == -1) {
//            SharedPreferences mPrefs = baseContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
//            screenBrightness[mode] = mPrefs.getInt(TAG_BRIGHTNESS[mode], DEFAULT_SCREEN_BRIGHTNESS[mode]);
//        }
//
//        return screenBrightness[mode];
//    }
//
//    public void setScreenBrightness(int screenBrightness) {
//        int mode = getDayNeightMode();
//
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt(TAG_BRIGHTNESS[mode], screenBrightness);
//        this.screenBrightness[mode] = screenBrightness;
//        ed.commit();
//    }
//
//    public int getEyestrainType() {
//        if (eyestrainType != Integer.MIN_VALUE) {
//            SharedPreferences mPrefs = baseContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
//            eyestrainType = checkEyestrainType(mPrefs.getInt(TAG_EYESTRAIN, DEFAULT_EYESTRAIN_TYPE));
//        }
//
//        return eyestrainType;
//    }
//
//    public void setEyestrainType(int eyestrainType) {
//        this.eyestrainType = checkEyestrainType(eyestrainType);
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt(TAG_EYESTRAIN, this.eyestrainType).commit();
//    }
//
//    private int checkEyestrainType(int eyestrainType) {
//        int cumulateCount = baseContext.getResources().getStringArray(R.array.options_cumulate_time).length;
//
//        return (eyestrainType < 0 || eyestrainType > cumulateCount) ? DEFAULT_EYESTRAIN_TYPE : eyestrainType;
//    }
//
//    public int getSettingSpaceType() {
//        if (settingspaceType != Integer.MIN_VALUE) {
//            SharedPreferences mPrefs = baseContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
//            settingspaceType = checkSettingSpaceType(mPrefs.getInt(TAG_SETTINGSPACE, DEFAULT_SETTINGSPACE_TYPE));
//        }
//
//        return settingspaceType;
//    }
//
//    public void setSettingSpaceType(int settingspaceType) {
//        this.settingspaceType = checkSettingSpaceType(settingspaceType);
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt(TAG_SETTINGSPACE, this.settingspaceType).commit();
//        readSettingChange1(true);
//    }
//
//    public int getSettingIndentMode() {
//        return indentMode;
//    }
//
//    public float getParagraphDistanceSet() {
//        return paragraphDistance;
//    }
//
//    public float getParagraphDistance() {
//        float scale = baseContext.getResources().getDisplayMetrics().scaledDensity;
//        int textSize = Float.valueOf((getTextSize() + BASE_FONT_SIZE) * scale + 0.5f).intValue();
//
//        int distance = (int) ((paragraphDistance - 1) * (textSize + getVSpacing()));
//        return distance;
////		if (paragraphDistance == 1 || distance < getVSpacing()) {
////			return 0;
////		}
////		return distance - getVSpacing();
//    }
//
//    public int getMarginLeft() {
//        return pageturningMode == PAGINGMODE_UD ? marginLeft : marginBookModeLeft;
//    }
//
//    public int getMarginRight() {
//        return pageturningMode == PAGINGMODE_UD ? marginRight : marginBookModeRight;
//    }
//
//    public int getMarginTop() {
//        return pageturningMode == PAGINGMODE_UD ? marginTop : marginBookModeTop;
//    }
//
//    public int getMarginBottom() {
//        return pageturningMode == PAGINGMODE_UD ? marginBottom : marginBookModeBottom;
//    }
//
//    private int checkSettingSpaceType(int settingspaceType) {
//        int cumulateCount = baseContext.getResources().getStringArray(R.array.options_space_setting).length;
//
//        return (settingspaceType < 0 || settingspaceType > cumulateCount) ? DEFAULT_SETTINGSPACE_TYPE : settingspaceType;
//    }
//
//    public int getRead_screen_set() {
//        // 不支持自动旋�?
//        if (read_screen_set == READ_SCREEN_AUTO) {
//            return READ_SCREEN_PORT;
//        }
//        return read_screen_set;
//    }
//
//    public boolean isUseScheme() {
//        return isUseScheme;
//    }
//
//    public void setUseScheme(boolean isUseScheme) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isUseScheme", isUseScheme);
//        ed.commit();
//        this.isUseScheme = isUseScheme;
//    }
//
//    public void setRead_screen_set(int read_screen_set) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt("read_screen_set", read_screen_set);
//        ed.commit();
//        this.read_screen_set = read_screen_set;
//    }
//
//    public int getBookshelfDisplayMode() {
//        return bookshelfDisplayMode;
//    }
//
//    public void setBookshelfDisplayMode(int mode) {
//        this.bookshelfDisplayMode = mode;
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt(CODE_BOOKSHELF_DISPLAY_MODE, this.bookshelfDisplayMode);
//        ed.commit();
//    }
//
//    public int getSort() {
//        return sort;
//    }
//
//    public void setSort(int sort) {
//        this.sort = sort;
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt(CODE_SORT, this.sort);
//        ed.commit();
//    }
//
//    /**
//     * 文件夹排在前�?
//     *
//     * @return
//     */
//    public int getFolderSite() {
//        return folderSite;
//    }
//
//    public void setFolderSite(int folderSite) {
//        this.folderSite = folderSite;
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt(CODE_FOLDER_SITE, this.folderSite);
//        ed.commit();
//    }
//
//    // private boolean showColorScheme;
//    // private int colorSchemeTextColor;
//    // private String colorSchemeBackgroundImagePath;
//    // private String colorSchemePath;
//    // private boolean showBackgroundImage;
//
//    private String backgroundImagePathDayMode;
//    private int backgroundTypeDayMode = DEFAULT_BACKGROUND_TYPE;
//
//
//    private String backgroundImagePathNightMode;
//    private int backgroundTypeNightMode = DEFAULT_BACKGROUND_TYPE;
//
//    public String getSettingSchemeName() {
//        return isDisplayingDayMode ? getDayModeSchemeName() : getNightModeSchemeName();
//    }
//
//    public void setSettingSchemeName(String name) {
//        if (isDisplayingDayMode) {
//            setDayModeSchemeName(name);
//        } else {
//            setNightModeSchemeName(name);
//        }
//    }
//
//    public int getBackgroundType() {
//        return isDisplayingDayMode ? backgroundTypeDayMode : backgroundTypeNightMode;
//    }
//
//    //白天模式
//    private String dayModeSchemeName;
//    //夜间模式
//    private String nightModeSchemeName;
//
//    public String getDayModeSchemeName() {
//        return dayModeSchemeName;
//    }
//
//    public void setDayModeSchemeName(String name) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putString("dayModeSchemeName", name);
//        ed.commit();
//        dayModeSchemeName = name;
//    }
//
//    public String getNightModeSchemeName() {
//        return nightModeSchemeName;
//    }
//
//    public void setNightModeSchemeName(String name) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putString("nightModeSchemeName", name);
//        ed.commit();
//        nightModeSchemeName = name;
//    }
//
//
//    public void setBackgroundType(int type) {
//        if (isDisplayingDayMode) {
//            setBackgroundTypeDayMode(type);
//        } else {
//            setBackgroundTypeNightMode(type);
//        }
//    }
//
//    public void setBackgroundTypeDayMode(int type) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt("backgroundTypeDayMode", type);
//        ed.commit();
//        backgroundTypeDayMode = type;
//        if (isDayMode) {
//            readSettingChange1(false);
//        }
//    }
//
//    public void setBackgroundTypeNightMode(int type) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt("backgroundTypeNightMode", type);
//        ed.commit();
//        backgroundTypeNightMode = type;
//        if (!isDayMode) {
//            readSettingChange1(false);
//        }
//    }
//
//    private int currentHomePageMode;
//    private boolean isHomePageChange;
//
//    public void setHomePageMode(int mode) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt("homepagemode", mode);
//        ed.commit();
//        currentHomePageMode = mode;
//    }
//
//    public void setUpdataTime(long millis) {
//
//    }
//
//    public int getHomePageMode() {
//        return currentHomePageMode;
//    }
//
//    public void setIsHomePageChange(boolean change) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("homepagemodechange", change);
//        ed.commit();
//        isHomePageChange = change;
//    }
//
//    public boolean getIsHomePageChange() {
//        return isHomePageChange;
//    }
//
//    public Drawable getBackground(Context context) {
//        String loadPath = isDisplayingDayMode ? getBackgroundImagePathDayMode() : getBackgroundImagePathNightMode();//backgroundImagePath;
//
//        if (read_bg == null || read_bg.get() == null || isReadSettingChange) {
//            synchronized (SettingContent.class) {
//                if (read_bg == null || read_bg.get() == null || isReadSettingChange) {
//                    InputStream in;
//                    try {
//                        if (getBackgroundType() == SettingScheme.BACKGROUND_TYPE_IMAGE && !TextUtils.isEmpty(loadPath)) {
//                            in = context.getAssets().open(loadPath);
//
//                            BitmapFactory.Options opt = new BitmapFactory.Options();
//                            opt.inPreferredConfig = Bitmap.Config.RGB_565;
//                            opt.inPurgeable = true;
//                            opt.inInputShareable = true;
//                            Bitmap bitmap = BitmapFactory.decodeStream(in, null, opt);
//                            read_bg = new SoftReference<Drawable>(new BitmapDrawable(bitmap));
//                        } else
//                            read_bg = new SoftReference<Drawable>(new ColorDrawable(getBackgroundColor()));
//                    } catch (Throwable t) {
//                        Log.e(t);
//                        return null;
//                    }
//                }
//            }
//        }
//        return read_bg.get();
//    }
//
//    public Drawable getBackgroundIgnoreExist(Context context) {
//        String loadPath = isDisplayingDayMode ? getBackgroundImagePathDayMode() : getBackgroundImagePathNightMode();//backgroundImagePath;
//        Drawable drawable = null;
//        InputStream in;
//        try {
//            if (getBackgroundType() == SettingScheme.BACKGROUND_TYPE_IMAGE) {
//                in = context.getAssets().open(loadPath);
//                drawable = Drawable.createFromStream(in, "bg.jpg");
//            } else
//                drawable = new ColorDrawable(getBackgroundColor());
//        } catch (Throwable t) {
//            Log.e(t);
//            return null;
//        }
//        return drawable;
//    }
//
//    // public String getColorSchemePath() {
//    // return colorSchemePath;
//    // }
//
//    public String getBackgroundImagePath() {
//        return isDisplayingDayMode ? backgroundImagePathDayMode : backgroundImagePathNightMode;
//    }
//
//    public String getBackgroundImagePathDayMode() {
//        return backgroundImagePathDayMode;
//    }
//
//    public String getBackgroundImagePathNightMode() {
//        return backgroundImagePathNightMode;
//    }
//
//    public void setBackgroundImagePath(String path) {
//        if (isDisplayingDayMode) {
//            setBackgroundImagePathDayMode(path);
//        } else {
//            setBackgroundImagePathNightMode(path);
//        }
//    }
//
//    public void setBackgroundImagePathDayMode(String path) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putString("backgroundImagePathDayMode", path);
//        ed.commit();
//        backgroundImagePathDayMode = path;
//        if (read_bg != null) {
//            read_bg.clear();
//        }
//        if (isDayMode) {
//            readSettingChange1(false);
//        }
//    }
//
//    public void setBackgroundImagePathNightMode(String path) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putString("backgroundImagePathNightMode", path);
//        ed.commit();
//        backgroundImagePathNightMode = path;
//        if (read_bg != null) {
//            read_bg.clear();
//        }
//        if (!isDayMode) {
//            readSettingChange1(false);
//        }
//    }
//
//    // public void setColorSchemePath(String path) {
//    // SharedPreferences.Editor ed = getEditor("setting");
//    // ed.putString("colorSchemePath", path);
//    // ed.commit();
//    // colorSchemePath = path;
//    // colorSchemeBackgroundImagePath = path + "/" + "image.jpg";
//    // if (read_bg != null) {
//    // read_bg.clear();
//    // }
//    // this.isReadSettingChange = true;
//    // }
//    public int getMode() {
//        if (!ModuleSwitch.isPowerSettingOpen()) {
//            return SavePower.SCREENLIGHT;
//        }
//        return mode;
//    }
//
//    public void setMode(int mode) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt("mode", mode);
//        ed.commit();
//        this.mode = mode;
//    }
//
//    /* 重力加�?�度感应支持 */
//    private boolean isSupportGravitation;
//    /* 重力加�?�度感应换肤选中模式索引�? */
//    private int skinSwitchModeIndex;
//
//    public boolean isAutoSplitChapter;
//    public boolean isPageTurnningAnimation;
//
//    public boolean isDownloadAnimation;
//    public boolean isDownloadSound;
//    public boolean isScreenControl;
//    public boolean isChapterNameControl;
//    public boolean isReadDetailControl;
//
//    public boolean isSupportGravitation() {
//        return isSupportGravitation;
//    }
//
//    public boolean isAutoSplitChapter() {
//        return isAutoSplitChapter;
//    }
//
//    public boolean isPageTurnningAnimation() {
//        return isPageTurnningAnimation;
//    }
//
//    /**
//     * 6.1版本暂停该功�?
//     */
//    public boolean isNeedOpenLastRead() {
//        return false;
//        //return isNeedOpenLastRead;
//    }
//
//    public boolean doesSignAlarmEnable() {
//        return signAlarmEnabled;
//    }
//
//    public boolean isDownloadAnimation() {
//        return isDownloadAnimation;
//    }
//
//    public boolean isDownloadSound() {
//        return isDownloadSound;
//    }
//
//    public boolean isScreenControl() {
//        return isScreenControl;
//    }
//
//    public boolean isChapterNameControl() {
//        if (SettingContent.getInstance().getPageturningMode() == SettingContent.PAGINGMODE_LR) {
//            return isChapterNameControl;
//        }
//        return false;
//    }
//
//    public boolean isReadDetailControl() {
//        return isReadDetailControl;
//    }
//
//    public int getSkinSwitchModeIndex() {
//        return skinSwitchModeIndex;
//    }
//
//    public void setSupportGravitation(boolean isSupport) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isSupportGravitation", isSupport);
//        ed.commit();
//        this.isSupportGravitation = isSupport;
//
//    }
//
//    public void setSkinSwitchModeIndex(int modeIndex) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt("skinSwitchModeIndex", modeIndex);
//        ed.commit();
//        this.skinSwitchModeIndex = modeIndex;
//    }
//
//    public void setAutoSplitChapter(boolean isAuto) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isAutoSplitChapter", isAuto);
//        ed.commit();
//        this.isAutoSplitChapter = isAuto;
//    }
//
//    public void setPageTurnningAnimation(boolean isAnim) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isPageTurnningAnimation", isAnim);
//        ed.commit();
//        this.isPageTurnningAnimation = isAnim;
//    }
//
//    public void setDownloadAnimation(boolean isAnim) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isDownloadAnimation", isAnim);
//        ed.commit();
//        this.isDownloadAnimation = isAnim;
//    }
//
//    public void setNeedOpenLastRead(boolean isNeed) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isNeedOpenLastRead", isNeed);
//        ed.commit();
//        this.isNeedOpenLastRead = isNeed;
//    }
//
//    public void setSignAlarmEnable(boolean enable) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("signAlarmEnable", enable);
//        ed.commit();
//        this.signAlarmEnabled = enable;
//    }
//
//    public void setDownloadSound(boolean isSound) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isDownloadSound", isSound);
//        ed.commit();
//        this.isDownloadSound = isSound;
//    }
//
//    public void setScreenControl(boolean isControl) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isScreenControl", isControl);
//        ed.commit();
//        this.isScreenControl = isControl;
//        readSettingChange1(true);
//    }
//
//    public void setChapterNameControl(boolean isChapterNameControl) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isChapterNameControl", isChapterNameControl);
//        ed.commit();
//        this.isChapterNameControl = isChapterNameControl;
//        readSettingChange1(true);
//    }
//
//    public void setReadDetailControl(boolean isReadDetailControl) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isReadDetailControl", isReadDetailControl);
//        ed.commit();
//        this.isReadDetailControl = isReadDetailControl;
//        readSettingChange1(true);
//    }
//
//    public boolean isOptimal() {
//        return isOptimal;
//    }
//
//    public void clearImageCallBack() {
//
//    }
//
//    public void setDefaultSetting() {
//        SharedPreferences.Editor ed = getEditor("font");
//
//        ed.putInt("textcolor", DEFAULTCOLOR);
//        ed.putInt("backgroundcolor", -134180);
//        ed.putInt("background", 6);
//        ed.putInt("color", 0);
//        ed.putInt("size", DEFAULTTEXTSIZE);
//        ed.putInt("hspacing", DEFAULTHSPACING);
//        ed.putInt("vspace", DEFAULTVSPACING);
//        ed.putString("bold", null);
//        ed.putString("udline", null);
//        ed.putString("italic", null);
//        ed.putInt("styleIndex", 1);
//        ed.putString("fontStyle", baseContext.getString(R.string.default_font_name));// 字体属�?�名�?
//
//        ed.putBoolean("isUseScheme", true);
//
//        ed.putInt("textcolorDayMode", DEFAULTCOLOR_DAYMODE);
//        ed.putInt("backgroundcolorDayMode", -134180);
//
//        ed.putFloat("pointXDayMode", getDefaultPointDayMode()[PICKER_COLOR].x);
//        ed.putFloat("pointYDayMode", getDefaultPointDayMode()[PICKER_COLOR].y);
//        ed.putFloat("secendPointXDayMode", getDefaultPointDayMode()[PICKER_SECEND_COLOR].x);
//        ed.putFloat("secendPointYDayMode", getDefaultPointDayMode()[PICKER_SECEND_COLOR].y);
//        ed.putFloat("tansPercentDayMode", DEFAULT_TRANSPARENT_DAYMODE[PICKER_COLOR]);
//        ed.putFloat("secendTansPercentDayMode", DEFAULT_TRANSPARENT_DAYMODE[PICKER_SECEND_COLOR]);
//
//        ed.putInt("backgroundDayMode", 6);
//        ed.putInt("colorDayMode", 0);
//        ed.putInt("sizeDayMode", DEFAULTTEXTSIZE);
//        ed.putInt("hspacingDayMode", DEFAULTHSPACING);
//        ed.putInt("vspaceDayMode", DEFAULTVSPACING);
//        ed.putString("boldDayMode", null);
//        ed.putString("udlineDayMode", null);
//        ed.putString("italicDayMode", null);
//        ed.putInt("styleIndexDayMode", 1);
//        ed.putString("fontStyleDayMode", baseContext.getString(R.string.default_font_name));// 字体属�?�名�?
//
//        ed.putBoolean("isUseSchemeDayMode", true);
//
//        ed.putInt("textcolorNightMode", DEFAULTCOLOR_NIGHTMODE);
//        ed.putInt("backgroundcolorNightMode", -134180);
//        ed.putFloat("pointXNightMode", getDefaultPointNightMode()[PICKER_COLOR].x);
//        ed.putFloat("pointYNightMode", getDefaultPointNightMode()[PICKER_COLOR].y);
//        ed.putFloat("secendPointXNightMode", getDefaultPointNightMode()[PICKER_SECEND_COLOR].x);
//        ed.putFloat("secendPointYNightMode", getDefaultPointNightMode()[PICKER_SECEND_COLOR].y);
//        ed.putFloat("tansPercentNightMode", DEFAULT_TRANSPARENT_NIGHTMODE[PICKER_COLOR]);
//        ed.putFloat("secendTansPercentNightMode", DEFAULT_TRANSPARENT_NIGHTMODE[PICKER_SECEND_COLOR]);
//        ed.putInt("backgroundNightMode", 6);
//        ed.putInt("colorNightMode", 0);
//        ed.putInt("sizeNightMode", DEFAULTTEXTSIZE);
//        ed.putInt("hspacingNightMode", DEFAULTHSPACING);
//        ed.putInt("vspaceNightMode", DEFAULTVSPACING);
//        ed.putString("boldNightMode", null);
//        ed.putString("udlineNightMode", null);
//        ed.putString("italicNightMode", null);
//        ed.putInt("styleIndexNightMode", 1);
//        ed.putString("fontStyleNightMode", baseContext.getString(R.string.default_font_name));// 字体属�?�名�?
//
//        ed.putBoolean("isUseSchemeNightMode", true);
//        ed.putInt("read_screen_set", read_screen_set);
//
//        ed.commit();
//
//        setBackgroundTypeDayMode(DEFAULT_BACKGROUND_TYPE);
//        setBackgroundTypeNightMode(DEFAULT_BACKGROUND_TYPE);
//        //setBackgroundImagePath(BACKGROUND_IAMGE_PATH_DEFAULT);
//        setBackgroundImagePathDayMode(BACKGROUND_IAMGE_PATH_DEFAULT_DAYMODE);
//        setBackgroundImagePathNightMode(BACKGROUND_IAMGE_PATH_DEFAULT_NIGHTMODE);
//
//        this.isDisplayingDayMode = true;
//        this.textColorIndexDayMode = 0;
//        this.textColorIndexNightMode = 2;
//
//        this.textColor = DEFAULTCOLOR;
//        this.textColorIndex = 0;
//        this.backgroundColor = -134180;
//        this.backgroundColorIndex = 6;
//        this.textSize = DEFAULTTEXTSIZE;
//        this.hSpacing = DEFAULTHSPACING;
//        this.vSpacing = DEFAULTVSPACING;
//        this.boldFlag = null;
//        this.underLineFlag = null;
//        this.italicFlag = null;
//        this.textStyleName = baseContext.getString(R.string.default_font_name);
//
//
//        this.textColorDayMode = DEFAULTCOLOR_DAYMODE;
//        this.textColorIndexDayMode = 0;
//        this.backgroundColorDayMode = -134180;
//        this.pointDayMode = getDefaultPointDayMode();
//        this.tansPercentDayMode = getDefaultTransparentDayMode();
//        this.backgroundColorIndexDayMode = 6;
//        this.textSizeDayMode = DEFAULTTEXTSIZE;
//        this.hSpacingDayMode = DEFAULTHSPACING;
//        this.vSpacingDayMode = DEFAULTVSPACING;
//        this.boldFlagDayMode = null;
//        this.underLineFlagDayMode = null;
//        this.italicFlagDayMode = null;
//        this.textStyleNameDayMode = baseContext.getString(R.string.default_font_name);
//
//
//        this.textColorNightMode = DEFAULTCOLOR_NIGHTMODE;
//        this.textColorIndex = 0;
//        this.backgroundColor = -134180;
//        this.backgroundColorIndex = 6;
//        this.textSizeNightMode = DEFAULTTEXTSIZE;
//        this.hSpacingNightMode = DEFAULTHSPACING;
//        this.vSpacingNightMode = DEFAULTVSPACING;
//        this.boldFlagNightMode = null;
//        this.underLineFlagNightMode = null;
//        this.italicFlagNightMode = null;
//        this.textStyleNameNightMode = baseContext.getString(R.string.default_font_name);
//        this.pointNightMode = getDefaultPointNightMode();
//        this.tansPercentNightMode = getDefaultTransparentNightMode();
//
//        this.read_screen_set = DEFAULT_READ_SCREEN;
//
//        SharedPreferences theme = baseContext.getSharedPreferences("theme", Context.MODE_PRIVATE);
//        SharedPreferences.Editor themeEd = theme.edit();
//        themeEd.putString("theme", DEFAULT_SKIN);
//        themeEd.commit();
//        setting.curSkin = theme.getString("theme", DEFAULT_SKIN);
//
//        SharedPreferences mPrefs = baseContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
//        SharedPreferences.Editor settingEd = mPrefs.edit();
//
//        String defaultSchemeName = baseContext.getString(R.string.otherSetting_label_defaultScheme);
//        String defaultSchemeNameDayMode = baseContext.getString(R.string.scheme_daymode);
//        String defaultSchemeNameNightMode = baseContext.getString(R.string.scheme_night_1);
//
//        int system_bright = Settings.System.getInt(baseContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, DEFAULT_SCREEN_BRIGHTNESS[MODE_DAY]);
//        this.screenBrightness[MODE_DAY] = BrightnessRegulatorFit.getInstance().isDayFit() && system_bright < 25 ? DEFAULT_SCREEN_BRIGHTNESS[MODE_DAY] : system_bright;
//        this.screenBrightness[MODE_NIGHT] = DEFAULT_SCREEN_BRIGHTNESS[MODE_NIGHT];
//        settingEd.putInt(TAG_BRIGHTNESS[MODE_DAY], this.screenBrightness[MODE_DAY]);
//        settingEd.putInt(TAG_BRIGHTNESS[MODE_NIGHT], this.screenBrightness[MODE_NIGHT]);
//
//        this.mIsFollowSystemBrightness = DEFAULT_FOLLOW_SYSTEM_BRIGHTNESS;
//        settingEd.putBoolean(TAG_FOLLOW_SYSTEM_BRIGHTNESS, DEFAULT_FOLLOW_SYSTEM_BRIGHTNESS);
//
//        settingEd.putBoolean("isSupportGravitation", true);
//        settingEd.putInt("skinSwitchModeIndex", 0);
//        settingEd.putBoolean("showColorScheme", true);
//        //settingEd.putString("colorSchemePath", "TextBackImage/1");
//        //settingEd.putInt("colorSchemeTextColor", Color.rgb(36, 36, 36));
//        settingEd.putBoolean("connect_auto", true);
//        settingEd.putBoolean("showTips", true);
//        settingEd.putBoolean("save_power", false);
//        settingEd.putBoolean("isAutoSplitChapter", true);
//        settingEd.putBoolean("isPageTurnningAnimation", true);
//        settingEd.putBoolean("isDownloadAnimation", false);
//        settingEd.putBoolean("isDownloadSound", true);
//        settingEd.putBoolean("isNeedOpenLastRead", true);
//
//        settingEd.putBoolean("isScreenControl", true);
//        settingEd.putBoolean("isChapterNameControl", true);
//        settingEd.putBoolean("isReadDetailControl", true);
//        settingEd.putInt("pageturningMode", DEFAULT_PAGETURNING_MODE);
//        settingEd.putInt("pageTurningAnimationMode", DEFAULT_PAGETURNING_ANIMATION_MODE);
//        settingEd.putBoolean("pageTurnAlwaysTurnNext", DEFAULT_PAGETURNING_ALWAYS_TURN_NEXT);
//        settingEd.putInt("homepagemode", HomeSwitch.DEFAULT_HOMEPAGE_MODE);
//        settingEd.putBoolean("homepagemodechange", false);
//        settingEd.putInt(TAG_EYESTRAIN, DEFAULT_EYESTRAIN_TYPE);
//        settingEd.putInt(TAG_SETTINGSPACE, DEFAULT_SETTINGSPACE_TYPE);
//        settingEd.putString("settingSchemeName", defaultSchemeName);
//        settingEd.putInt("defaultNoteColor", DEFAULT_NOTE_COLOR);
//        settingEd.putString("dayModeSchemeName", defaultSchemeNameDayMode);
//        settingEd.putString("nightModeSchemeName", defaultSchemeNameNightMode);
//        ed.putBoolean("isDayMode", true);
//        setting.isDayMode = true;
//        setting.dayModeSchemeName = defaultSchemeNameDayMode;
//        setting.nightModeSchemeName = defaultSchemeNameNightMode;
//        setting.isScreenControl = true;
//        setting.isChapterNameControl = true;
//        setting.isReadDetailControl = true;
//
//        setting.sort = DEFAULT_SORT;
////		setting.bookshelfDisplayMode = BOOKSHELF_SHELF_MODE;
//        setting.folderSite = DEFAULT_FOLDER_SITE;
//        settingEd.putInt(CODE_SORT, setting.sort);
////		settingEd.putInt(CODE_BOOKSHELF_DISPLAY_MODE, setting.bookshelfDisplayMode);
//        settingEd.putInt(CODE_FOLDER_SITE, setting.folderSite);
//
//        settingEd.commit();
//        setOptimal(false);
//        setMode(0);
//
//        if (read_bg != null) {
//            read_bg.clear();
//        }
//
//        setting.eyestrainType = DEFAULT_EYESTRAIN_TYPE;
//
//        setting.isSupportGravitation = true;
//        setting.skinSwitchModeIndex = 0;
//        // setting.colorSchemePath = "textbackimage/1";
//        // setting.colorSchemeBackgroundImagePath = "textbackimage/1/" +
//        // "image.jpg";
//        // setting.colorSchemeTextColor = Color.rgb(36, 36, 36);
//        // setting.showColorScheme = true;
//        setting.textStyleIndex = 1;
//        setting.textStyleName = baseContext.getString(R.string.default_font_name);
//        setting.connect_auto = true;
//        setting.isShowTips = true;
//        setting.isSavePower = false;
//        setting.isAutoSplitChapter = true;
//        setting.isPageTurnningAnimation = true;
//        setting.isDownloadAnimation = false;
//        setting.isDownloadSound = false;
//        setting.isNeedOpenLastRead = true;
//        setting.pageturningMode = DEFAULT_PAGETURNING_MODE;
//        setting.pageTurningAnimationMode = DEFAULT_PAGETURNING_ANIMATION_MODE;
//        setting.pageTurnAlwaysTurnNext = DEFAULT_PAGETURNING_ALWAYS_TURN_NEXT;
//        if (setting.currentHomePageMode != HomeSwitch.DEFAULT_HOMEPAGE_MODE) {
//            setting.currentHomePageMode = HomeSwitch.DEFAULT_HOMEPAGE_MODE;
//            setting.isHomePageChange = true;
//        }
//        setting.defaultNoteColor = DEFAULT_NOTE_COLOR;
//        PreferenceUtils.setNetworkEnable(baseContext, PreferenceUtils.NETWORK_ENABLE);
//        PreferenceUtils.setEnableNetworkListening(baseContext, false);
//        PreferenceUtils.setShowNetworkStatus(baseContext, false);
//
//        SharedPreferences.Editor rollStyle = getEditor("rollstyle");
//        rollStyle.putInt("num", 1);
//        rollStyle.commit();
//        this.rollStyle = 0;
//
//        setRollSpeed(ROLL_PIXEL_MODE, 50, true);
//        setRollSpeed(ROLL_LINE_MODE, 50, true);
//        setRollSpeed(ROLL_PAGE_UP_DOWN_MODE, 50, true);
//        setRollSpeed(ROLL_PAGE_LEFT_RIGHT_MODE, 50, true);
//
//        SharedPreferences.Editor action = getEditor("action");
//        action.putBoolean("isKeepOneLine", false);
//        action.commit();
//        this.keepOneLine = false;
//
//        SharedPreferences.Editor indentMode = getEditor("indentMode");
//        indentMode.putInt("mode", INDENT_TWO_CHAR);
//        indentMode.commit();
//        this.indentMode = INDENT_TWO_CHAR;
//
//        SharedPreferences.Editor paragraphDistance = getEditor("paragraphDistance");
//        paragraphDistance.putFloat("distance", PARAGROUPH_DEFAULT_DISTANCE);
//        paragraphDistance.commit();
//        this.paragraphDistance = PARAGROUPH_DEFAULT_DISTANCE;
//
//        SharedPreferences.Editor marginEditor = getEditor("margin");
//        marginEditor.putInt("marginLeft", 5);
//        marginEditor.putInt("marginRight", 5);
//        marginEditor.putInt("marginTop", 0);
//        marginEditor.putInt("marginBottom", 0);
//
//        marginEditor.putInt("marginBookModeLeft", MARGIN_BOOK_MODE_LEFT);
//        marginEditor.putInt("marginBookModeRight", MARGIN_BOOK_MODE_RIGHT);
//        marginEditor.putInt("marginBookModeTop", MARGIN_BOOK_MODE_TOP);
//        marginEditor.putInt("marginBookModeBottom", 0);
//        marginEditor.commit();
//        this.marginLeft = 5;
//        this.marginRight = 5;
//        this.marginTop = 0;
//        this.marginBottom = 0;
//
//        this.marginBookModeBottom = 0;
//        this.marginBookModeTop = MARGIN_BOOK_MODE_TOP;
//        this.marginBookModeLeft = MARGIN_BOOK_MODE_LEFT;
//        this.marginBookModeRight = MARGIN_BOOK_MODE_RIGHT;
//
//        this.settingspaceType = DEFAULT_SETTINGSPACE_TYPE;
//
//        readSettingChange1(true);
//        updateMargin();
////		SetLoadedSchemeToLocal(false);
//
//        //删除原有的配色方�?
//        String[] basePath = {StorageUtils.getExternalLibPath() + "/",
//                StorageUtils.getMemoryLibPath() + "/"};
//        for (int i = 0; i < basePath.length; i++) {
//            File file = new File(basePath[i] + SETTING_SCHEME_PATH);
//            if (file.exists()) {
//                FileUtil.deleteFile(file);
//            }
//        }
//
//        //载入配色方案到本�?
//        //loadSchemeToLocal();
//        BrightPopupMenu.deleteSchemeFile(true);
//        BrightPopupMenu.isSetDefalut = true;
//        resetTextDraw();
//    }
//
//    private void resetTextDraw() {
//        TextDraw.STATEBAR_HEIGHT = StateBannerHelper.getStateBannerHeight();
//        TextDraw.PADDING_TOP = Utils.dipDimensionInteger(SettingContent.getInstance().getMarginTop());// 页面顶部间距
//        TextDraw.PADDING_LEFT = Utils.dipDimensionInteger(SettingContent.getInstance().getMarginLeft());// 页面左侧间距
//        TextDraw.PADDING_RIGHT = Utils.dipDimensionInteger(SettingContent.getInstance().getMarginRight());// 页面右侧间距
//        TextDraw.PADDING_BOTTOM = Utils.dipDimensionInteger(SettingContent.getInstance().getMarginBottom());// 页面底部间距
//    }
//
//    private void loadSchemeToLocal() {
//        //拷贝Assets中的配色方案配置到本�?
//
//        try {
//            FileUtil.loadAssetsSchemesToLocal(SettingSchemeLoader.loadSettingScheme());
//            setting.SetLoadedSchemeToLocal(true);
//        } catch (Exception e) {
//            Log.e(e);
//        }
//    }
//
//    public void setOptimal(boolean optimal) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putBoolean("isOptimal", optimal);
//        ed.commit();
//        this.isOptimal = optimal;
//        if (isOptimal) {
//            ed.putInt("textcolor", DEFAULTCOLOR);
//            ed.putInt("color", 0);
//            ed.putInt("size", DEFAULTTEXTSIZE);
//            ed.putInt("hspacing", DEFAULTHSPACING);
//            ed.putInt("vspace", DEFAULTVSPACING);
//            ed.putString("bold", null);
//            ed.putString("udline", null);
//            ed.putString("italic", null);
//            ed.putString("backgroundpic", DEFAULTBG);
//            if (read_bg != null) {
//                read_bg.clear();
//            }
//            ed.putInt("backgroundpic_index", -1);
//            ed.commit();
//            this.textColor = DEFAULTCOLOR;
//            this.textColorIndex = 0;
//            this.textSize = DEFAULTTEXTSIZE;
//            this.hSpacing = DEFAULTHSPACING;
//            this.vSpacing = DEFAULTVSPACING;
//            this.boldFlag = null;
//            this.underLineFlag = null;
//            this.italicFlag = null;
//            this.backgroundColorIndex = -1;
//            this.backgroundPicIndex = -1;
//            this.backgroundPic = DEFAULTBG;
//
//            ed.putInt("textcolorDayMode", DEFAULTCOLOR_DAYMODE);
//            ed.putInt("colorDayMode", 0);
//            ed.putInt("sizeDayMode", DEFAULTTEXTSIZE);
//            ed.putInt("hspacingDayMode", DEFAULTHSPACING);
//            ed.putInt("vspaceDayMode", DEFAULTVSPACING);
//            ed.putString("boldDayMode", null);
//            ed.putString("udlineDayMode", null);
//            ed.putString("italicDayMode", null);
//            ed.putString("backgroundpicDayMode", DEFAULTBG);
//            if (read_bg != null) {
//                read_bg.clear();
//            }
//            ed.putInt("backgroundpicDayMode_index", -1);
//            ed.commit();
//            this.textColorDayMode = DEFAULTCOLOR_DAYMODE;
//            this.textColorIndexDayMode = 0;
//            this.textSizeDayMode = DEFAULTTEXTSIZE;
//            this.hSpacingDayMode = DEFAULTHSPACING;
//            this.vSpacingDayMode = DEFAULTVSPACING;
//            this.boldFlagDayMode = null;
//            this.underLineFlagDayMode = null;
//            this.italicFlagDayMode = null;
//            this.backgroundColorIndexDayMode = -1;
//            this.backgroundPicIndexDayMode = -1;
//            this.backgroundPicDayMode = DEFAULTBG;
//
//            ed.putInt("textcolorNightMode", DEFAULTCOLOR_NIGHTMODE);
//            ed.putInt("colorNightMode", 2);
//            ed.putInt("sizeNightMode", DEFAULTTEXTSIZE);
//            ed.putInt("hspacingNightMode", DEFAULTHSPACING);
//            ed.putInt("vspaceNightMode", DEFAULTVSPACING);
//            ed.putString("boldNightMode", null);
//            ed.putString("udlineNightMode", null);
//            ed.putString("italicNightMode", null);
//            ed.putString("backgroundpicNightMode", DEFAULTBG);
//            if (read_bg != null) {
//                read_bg.clear();
//            }
//            ed.putInt("backgroundpicNightMode_index", -1);
//            ed.commit();
//            this.textColorNightMode = DEFAULTCOLOR_NIGHTMODE;
//            this.textColorIndexNightMode = 2;
//            this.textSizeNightMode = DEFAULTTEXTSIZE;
//            this.hSpacingNightMode = DEFAULTHSPACING;
//            this.vSpacingNightMode = DEFAULTVSPACING;
//            this.boldFlagNightMode = null;
//            this.underLineFlagNightMode = null;
//            this.italicFlagNightMode = null;
//            this.backgroundColorIndexNightMode = -1;
//            this.backgroundPicIndexNightMode = -1;
//            this.backgroundPicNightMode = DEFAULTBG;
//        }
//    }
//
//    private SettingContent() {
//    }
//
//    /**
//     * 回收资源
//     */
//    public static void recycle() {
//        setting = null;
//    }
//
//    public static synchronized SettingContent getInstance() {
//        if (setting != null) {
//            return setting;
//        }
//
//        // 滚屏模式速度边界设定
//        SettingContent.rollTimes[ROLL_PIXEL_MODE] = new int[]{1, 100};
//        SettingContent.rollTimes[ROLL_LINE_MODE] = new int[]{250, 3000};
//        SettingContent.rollTimes[ROLL_PAGE_UP_DOWN_MODE] = new int[]{4000, 40000};
//        SettingContent.rollTimes[ROLL_PAGE_LEFT_RIGHT_MODE] = new int[]{4000, 40000};
//
//        setting = new SettingContent();
////		setting.setSupportGravitation(false);
//        density = baseContext.getResources().getDisplayMetrics().density;
//        densityDpi = baseContext.getResources().getDisplayMetrics().densityDpi;
//
//        // 读取当前皮肤主题，默认为蓝色
//        SharedPreferences mPrefs = baseContext.getSharedPreferences("theme", Context.MODE_PRIVATE);
//        setting.curSkin = ThemeConfig.getInstance().reviseTheme(mPrefs.getString("theme", DEFAULT_SKIN));
//
//        // 读取字体相关设置
//        mPrefs = baseContext.getSharedPreferences("font", Context.MODE_PRIVATE);
//
//        setting.textSize = mPrefs.getInt("size", DEFAULTTEXTSIZE); // 字体大小
//        setting.textStyleIndex = mPrefs.getInt("styleIndex", 1);// 字体属�?�所在索�?
//        setting.textStyleName = mPrefs.getString("fontStyle", baseContext.getString(R.string.default_font_name));// 字体属�?�名�?
//        setting.vSpacing = mPrefs.getInt("vspace", DEFAULTVSPACING);// 行间�?
//        setting.hSpacing = mPrefs.getInt("hspacing", DEFAULTHSPACING);// 字间�?
//        setting.boldFlag = mPrefs.getString("bold", null);// 是否粗体
//        setting.underLineFlag = mPrefs.getString("udline", null);// 是否下划�?
//        setting.italicFlag = mPrefs.getString("italic", null);// 是否斜体
//        setting.backgroundColorIndex = mPrefs.getInt("background", -1);// 背景色所在索引�??
//        setting.textColorIndex = mPrefs.getInt("color", -1);// 文本色所在索引�??
//        setting.backgroundColor = mPrefs.getInt("backgroundcolor", DEFAULT_BACKGROUND_COLOR);// 背景�?
//        setting.textColor = mPrefs.getInt("textcolor", DEFAULTCOLOR);// 文本�?
//        setting.backgroundPicIndex = mPrefs.getInt("backgroundpic_index", 0);// 背景�?用图片的索引�?
//        setting.backgroundPic = mPrefs.getString("backgroundpic", DEFAULTBG);// 背景�?用图片名�?
//
//        if (setting.isDayMode) {
//            //////////////////////白天模式////////////////////////////
//            setting.textSizeDayMode = mPrefs.getInt("sizeDayMode", DEFAULTTEXTSIZE); // 字体大小
//            setting.textStyleIndexDayMode = mPrefs.getInt("styleIndexDayMode", 1);// 字体属�?�所在索�?
//            setting.textStyleNameDayMode = mPrefs.getString("fontStyleDayMode", baseContext.getString(R.string.default_font_name));// 字体属�?�名�?
//            setting.vSpacingDayMode = mPrefs.getInt("vspaceDayMode", DEFAULTVSPACING);// 行间�?
//            setting.hSpacingDayMode = mPrefs.getInt("hspacingDayMode", DEFAULTHSPACING);// 字间�?
//            setting.boldFlagDayMode = mPrefs.getString("boldDayMode", null);// 是否粗体
//            setting.underLineFlagDayMode = mPrefs.getString("udlineDayMode", null);// 是否下划�?
//            setting.italicFlagDayMode = mPrefs.getString("italicDayMode", null);// 是否斜体
//
//            //////////////////////夜间模式////////////////////////////
//            setting.textSizeNightMode = mPrefs.getInt("sizeDayMode", DEFAULTTEXTSIZE); // 字体大小
//            setting.textStyleIndexNightMode = mPrefs.getInt("styleIndexDayMode", 1);// 字体属�?�所在索�?
//            setting.textStyleNameNightMode = mPrefs.getString("fontStyleDayMode", baseContext.getString(R.string.default_font_name));// 字体属�?�名�?
//            setting.vSpacingNightMode = mPrefs.getInt("vspaceDayMode", DEFAULTVSPACING);// 行间�?
//            setting.hSpacingNightMode = mPrefs.getInt("hspacingDayMode", DEFAULTHSPACING);// 字间�?
//            setting.boldFlagNightMode = mPrefs.getString("boldDayMode", null);// 是否粗体
//            setting.underLineFlagNightMode = mPrefs.getString("udlineDayMode", null);// 是否下划�?
//            setting.italicFlagNightMode = mPrefs.getString("italicDayMode", null);// 是否斜体
//        } else {
//            //////////////////////白天模式////////////////////////////
//            setting.textSizeDayMode = mPrefs.getInt("sizeNightMode", DEFAULTTEXTSIZE); // 字体大小
//            setting.textStyleIndexDayMode = mPrefs.getInt("styleIndexNightMode", 1);// 字体属�?�所在索�?
//            setting.textStyleNameDayMode = mPrefs.getString("fontStyleNightMode", baseContext.getString(R.string.default_font_name));// 字体属�?�名�?
//            setting.vSpacingDayMode = mPrefs.getInt("vspaceNightMode", DEFAULTVSPACING);// 行间�?
//            setting.hSpacingDayMode = mPrefs.getInt("hspacingNightMode", DEFAULTHSPACING);// 字间�?
//            setting.boldFlagDayMode = mPrefs.getString("boldNightMode", null);// 是否粗体
//            setting.underLineFlagDayMode = mPrefs.getString("udlineNightMode", null);// 是否下划�?
//            setting.italicFlagDayMode = mPrefs.getString("italicNightMode", null);// 是否斜体
//
//            //////////////////////夜间模式////////////////////////////
//            setting.textSizeNightMode = mPrefs.getInt("sizeNightMode", DEFAULTTEXTSIZE); // 字体大小
//            setting.textStyleIndexNightMode = mPrefs.getInt("styleIndexNightMode", 1);// 字体属�?�所在索�?
//            setting.textStyleNameNightMode = mPrefs.getString("fontStyleNightMode", baseContext.getString(R.string.default_font_name));// 字体属�?�名�?
//            setting.vSpacingNightMode = mPrefs.getInt("vspaceNightMode", DEFAULTVSPACING);// 行间�?
//            setting.hSpacingNightMode = mPrefs.getInt("hspacingNightMode", DEFAULTHSPACING);// 字间�?
//            setting.boldFlagNightMode = mPrefs.getString("boldNightMode", null);// 是否粗体
//            setting.underLineFlagNightMode = mPrefs.getString("udlineNightMode", null);// 是否下划�?
//            setting.italicFlagNightMode = mPrefs.getString("italicNightMode", null);// 是否斜体
//        }
//
//        //////////////////////白天模式////////////////////////////
//        setting.backgroundColorIndexDayMode = mPrefs.getInt("backgroundDayMode", -1);// 背景色所在索引�??
//        setting.textColorIndexDayMode = mPrefs.getInt("colorDayMode", 0);// 文本色所在索引�??
//        setting.backgroundColorDayMode = mPrefs.getInt("backgroundcolorDayMode", DEFAULT_BACKGROUND_COLOR_DAYMODE);// 背景�?
//        setting.textColorDayMode = mPrefs.getInt("textcolorDayMode", DEFAULTCOLOR_DAYMODE);// 文本�?
//        setting.backgroundPicIndexDayMode = mPrefs.getInt("backgroundpicDayMode_index", 0);// 背景�?用图片的索引�?
//        setting.backgroundPicDayMode = mPrefs.getString("backgroundpicDayMode", DEFAULTBG);// 背景�?用图片名�?
//        setting.pointDayMode[PICKER_COLOR].x = mPrefs.getFloat("pointXDayMode", getDefaultPointDayMode()[PICKER_COLOR].x);
//        setting.pointDayMode[PICKER_COLOR].y = mPrefs.getFloat("pointYDayMode", getDefaultPointDayMode()[PICKER_COLOR].y);
//        setting.tansPercentDayMode[PICKER_COLOR] = mPrefs.getFloat("tansPercentDayMode", DEFAULT_TRANSPARENT_DAYMODE[PICKER_COLOR]);
//        setting.pointDayMode[PICKER_SECEND_COLOR].x = mPrefs.getFloat("secendPointXDayMode", getDefaultPointDayMode()[PICKER_SECEND_COLOR].x);
//        setting.pointDayMode[PICKER_SECEND_COLOR].y = mPrefs.getFloat("secendPointYDayMode", getDefaultPointDayMode()[PICKER_SECEND_COLOR].y);
//        setting.tansPercentDayMode[PICKER_SECEND_COLOR] = mPrefs.getFloat("secendTansPercentDayMode", DEFAULT_TRANSPARENT_DAYMODE[PICKER_SECEND_COLOR]);
//
//        //////////////////////夜间模式////////////////////////////
//        setting.backgroundColorIndexNightMode = mPrefs.getInt("backgroundNightMode", -1);// 背景色所在索引�??
//        setting.textColorIndexNightMode = mPrefs.getInt("colorNightMode", 0);// 文本色所在索引�??
//        setting.backgroundColorNightMode = mPrefs.getInt("backgroundcolorNightMode", DEFAULT_BACKGROUND_COLOR_NIGHTMODE);// 背景�?
//        setting.textColorNightMode = mPrefs.getInt("textcolorNightMode", DEFAULTCOLOR_NIGHTMODE);// 文本�?
//        setting.backgroundPicIndexNightMode = mPrefs.getInt("backgroundpicNightMode_index", 0);// 背景�?用图片的索引�?
//        setting.backgroundPicNightMode = mPrefs.getString("backgroundpicNightMode", DEFAULTBG);// 背景�?用图片名�?
//
//        setting.pointNightMode[PICKER_COLOR].x = mPrefs.getFloat("pointXNightMode", getDefaultPointNightMode()[PICKER_COLOR].x);
//        setting.pointNightMode[PICKER_COLOR].y = mPrefs.getFloat("pointYNightMode", getDefaultPointNightMode()[PICKER_COLOR].y);
//        setting.tansPercentNightMode[PICKER_COLOR] = mPrefs.getFloat("tansPercentNightMode", DEFAULT_TRANSPARENT_NIGHTMODE[PICKER_COLOR]);
//        setting.pointNightMode[PICKER_SECEND_COLOR].x = mPrefs.getFloat("secendPointXNightMode", getDefaultPointNightMode()[PICKER_SECEND_COLOR].x);
//        setting.pointNightMode[PICKER_SECEND_COLOR].y = mPrefs.getFloat("secendPointYNightMode", getDefaultPointNightMode()[PICKER_SECEND_COLOR].y);
//        setting.tansPercentNightMode[PICKER_SECEND_COLOR] = mPrefs.getFloat("secendTansPercentNightMode", DEFAULT_TRANSPARENT_NIGHTMODE[PICKER_SECEND_COLOR]);
//
//        setting.isOptimal = mPrefs.getBoolean("isOptimal", false);
//
//        // 段首缩进
//        mPrefs = baseContext.getSharedPreferences("indentMode", Context.MODE_PRIVATE);
//        setting.indentMode = mPrefs.getInt("mode", INDENT_TWO_CHAR);
//        // 段间�?
//        mPrefs = baseContext.getSharedPreferences("paragraphDistance", Context.MODE_PRIVATE);
//        setting.paragraphDistance = mPrefs.getFloat("distance", PARAGROUPH_DEFAULT_DISTANCE);
//        // 读取是否翻页时保持一�?
//        mPrefs = baseContext.getSharedPreferences("action", Context.MODE_PRIVATE);
//        setting.keepOneLine = mPrefs.getBoolean("isKeepOneLine", false);
//        // 页边�?
//        mPrefs = baseContext.getSharedPreferences("margin", Context.MODE_PRIVATE);
//        setting.marginLeft = mPrefs.getInt("marginLeft", 5);
//        setting.marginRight = mPrefs.getInt("marginRight", 5);
//        setting.marginTop = mPrefs.getInt("marginTop", 0);
//        setting.marginBottom = mPrefs.getInt("marginBottom", 0);
//
//        setting.marginBookModeLeft = mPrefs.getInt("marginBookModeLeft", MARGIN_BOOK_MODE_LEFT);
//        setting.marginBookModeRight = mPrefs.getInt("marginBookModeRight", MARGIN_BOOK_MODE_RIGHT);
//        setting.marginBookModeTop = mPrefs.getInt("marginBookModeTop", MARGIN_BOOK_MODE_TOP);
//        setting.marginBookModeBottom = mPrefs.getInt("marginBookModeBottom", 0);
//
//        // 读取滚屏方式设定
//        mPrefs = baseContext.getSharedPreferences("rollstyle", Context.MODE_PRIVATE);
//        setting.rollStyle = mPrefs.getInt("num", 1);
//
//        mPrefs = baseContext.getSharedPreferences("rollSpeedPixelMode", Context.MODE_PRIVATE);
//        SettingContent.rollSpeed[ROLL_PIXEL_MODE] = mPrefs.getInt("num", 50);
//
//        mPrefs = baseContext.getSharedPreferences("rollSpeedLineMode", Context.MODE_PRIVATE);
//        SettingContent.rollSpeed[ROLL_LINE_MODE] = mPrefs.getInt("num", 50);
//
//        mPrefs = baseContext.getSharedPreferences("rollSpeedPageUnDownMode", Context.MODE_PRIVATE);
//        SettingContent.rollSpeed[ROLL_PAGE_UP_DOWN_MODE] = mPrefs.getInt("num", 50);
//
//        mPrefs = baseContext.getSharedPreferences("rollSpeedPageLeftRightMode", Context.MODE_PRIVATE);
//        SettingContent.rollSpeed[ROLL_PAGE_LEFT_RIGHT_MODE] = mPrefs.getInt("num", 50);
//
//
//        // 网络连接设定
//        mPrefs = baseContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
//        setting.connect_auto = mPrefs.getBoolean("connect_auto", false);
//        setting.isShowTips = mPrefs.getBoolean("showTips", true);
//        setting.isSavePower = mPrefs.getBoolean("save_power", false);
//
//        setting.backgroundTypeDayMode = mPrefs.getInt("backgroundTypeDayMode", DEFAULT_BACKGROUND_TYPE);
//        setting.backgroundTypeNightMode = mPrefs.getInt("backgroundTypeNightMode", DEFAULT_BACKGROUND_TYPE);
//
//        setting.mode = mPrefs.getInt("mode", 0);
//        setting.isSupportGravitation = mPrefs.getBoolean("isSupportGravitation", true);
//        setting.isAutoSplitChapter = mPrefs.getBoolean("isAutoSplitChapter", true);
//        setting.isPageTurnningAnimation = mPrefs.getBoolean("isPageTurnningAnimation", true);
//        setting.isDownloadAnimation = mPrefs.getBoolean("isDownloadAnimation", false);
//        setting.isDownloadSound = mPrefs.getBoolean("isDownloadSound", false);
//        setting.isNeedOpenLastRead = mPrefs.getBoolean("isNeedOpenLastRead", true);
//
//        setting.isScreenControl = mPrefs.getBoolean("isScreenControl", true);
//        setting.isChapterNameControl = mPrefs.getBoolean("isChapterNameControl", true);
//        setting.isReadDetailControl = mPrefs.getBoolean("isReadDetailControl", true);
//        setting.skinSwitchModeIndex = mPrefs.getInt("skinSwitchModeIndex", 0);
//
//        mPrefs.getString("backgroundImagePath", BACKGROUND_IAMGE_PATH_DEFAULT);
//        setting.backgroundImagePathDayMode = mPrefs.getString("backgroundImagePathDayMode", BACKGROUND_IAMGE_PATH_DEFAULT_DAYMODE);
//        setting.backgroundImagePathNightMode = mPrefs.getString("backgroundImagePathNightMode", BACKGROUND_IAMGE_PATH_DEFAULT_NIGHTMODE);
//
//        String defaultSchemeNameDayMode = baseContext.getString(R.string.scheme_daymode);
//        String defaultSchemeNameNightMode = baseContext.getString(R.string.scheme_night_1);
//        setting.dayModeSchemeName = mPrefs.getString("dayModeSchemeName", defaultSchemeNameDayMode);
//        setting.nightModeSchemeName = mPrefs.getString("nightModeSchemeName", defaultSchemeNameNightMode);
//        setting.eyestrainType = mPrefs.getInt(TAG_EYESTRAIN, DEFAULT_EYESTRAIN_TYPE);
//        setting.settingspaceType = mPrefs.getInt(TAG_SETTINGSPACE, DEFAULT_SETTINGSPACE_TYPE);
//
//        setting.isDayMode = mPrefs.getBoolean("isDayMode", true);
//        setting.setDisplayingDayMode(setting.isDayMode);
//
//
//        setting.currentHomePageMode = mPrefs.getInt("homepagemode", HomeSwitch.DEFAULT_HOMEPAGE_MODE);
//        setting.isHomePageChange = mPrefs.getBoolean("homepagemodechange", false);
//        setting.read_screen_set = mPrefs.getInt("read_screen_set", DEFAULT_READ_SCREEN);
//        setting.defaultNoteColor = mPrefs.getInt("defaultNoteColor", DEFAULT_NOTE_COLOR);
//        setting.sort = mPrefs.getInt(CODE_SORT, DEFAULT_SORT);
//        setting.bookshelfDisplayMode = mPrefs.getInt(CODE_BOOKSHELF_DISPLAY_MODE, BOOKSHELF_SHELF_MODE);
//        setting.folderSite = mPrefs.getInt(CODE_FOLDER_SITE, DEFAULT_FOLDER_SITE);
//
//        mPrefs.getInt("backgroundType", DEFAULT_BACKGROUND_TYPE);
//
//        int system_bright = Settings.System.getInt(baseContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, DEFAULT_SCREEN_BRIGHTNESS[MODE_DAY]);
//        int mode_day_bright = mPrefs.getInt(TAG_BRIGHTNESS[MODE_DAY], system_bright);
//        setting.screenBrightness[MODE_DAY] = BrightnessRegulatorFit.getInstance().isDayFit() && mode_day_bright < 25 ? DEFAULT_SCREEN_BRIGHTNESS[MODE_DAY] : mode_day_bright;
//        setting.screenBrightness[MODE_NIGHT] = mPrefs.getInt(TAG_BRIGHTNESS[MODE_NIGHT], DEFAULT_SCREEN_BRIGHTNESS[MODE_NIGHT]);
//
//        setting.mIsFollowSystemBrightness = mPrefs.getBoolean(TAG_FOLLOW_SYSTEM_BRIGHTNESS, DEFAULT_FOLLOW_SYSTEM_BRIGHTNESS);
//
//		/*翻页效果*/
//        setting.pageturningMode = mPrefs.getInt("pageturningMode", DEFAULT_PAGETURNING_MODE);
//        setting.pageTurningAnimationMode = mPrefs.getInt("pageTurningAnimationMode", DEFAULT_PAGETURNING_ANIMATION_MODE);
//        setting.pageTurnAlwaysTurnNext = mPrefs.getBoolean("pageTurnAlwaysTurnNext", DEFAULT_PAGETURNING_ALWAYS_TURN_NEXT);
//        converSchemeFromVer1100to1200();
//        return setting;
//    }
//
//    public static void converSchemeFromVer1100to1200() {
//        //只在第一次启动时候做转换
//        if (!PreferenceUtils.isInitialized(baseContext.getString(R.string.version))) {
//            if (setting != null) {
//                if (setting.backgroundImagePathDayMode != null) {
//                    //牛皮�?
//                    if (setting.backgroundImagePathDayMode.contains("1")) {
//                        setting.backgroundImagePathDayMode = setting.backgroundImagePathDayMode.replace("1", "2");
//                        return;
//                    }
//                    //少女情�??
//                    if (setting.backgroundImagePathDayMode.contains("7")) {
//                        setting.setBackgroundType(SettingScheme.BACKGROUND_TYPE_COLOR);
//                        setting.setBackgroundColor(-336687);
//                        setting.setTextColor(-15636475, 0);
//
//                    }
//                    //美丽星空
//                    if (setting.backgroundImagePathDayMode.contains("8")) {
//                        setting.setBackgroundType(SettingScheme.BACKGROUND_TYPE_COLOR);
//                        setting.setBackgroundColor(-14582395);
//                        setting.setTextColor(-13421773, 0);
//                        return;
//                    }
//                    //蓝色图腾
//                    if (setting.backgroundImagePathDayMode.contains("9")) {
//                        setting.backgroundImagePathDayMode = setting.backgroundImagePathDayMode.replace("9", "4");
//                        return;
//                    }
//
//                    //经典1 经典2
//                    if (setting.backgroundImagePathDayMode.contains("12") || setting.backgroundImagePathDayMode.contains("11")
//                            || setting.backgroundImagePathDayMode.contains("5")) {
//                        setting.setBackgroundType(SettingScheme.BACKGROUND_TYPE_COLOR);
//                        setting.setBackgroundColor(-330791);
//                        setting.setTextColor(-16777216, 0);
//                        return;
//                    }
//
//                }
//            }
//        }
//    }
//
//    public void setTextSize(int textSize, boolean readSettingChange) {
//        setTextSizeDayMode(textSize, readSettingChange);
//        setTextSizeNightMode(textSize, readSettingChange);
//    }
//
//    public void setTextSizeDayMode(int textSize, boolean readSettingChange) {
//        if (textSize != this.textSizeDayMode) {
//            LogManager.writeLog(UserOperation.CHANGE_TEXT_SIZE, 0);
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putInt("sizeDayMode", textSize);
//            ed.commit();
//            this.textSizeDayMode = textSize;
//            if (isDayMode && readSettingChange) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setTextSizeNightMode(int textSize, boolean readSettingChange) {
//        if (textSize != this.textSizeNightMode) {
//            LogManager.writeLog(UserOperation.CHANGE_TEXT_SIZE, 0);
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putInt("sizeNightMode", textSize);
//            ed.commit();
//            this.textSizeNightMode = textSize;
//            if (!isDayMode && readSettingChange) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setTextStyle(String textStyle, int styleIndex) {
//        setTextStyleDayMode(textStyle, styleIndex);
//        setTextStyleNightMode(textStyle, styleIndex);
////		if (isDisplayingDayMode) {
////			setTextStyleDayMode(textStyle, styleIndex);
////		}
////		else {
////			setTextStyleNightMode(textStyle, styleIndex);
////		}
//    }
//
//    public void setTextStyleDayMode(String textStyle, int styleIndex) {
//        LogManager.writeLog(UserOperation.CHANGE_TEXT_STYLE, 0);
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putInt("styleIndexDayMode", styleIndex);
//        ed.putString("fontStyleDayMode", textStyle);
//        ed.putBoolean("isFontChange", true);
//        ed.commit();
//
//        this.textStyleNameDayMode = textStyle;
//        this.textStyleIndexDayMode = styleIndex;
//        if (isDayMode) {
//            readSettingChange1(true);
//        }
//    }
//
//
//    public void setTextStyleNightMode(String textStyle, int styleIndex) {
//        LogManager.writeLog(UserOperation.CHANGE_TEXT_STYLE, 0);
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putInt("styleIndexNightMode", styleIndex);
//        ed.putString("fontStyleNightMode", textStyle);
//        ed.putBoolean("isFontChange", true);
//        ed.commit();
//
//        this.textStyleNameNightMode = textStyle;
//        this.textStyleIndexNightMode = styleIndex;
//        if (!isDayMode) {
//            readSettingChange1(true);
//        }
//    }
//
//
//    public void setVSpace(int vSpace) {
//        setVSpaceDayMode(vSpace);
//        setVSpaceNightMode(vSpace);
////		if (isDisplayingDayMode) {
////			setVSpaceDayMode(vSpace);
////		}
////		else {
////			setVSpaceNightMode(vSpace);
////		}
//    }
//
//    public void setVSpaceDayMode(int vSpace) {
//        if (vSpace != this.vSpacingDayMode) {
//            LogManager.writeLog(UserOperation.CHANGE_TEXT_SPACE_VERTICAL, 0);
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putInt("vspaceDayMode", vSpace);
//            ed.commit();
//
//            this.vSpacingDayMode = vSpace;
//            if (isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setVSpaceNightMode(int vSpace) {
//        if (vSpace != this.vSpacingNightMode) {
//            LogManager.writeLog(UserOperation.CHANGE_TEXT_SPACE_VERTICAL, 0);
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putInt("vspaceNightMode", vSpace);
//            ed.commit();
//
//            this.vSpacingNightMode = vSpace;
//            if (!isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setHSpace(int hSpace) {
//        setHSpaceDayMode(hSpace);
//        setHSpaceNightMode(hSpace);
////		if (isDisplayingDayMode) {
////			setHSpaceDayMode(hSpace);
////		}
////		else {
////			setHSpaceNightMode(hSpace);
////		}
//    }
//
//    public void setHSpaceDayMode(int hSpace) {
//        if (hSpace != this.hSpacingDayMode) {
//            LogManager.writeLog(UserOperation.CHANGE_TEXT_SPACE_HORIZONTAL, 0);
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putInt("hspacingDayMode", hSpace);
//            ed.commit();
//
//            this.hSpacingDayMode = hSpace;
//            if (isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setHSpaceNightMode(int hSpace) {
//        if (hSpace != this.hSpacingNightMode) {
//            LogManager.writeLog(UserOperation.CHANGE_TEXT_SPACE_HORIZONTAL, 0);
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putInt("hspacingNightMode", hSpace);
//            ed.commit();
//
//            this.hSpacingNightMode = hSpace;
//            if (!isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setTextBold(String boldFlag) {
//        setTextBoldDayMode(boldFlag);
//        setTextBoldNightMode(boldFlag);
////		if (isDisplayingDayMode) {
////			setTextBoldDayMode(boldFlag);
////		}
////		else {
////			setTextBoldNightMode(boldFlag);
////		}
//    }
//
//    public void setTextBoldDayMode(String boldFlag) {
//        if (boldFlag == null) {
//            if (this.boldFlagDayMode != null) {
//                LogManager.writeLog(UserOperation.CHANGE_TEXT_TO_BOLD, 0);
//                SharedPreferences.Editor ed = getEditor("font");
//                ed.putString("boldDayMode", boldFlag);
//                ed.commit();
//
//                this.boldFlagDayMode = boldFlag;
//                if (isDayMode) {
//                    readSettingChange1(true);
//                }
//            }
//            return;
//        }
//        if (!boldFlag.equals(this.boldFlagDayMode)) {
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putString("boldDayMode", boldFlag);
//            ed.commit();
//
//            this.boldFlagDayMode = boldFlag;
//            if (isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setTextBoldNightMode(String boldFlag) {
//        if (boldFlag == null) {
//            if (this.boldFlagNightMode != null) {
//                LogManager.writeLog(UserOperation.CHANGE_TEXT_TO_BOLD, 0);
//                SharedPreferences.Editor ed = getEditor("font");
//                ed.putString("boldNightMode", boldFlag);
//                ed.commit();
//
//                this.boldFlagNightMode = boldFlag;
//                if (!isDayMode) {
//                    readSettingChange1(true);
//                }
//            }
//            return;
//        }
//        if (!boldFlag.equals(this.boldFlagNightMode)) {
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putString("boldNightMode", boldFlag);
//            ed.commit();
//
//            this.boldFlagNightMode = boldFlag;
//            if (!isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setTextUnderLine(String underLineFlag) {
//        setTextUnderLineDayMode(underLineFlag);
//        setTextUnderLineNightMode(underLineFlag);
////		if (isDisplayingDayMode) {
////			setTextUnderLineDayMode(underLineFlag);
////		}
////		else {
////			setTextUnderLineNightMode(underLineFlag);
////		}
//    }
//
//    public void setTextUnderLineDayMode(String underLineFlag) {
//        if (underLineFlag == null) {
//            if (this.underLineFlagDayMode != null) {
//                LogManager.writeLog(UserOperation.CHANGE_TEXT_TO_UNDERLINE, 0);
//                SharedPreferences.Editor ed = getEditor("font");
//                ed.putString("udlineDayMode", underLineFlag);
//                ed.commit();
//
//                this.underLineFlagDayMode = underLineFlag;
//                if (isDayMode) {
//                    readSettingChange1(true);
//                }
//            }
//            return;
//        }
//        if (!underLineFlag.equals(this.underLineFlagDayMode)) {
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putString("udlineDayMode", underLineFlag);
//            ed.commit();
//
//            this.underLineFlagDayMode = underLineFlag;
//            if (isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setTextUnderLineNightMode(String underLineFlag) {
//        if (underLineFlag == null) {
//            if (this.underLineFlagNightMode != null) {
//                LogManager.writeLog(UserOperation.CHANGE_TEXT_TO_UNDERLINE, 0);
//                SharedPreferences.Editor ed = getEditor("font");
//                ed.putString("udlineNightMode", underLineFlag);
//                ed.commit();
//
//                this.underLineFlagNightMode = underLineFlag;
//                if (!isDayMode) {
//                    readSettingChange1(true);
//                }
//            }
//            return;
//        }
//        if (!underLineFlag.equals(this.underLineFlagNightMode)) {
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putString("udlineNightMode", underLineFlag);
//            ed.commit();
//
//            this.underLineFlagNightMode = underLineFlag;
//            if (!isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setTextItaly(String italyFlag) {
//        setTextItalyDayMode(italyFlag);
//        setTextItalyNightMode(italyFlag);
////		if (isDisplayingDayMode) {
////			setTextItalyDayMode(italyFlag);
////		}
////		else {
////			setTextItalyNightMode(italyFlag);
////		}
//    }
//
//    public void setTextItalyDayMode(String italyFlag) {
//        if (italyFlag == null) {
//            if (this.italicFlagDayMode != null) {
//                LogManager.writeLog(UserOperation.CHANGE_TEXT_TO_ITALY, 0);
//                SharedPreferences.Editor ed = getEditor("font");
//                ed.putString("italicDayMode", italyFlag);
//                ed.commit();
//
//                this.italicFlagDayMode = italyFlag;
//                if (isDayMode) {
//                    readSettingChange1(true);
//                }
//            }
//            return;
//        }
//        if (!italyFlag.equals(this.italicFlagDayMode)) {
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putString("italicDayMode", italyFlag);
//            ed.commit();
//
//            this.italicFlagDayMode = italyFlag;
//            if (isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public void setTextItalyNightMode(String italyFlag) {
//        if (italyFlag == null) {
//            if (this.italicFlagNightMode != null) {
//                LogManager.writeLog(UserOperation.CHANGE_TEXT_TO_ITALY, 0);
//                SharedPreferences.Editor ed = getEditor("font");
//                ed.putString("italicNightMode", italyFlag);
//                ed.commit();
//
//                this.italicFlagNightMode = italyFlag;
//                if (!isDayMode) {
//                    readSettingChange1(true);
//                }
//            }
//            return;
//        }
//        if (!italyFlag.equals(this.italicFlagNightMode)) {
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putString("italicNightMode", italyFlag);
//            ed.commit();
//
//            this.italicFlagNightMode = italyFlag;
//            if (!isDayMode) {
//                readSettingChange1(true);
//            }
//        }
//    }
//
//    public final void setTansPercent(float[] percent) {
//        if (isDisplayingDayMode) {
//            setTansPercentDayMode(percent);
//        } else {
//            setTansPercentNightMode(percent);
//        }
//    }
//
//    public final void setTansPercentDayMode(float[] percent) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putFloat("tansPercentDayMode", percent[PICKER_COLOR]);
//        ed.putFloat("secendTansPercentDayMode", percent[PICKER_SECEND_COLOR]);
//        ed.commit();
//        this.tansPercentDayMode = percent.clone();
//    }
//
//    public final void setTansPercentNightMode(float[] percent) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putFloat("tansPercentNightMod", percent[PICKER_COLOR]);
//        ed.putFloat("secendTansPercentNightMod", percent[PICKER_SECEND_COLOR]);
//        ed.commit();
//        this.tansPercentNightMode = percent.clone();
//    }
//
//    public final void setPoint(PointF[] point) {
//        if (isDisplayingDayMode) {
//            setPointDayMode(point);
//        } else {
//            setPointNightMode(point);
//        }
//    }
//
//    public final void setPointDayMode(PointF[] point) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putFloat("pointXDayMode", point[PICKER_COLOR].x);
//        ed.putFloat("pointYDayMode", point[PICKER_COLOR].y);
//        ed.putFloat("secendPointXDayMode", point[PICKER_SECEND_COLOR].x);
//        ed.putFloat("secendPointYDayMode", point[PICKER_SECEND_COLOR].y);
//        ed.commit();
//        this.pointDayMode = point.clone();
//    }
//
//    public final void setPointNightMode(PointF[] point) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putFloat("pointXNightMode", point[PICKER_COLOR].x);
//        ed.putFloat("pointYNightMode", point[PICKER_COLOR].y);
//
//        ed.putFloat("secendPointXNightMode", point[PICKER_COLOR].x);
//        ed.putFloat("secendPointYNightMode", point[PICKER_COLOR].y);
//        ed.commit();
//        this.pointNightMode = point.clone();
//    }
//
//    public final void setBackgroundColor(int backgroundColor) {
//        if (isDisplayingDayMode) {
//            setBackgroundColorDayMode(backgroundColor);
//        } else {
//            setBackgroundColorNightMode(backgroundColor);
//        }
//    }
//
//    public final void setBackgroundColorDayMode(int backgroundColor) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putInt("backgroundcolorDayMode", backgroundColor);
//        ed.commit();
//        this.backgroundColorDayMode = backgroundColor;
//        if (isDayMode) {
//            readSettingChange1(false);
//        }
//    }
//
//    public final void setBackgroundColorNightMode(int backgroundColor) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putInt("backgroundcolorNightMode", backgroundColor);
//        ed.commit();
//        this.backgroundColorNightMode = backgroundColor;
//        if (!isDayMode) {
//            readSettingChange1(false);
//        }
//    }
//
//    public final void setTextColor(int textColor, int textColorIndex) {
//        if (isDisplayingDayMode) {
//            setTextColorDayMode(textColor, textColorIndex);
//        } else {
//            setTextColorNightMode(textColor, textColorIndex);
//        }
//    }
//
//    public final void setTextColorDayMode(int textColor, int textColorIndex) {
//        if (textColor != this.textColorDayMode) {
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putInt("textcolorDayMode", textColor);
//            ed.putInt("colorDayMode", textColorIndex);
//            ed.commit();
//            this.textColorDayMode = textColor;
//            this.textColorIndexDayMode = textColorIndex;
//            if (isDayMode) {
//                readSettingChange1(false);
//            }
//        }
//    }
//
//    public final void setTextColorNightMode(int textColor, int textColorIndex) {
//        if (textColor != this.textColorNightMode) {
//            SharedPreferences.Editor ed = getEditor("font");
//            ed.putInt("textcolorNightMode", textColor);
//            ed.putInt("colorNightMode", textColorIndex);
//            ed.commit();
//            this.textColorNightMode = textColor;
//            this.textColorIndexNightMode = textColorIndex;
//            if (!isDayMode) {
//                readSettingChange1(false);
//            }
//        }
//    }
//
//    public final void setBackgroundPicIndex(String backgroundPic, int backgroundPicIndex) {
//        if (isDisplayingDayMode) {
//            setBackgroundPicIndexDayMode(backgroundPic, backgroundPicIndex);
//        } else {
//            setBackgroundPicIndexNightMode(backgroundPic, backgroundPicIndex);
//        }
//    }
//
//    public final void setBackgroundPicIndexDayMode(String backgroundPic, int backgroundPicIndex) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putString("backgroundpicDayMode", backgroundPic);
//        ed.putInt("backgroundpicDayMode_index", backgroundPicIndex);
//        ed.commit();
//        this.backgroundPicDayMode = backgroundPic;
//        this.backgroundPicIndexDayMode = backgroundPicIndex;
//        if (read_bg != null) {
//            read_bg.clear();
//        }
//    }
//
//    public final void setBackgroundPicIndexNightMode(String backgroundPic, int backgroundPicIndex) {
//        SharedPreferences.Editor ed = getEditor("font");
//        ed.putString("backgroundpicNightMode", backgroundPic);
//        ed.putInt("backgroundpicNightMode_index", backgroundPicIndex);
//        ed.commit();
//        this.backgroundPicNightMode = backgroundPic;
//        this.backgroundPicIndexNightMode = backgroundPicIndex;
//        if (read_bg != null) {
//            read_bg.clear();
//        }
//    }
//
//    public final void setIndentMode(int mode) {
//        if (mode != INDENT_DISABLE && mode != INDENT_ONE_CHAR && mode != INDENT_TWO_CHAR) {
//            Log.e("error in");
//            mode = INDENT_TWO_CHAR;
//        }
//        SharedPreferences.Editor ed = getEditor("indentMode");
//        ed.putInt("mode", mode);
//        ed.commit();
//        this.indentMode = mode;
//        readSettingChange1(true);
//    }
//
//    public final void setParagraphDistance(float distance) {
//        if (distance <= 0) {
//            Log.e("error in");
//            distance = PARAGROUPH_DEFAULT_DISTANCE;
//        }
//        SharedPreferences.Editor ed = getEditor("paragraphDistance");
//        ed.putFloat("distance", distance);
//        ed.commit();
//        this.paragraphDistance = distance;
//        readSettingChange1(true);
//    }
//
//    public final void setMarginLeft(int value) {
//        if (value < 0) {
//            Log.e("error in");
//            value = 0;
//        }
//        SharedPreferences.Editor ed = getEditor("margin");
//        if (pageturningMode == PAGINGMODE_UD) {
//            ed.putInt("marginLeft", value);
//            this.marginLeft = value;
//        } else {
//            ed.putInt("marginBookModeLeft", value);
//            this.marginBookModeLeft = value;
//        }
//        ed.commit();
//        TextDraw.PADDING_LEFT = Utils.dipDimensionInteger(value);
//        readSettingChange1(true);
//    }
//
//    public final void setMarginRight(int value) {
//        if (value < 0) {
//            Log.e("error in");
//            value = 0;
//        }
//        SharedPreferences.Editor ed = getEditor("margin");
//        if (pageturningMode == PAGINGMODE_UD) {
//            ed.putInt("marginRight", value);
//            this.marginRight = value;
//        } else {
//            ed.putInt("marginBookModeRight", value);
//            this.marginBookModeRight = value;
//        }
//        ed.commit();
//        TextDraw.PADDING_RIGHT = Utils.dipDimensionInteger(value);
//        readSettingChange1(true);
//    }
//
//    public final void setMarginTop(int value) {
//        if (value < 0) {
//            Log.e("error in");
//            value = 0;
//        }
//        SharedPreferences.Editor ed = getEditor("margin");
//        if (pageturningMode == PAGINGMODE_UD) {
//            ed.putInt("marginTop", value);
//            this.marginTop = value;
//        } else {
//            ed.putInt("marginBookModeTop", value);
//            this.marginBookModeTop = value;
//        }
//        ed.commit();
//        TextDraw.PADDING_TOP = Utils.dipDimensionInteger(value);
//        readSettingChange1(true);
//    }
//
//    public final void setMarginBottom(int value) {
//        if (value < 0) {
//            Log.e("error in");
//            value = 0;
//        }
//        SharedPreferences.Editor ed = getEditor("margin");
//        if (pageturningMode == PAGINGMODE_UD) {
//            ed.putInt("marginBottom", value);
//            this.marginBottom = value;
//        } else {
//            ed.putInt("marginBookModeBottom", value);
//            this.marginBookModeBottom = value;
//        }
//        ed.commit();
//        TextDraw.PADDING_BOTTOM = Utils.dipDimensionInteger(value);
//        readSettingChange1(true);
//    }
//
//    public final void setKeepOneLine(boolean keepOneLine) {
//        SharedPreferences.Editor ed = getEditor("action");
//        ed.putBoolean("isKeepOneLine", keepOneLine);
//        ed.commit();
//        this.keepOneLine = keepOneLine;
//        readSettingChange1(true);
//    }
//
//    public final void setRollStyle(int rollStyleIndex) {
//        if (SettingContent.ROLL_PAGE_LEFT_RIGHT_MODE != rollStyleIndex) {
//            SharedPreferences.Editor ed = getEditor("rollstyle");
//            ed.putInt("num", rollStyleIndex);
//            ed.commit();
//            this.rollStyle = rollStyleIndex;
//        }
//    }
//
//    public final void setRollSpeed(int rollType, int rollSpeed, boolean isCommit) {
//        if (rollSpeed >= 100) {
//            rollSpeed = 99;
//        }
//
//        if (rollSpeed < 0 || rollSpeed > 100) {
//            Log.d("error rollSpeed input");
//            return;
//        }
//
//        if (isCommit) {
//            SharedPreferences.Editor ed = getEditor(getRollModeName(rollType));
//            ed.putInt("num", rollSpeed);
//            ed.commit();
//        }
//
//        SettingContent.rollSpeed[rollType] = rollSpeed;
//    }
//
//    private final String getRollModeName(int rollType) {
//        switch (rollType) {
//            case ROLL_PIXEL_MODE:
//                return "rollSpeedPixelMode";
//            case ROLL_LINE_MODE:
//                return "rollSpeedLineMode";
//            case ROLL_PAGE_UP_DOWN_MODE:
//                return "rollSpeedPageUnDownMode";
//            case ROLL_PAGE_LEFT_RIGHT_MODE:
//                return "rollSpeedPageLeftRightMode";
//            default:
//                Log.d("error rollType input");
//        }
//        return "rollSpeedPixelMode";
//    }
//
//    public final void setCurSkin(String skinName) {
//        LogManager.writeLog(45, 0);
//        SharedPreferences.Editor ed = getEditor("theme");
//        ed.putString("theme", skinName);
//        ed.commit();
//        this.curSkin = skinName;
//    }
//
//    public final String getCurSkin() {
//        return curSkin;
//    }
//
//    public final InputStream getDefaultNoBitmapInputStream() {
//        InputStream in = null;
//        try {
//            in = baseContext.getAssets().open("images/none_picture.png");
//        } catch (Exception e) {
//            Log.e(e);
//        }
//
//        return in;
//    }
//
//    public final int getTextStyleIndex() {
////		return textStyleIndex;
//        return isDisplayingDayMode ? getTextStyleIndexDayMode() : getTextStyleIndexNightMode();
//    }
//
//    public final int getTextStyleIndexDayMode() {
//        return textStyleIndexDayMode;
//    }
//
//    public final int getTextStyleIndexNightMode() {
//        return textStyleIndexNightMode;
//    }
//
//    public final void setTextStyleIndex(int textStyleIndex) {
//        setTextStyleIndexDayMode(textStyleIndex);
//        setTextStyleIndexNightMode(textStyleIndex);
////		if (isDisplayingDayMode) {
////			setTextStyleIndexDayMode(textStyleIndex);
////		}
////		else {
////			setTextStyleIndexNightMode(textStyleIndex);
////		}
//    }
//
//    public final void setTextStyleIndexDayMode(int textStyleIndex) {
//        this.textStyleIndexDayMode = textStyleIndex;
//    }
//
//    public final void setTextStyleIndexNightMode(int textStyleIndex) {
//        this.textStyleIndexNightMode = textStyleIndex;
//    }
//
//    public final String getTextStyleName() {
//        return isDisplayingDayMode ? textStyleNameDayMode : textStyleNameNightMode;
//    }
//
//    public final void setTextStyleName(String textStyleName) {
//        textStyleNameDayMode = textStyleName;
//        textStyleNameNightMode = textStyleName;
////		if (isDisplayingDayMode) {
////			textStyleNameDayMode = textStyleName;
////		}
////		else {
////			textStyleNameNightMode = textStyleName;
////		}
//    }
//
//    public final int getVSpacing() {
////		return vSpacing;
//        return isDisplayingDayMode ? getVSpacingDayMode() : getVSpacingNightMode();
//    }
//
//    public final int getVSpacingDayMode() {
//        return vSpacingDayMode;
//    }
//
//    public final int getVSpacingNightMode() {
//        return vSpacingNightMode;
//    }
//
//    public final int getHSpacing() {
////		return hSpacing;
//        return isDisplayingDayMode ? getHSpacingDayMode() : getHSpacingNightMode();
//    }
//
//    public final int getHSpacingDayMode() {
//        return hSpacingDayMode;
//    }
//
//    public final int getHSpacingNightMode() {
//        return hSpacingNightMode;
//    }
//
//    public final String getBoldFlag() {
////		return boldFlag;
//        return isDisplayingDayMode ? getBoldFlagDayMode() : getBoldFlagNightMode();
//    }
//
//    public final String getBoldFlagDayMode() {
//        return boldFlagDayMode;
//    }
//
//    public final String getBoldFlagNightMode() {
//        return boldFlagNightMode;
//    }
//
//    public final void setBoldFlag(String boldFlag) {
//        setBoldFlagDayMode(boldFlag);
//        setBoldFlagNightMode(boldFlag);
////		if (isDisplayingDayMode) {
////			setBoldFlagDayMode(boldFlag);
////		}
////		else {
////			setBoldFlagNightMode(boldFlag);
////		}
//    }
//
//    public final void setBoldFlagDayMode(String boldFlag) {
//        this.boldFlagDayMode = boldFlag;
//    }
//
//    public final void setBoldFlagNightMode(String boldFlag) {
//        this.boldFlagNightMode = boldFlag;
//    }
//
//    public final String getUnderLineFlag() {
////		return underLineFlag;
//        return isDisplayingDayMode ? getUnderLineFlagDayMode() : getUnderLineFlagNightMode();
//    }
//
//    public final String getUnderLineFlagDayMode() {
//        return underLineFlagDayMode;
//    }
//
//    public final String getUnderLineFlagNightMode() {
//        return underLineFlagNightMode;
//    }
//
//    public final void setUnderLineFlag(String underLineFlag) {
//        setUnderLineFlagDayMode(underLineFlag);
//        setUnderLineFlagNightMode(underLineFlag);
////		if (isDisplayingDayMode) {
////			setUnderLineFlagDayMode(underLineFlag);
////		}
////		else {
////			setUnderLineFlagNightMode(underLineFlag);
////		}
//    }
//
//    public final void setUnderLineFlagDayMode(String underLineFlag) {
//        this.underLineFlagDayMode = underLineFlag;
//    }
//
//    public final void setUnderLineFlagNightMode(String underLineFlag) {
//        this.underLineFlagNightMode = underLineFlag;
//    }
//
//    public final String getItalicFlag() {
////		return italicFlag;
//        return isDisplayingDayMode ? getItalicFlagDayMode() : getItalicFlagNightMode();
//    }
//
//    public final String getItalicFlagDayMode() {
//        return italicFlagDayMode;
//    }
//
//    public final String getItalicFlagNightMode() {
//        return italicFlagNightMode;
//    }
//
//    public final void setConnectAuto(boolean is) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("connect_auto", is);
//        ed.commit();
//        this.connect_auto = is;
//    }
//
//    public final void setShowTips(boolean is) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("showTips", is);
//        ed.commit();
//        this.isShowTips = is;
//    }
//
//    public final int getBackgroundColorIndex() {
//        return isDisplayingDayMode ? backgroundColorIndexDayMode : backgroundColorIndexNightMode;
//    }
//
//    public final int getTextColorIndex() {
////		return textColorIndex;
//        return isDisplayingDayMode ? getTextColorIndexDayMode() : getTextColorIndexNightMode();
//    }
//
//    public final int getTextColorIndexDayMode() {
//        return textColorIndexDayMode;
//    }
//
//    public final int getTextColorIndexNightMode() {
//        return textColorIndexNightMode;
//    }
//
//    public final void setTextColorIndex(int textColorIndex) {
//        if (isDisplayingDayMode) {
//            setTextColorIndexDayMode(textColorIndex);
//        } else {
//            setTextColorIndexNightMode(textColorIndex);
//        }
//    }
//
//    public final void setTextColorIndexDayMode(int textColorIndex) {
//        this.textColorIndexDayMode = textColorIndex;
//    }
//
//    public final void setTextColorIndexNightMode(int textColorIndex) {
//        this.textColorIndexNightMode = textColorIndex;
//    }
//
//    public final int getBackgroundPicIndex() {
//        return isDisplayingDayMode ? backgroundPicIndexDayMode : backgroundPicIndexNightMode;
//    }
//
//    public final String getBackgroundPic() {
//        return isDisplayingDayMode ? backgroundPicDayMode : backgroundPicNightMode;
//    }
//
//    public final boolean isIndentModeDisable() {
//        return indentMode == INDENT_DISABLE;
//    }
//
//    public final boolean isKeepOneLine() {
//        return keepOneLine;
//    }
//
//    public final int getRollSpeed() {
//        return rollSpeed[getRollStyle()];
//    }
//
//    public final int getRollTime() {
//        return rollTimes[getRollStyle()][0] + (100 - rollSpeed[getRollStyle()]) * (rollTimes[getRollStyle()][1] - rollTimes[getRollStyle()][0]) / 100;
//    }
//
//    /*
//     * 	return value ROLL_PIXEL_MODE , ROLL_LINE_MODE, ROLL_PAGE_UP_DOWN_MODE, ROLL_PAGE_LEFT_RIGHT_MODE
//     */
//    public int getRollStyle() {
//        if (pageturningMode == PAGINGMODE_LR) {
//            return ROLL_PAGE_LEFT_RIGHT_MODE;
//        }
//        if (rollStyle == ROLL_PAGE_LEFT_RIGHT_MODE) {
//            return ROLL_LINE_MODE;
//        }
//        return rollStyle;
//    }
//
//    public final float[] getTansPercent() {
//        return isDisplayingDayMode ? getTansPercentDayMode() : getTansPercentNightMode();
//    }
//
//    public final float[] getTansPercentDayMode() {
//        return tansPercentDayMode.clone();
//    }
//
//    public final float[] getTansPercentNightMode() {
//        return tansPercentNightMode.clone();
//    }
//
//    public final PointF[] getPoint() {
//        return isDisplayingDayMode ? getPointDayMode() : getPointNightMode();
//    }
//
//    public final PointF[] getPointDayMode() {
//        return pointDayMode.clone();
//    }
//
//    public final PointF[] getPointNightMode() {
//        return pointNightMode.clone();
//    }
//
//    public final int getBackgroundColor() {
//        return isDisplayingDayMode ? getBackgroundColorDayMode() : getBackgroundColorNightMode();
//    }
//
//    public final int getBackgroundColorDayMode() {
//        return backgroundColorDayMode;
//    }
//
//    public final int getBackgroundColorNightMode() {
//        return backgroundColorNightMode;
//    }
//
//    public final int getTextColor() {
//        //return textColor;
//        return isDisplayingDayMode ? getTextColorDayMode() : getTextColorNightMode();
//    }
//
//    public final int getTextColorDayMode() {
//        return textColorDayMode;
//    }
//
//    public final int getTextColorNightMode() {
//        return textColorNightMode;
//    }
//
//    public final boolean isReadSettingChange() {
//        return isReadSettingChange;
//    }
//
//    public final void setReadSettingChange(boolean is) {
//        if (is) {
//            readSettingChange1(true);
//        } else {
//            isReadSettingChange = is;
//        }
//    }
//
//    public final int getTextSize() {
//        //return textSize;
//        return isDisplayingDayMode ? getTextSizeDayMode() : getTextSizeNightMode();
//    }
//
//    public final int getTextSizeDayMode() {
//        return textSizeDayMode;
//    }
//
//    public final int getTextSizeNightMode() {
//        return textSizeNightMode;
//    }
//
//    public final boolean getIsConnectAuto() {
//        return connect_auto;
//    }
//
//    public final boolean isShowTips() {
//        return this.isShowTips;
//    }
//
//    public final DrawableContainer getShelfGuide() {
//        AnimationDrawable guideDrawable = new AnimationDrawable();
//
//        DrawableContainer.DrawableContainerState guideDrawableState = (DrawableContainer.DrawableContainerState) guideDrawable.getConstantState();
//        if (guideDrawableState != null) {
//
//            Drawable drawable = getDrawable("guide/shelf_guides_src1.jpg", "jpg");
//            if (drawable != null)
//                guideDrawableState.addChild(drawable);
//
//            drawable = getDrawable("guide/shelf_guides_src2.jpg", "jpg");
//            if (drawable != null)
//                guideDrawableState.addChild(drawable);
//
////			drawable = getDrawable("guide/shelf_guides_src3.jpg", "jpg");
////			if(drawable!=null)
////				guideDrawableState.addChild(drawable);
////
////			drawable = getDrawable("guide/shelf_guides_src4.jpg", "jpg");
////			if(drawable!=null)
////				guideDrawableState.addChild(drawable);
//
//        }
//
//        return guideDrawable;
//    }
//
//    private final Drawable getDrawable(String path, String type) {
//        SoftReference<Drawable> softReference = null;
//        InputStream in;
//        try {
//            in = baseContext.getAssets().open(path);
//            softReference = new SoftReference<Drawable>(Drawable.createFromStream(in, type));
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return softReference.get();
//    }
//
//    private static SharedPreferences.Editor getEditor(String name) {
//        SharedPreferences mPrefs = baseContext.getSharedPreferences(name, Context.MODE_PRIVATE);
//        SharedPreferences.Editor ed = mPrefs.edit();
//        return ed;
//    }
//
//    public final boolean isSavePower() {
//        return this.isSavePower;
//    }
//
//    public final void setSavePower(boolean isSavePower) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("save_power", isSavePower);
//        ed.commit();
//        this.isSavePower = isSavePower;
//    }
//
//    public final int getPageturningMode() {
//        return pageturningMode;
//    }
//
//    public void setPageturningMode(int pageturningMode) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt("pageturningMode", pageturningMode);
//        ed.commit();
//        this.pageturningMode = pageturningMode;
//        updateMargin();
//    }
//
//    public int getPageturningAnimationMode() {
//        return pageTurningAnimationMode;
//    }
//
//    public final boolean isAlwaysTurnNext() {
//        return pageTurnAlwaysTurnNext;
//    }
//
//    public void updateMargin() {
//        TextDraw.PADDING_TOP = Utils.dipDimensionInteger(SettingContent.getInstance().getMarginTop());
//        TextDraw.PADDING_LEFT = Utils.dipDimensionInteger(SettingContent.getInstance().getMarginLeft());
//        TextDraw.PADDING_RIGHT = Utils.dipDimensionInteger(SettingContent.getInstance().getMarginRight());
//        TextDraw.PADDING_BOTTOM = Utils.dipDimensionInteger(SettingContent.getInstance().getMarginBottom());
//    }
//
//    public void setPageTurningAnimationMode(int pageTurningAnimationMode) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putInt("pageTurningAnimationMode", pageTurningAnimationMode);
//        ed.commit();
//        this.pageTurningAnimationMode = pageTurningAnimationMode;
//    }
//
//    public void setPageTurnAlwaysTurnNext(boolean isAlwaysTurnNext) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("pageTurnAlwaysTurnNext", isAlwaysTurnNext);
//        ed.commit();
//        this.pageTurnAlwaysTurnNext = isAlwaysTurnNext;
//    }
//
//    public int getDefaultNoteColor() {
//        return defaultNoteColor;
//    }
//
//    public void setDefaultNoteColor(int defaultNoteColor) {
//        if (defaultNoteColor != this.defaultNoteColor) {
//            SharedPreferences.Editor ed = getEditor("setting");
//            ed.putInt("defaultNoteColor", defaultNoteColor);
//            ed.commit();
//            this.defaultNoteColor = defaultNoteColor;
//        }
//    }
//
//    public float getDensity() {
//        return density;
//    }
//
//    public int getDensityDpi() {
//        return densityDpi;
//    }
//
//
//    public void setDayMode(boolean isDayMode) {
//        SharedPreferences.Editor ed = getEditor("setting");
//        ed.putBoolean("isDayMode", isDayMode);
//        ed.commit();
//        this.isDayMode = isDayMode;
//        setDisplayingDayMode(isDayMode);
//        readSettingChange1(false);
//    }
//
//    public boolean getDayMode() {
//        return isDayMode;
//    }
//
//    //正在显示的是否是白天模式
//    private boolean isDisplayingDayMode = isDayMode;
//
//    public void setDisplayingDayMode(boolean is) {
//        this.isDisplayingDayMode = is;
//    }
//
//    public boolean getDisplayingDayMode() {
//        return isDisplayingDayMode;
//    }
//
//    //是否�?要更新配色方案页�?
//
//    private boolean needUpdateThemePage = false;
//
//    public boolean getNeedUpdateThemePage() {
//        return needUpdateThemePage;
//    }
//
//    public void setNeedUpdateThemePage(boolean needUpdateThemePage) {
//        this.needUpdateThemePage = needUpdateThemePage;
//    }
//
//    public int getDayNeightMode() {
//
//        return getDisplayingDayMode() ? MODE_DAY : MODE_NIGHT;
//    }
//
//    public int getPickerColorchanged() {
//        return pickerColorchanged;
//    }
//
//    public void setPickerColorchanged(int pickerColorchanged) {
//        this.pickerColorchanged = pickerColorchanged;
//    }
//
//    public static int[] getDefaultColorDayMode() {
//        return DEFAULT_COLOR_DAYMODE.clone();
//    }
//
//    public static int[] getDefaultColorNightMode() {
//        return DEFAULT_COLOR_NIGHTMODE.clone();
//    }
//
//    public static PointF[] getDefaultPointDayMode() {
//        PointF[] defaultpoint = DEFAULT_POINT_DAYMODE.clone();
//
//        defaultpoint[PICKER_COLOR] = adjustPoint(defaultpoint[PICKER_COLOR]);
//        defaultpoint[PICKER_SECEND_COLOR] = adjustPoint(defaultpoint[PICKER_SECEND_COLOR]);
//
//        return defaultpoint;
//    }
//
//    public static PointF[] getDefaultPointNightMode() {
//        PointF[] defaultpoint = DEFAULT_POINT_NIGHTMODE.clone();
//
//        defaultpoint[PICKER_COLOR] = adjustPoint(defaultpoint[PICKER_COLOR]);
//        defaultpoint[PICKER_SECEND_COLOR] = adjustPoint(defaultpoint[PICKER_SECEND_COLOR]);
//
//        return defaultpoint;
//    }
//
//    public static float[] getDefaultTransparentDayMode() {
//        return DEFAULT_TRANSPARENT_DAYMODE.clone();
//    }
//
//    public static float[] getDefaultTransparentNightMode() {
//        return DEFAULT_TRANSPARENT_NIGHTMODE.clone();
//    }
//
//    /**
//     * 依据自身的屏幕调整相对于基准(480x800)的自定义颜色区域X,Y�?.
//     *
//     * @param point
//     * @return
//     */
//    private static PointF adjustPoint(PointF point) {
//        PointF desPoint = new PointF(NONE_POINT, NONE_POINT);
//        if (point != null) {
//            float width = baseContext.getResources().getDisplayMetrics().widthPixels;
//            float percent = width / BENCHMARK;
//
//            desPoint.x = point.x * percent;
//            desPoint.y = point.y * percent;
//        }
//
//        return desPoint;
//    }
//
//    public static void clearReadBg() {
//        if (read_bg != null) {
//            read_bg.clear();
//        }
//    }
//
//    public final void readSettingChange1(boolean needReRender) {
//        isReadSettingChange = true;
//        if (needReRender) {
//            BookReadInfoManager.getInstance().cleanAllCache();
//        }
//    }
//
//    public boolean isUpDownDrawMode() {
//        return getPageturningMode() == PAGINGMODE_UD;
//    }
//
//    public boolean isLeftRightDrawMode() {
//        return getPageturningMode() == PAGINGMODE_LR;
//    }
}
