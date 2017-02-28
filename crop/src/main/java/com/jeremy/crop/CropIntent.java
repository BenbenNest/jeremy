package com.jeremy.crop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

/**
 * Created by changqing.zhao on 2017/2/25.
 */

public class CropIntent extends Intent {

    public void test() {
        AlertDialog.Builder builder = new AlertDialog.Builder(null);
        builder.create();
    }

    public static class Builder {
        CropIntent intent;

        public CropIntent create() {
            return intent;
        }

        public Builder() {
            intent = new CropIntent();
        }

        public Builder(Context context) {
            intent = new CropIntent();
        }

        public Builder putExtra(String name, boolean value) {
            intent.putExtra(name, value);
//            intent.put
//            intent.putExtra()
            return this;
        }

//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The byte data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getByteExtra(String, byte)
//         */
//        public Intent putExtra(String name, byte value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putByte(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The char data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getCharExtra(String, char)
//         */
//        public Intent putExtra(String name, char value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putChar(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The short data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getShortExtra(String, short)
//         */
//        public Intent putExtra(String name, short value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putShort(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The integer data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getIntExtra(String, int)
//         */
//        public Intent putExtra(String name, int value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putInt(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The long data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getLongExtra(String, long)
//         */
//        public Intent putExtra(String name, long value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putLong(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The float data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getFloatExtra(String, float)
//         */
//        public Intent putExtra(String name, float value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putFloat(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The double data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getDoubleExtra(String, double)
//         */
//        public Intent putExtra(String name, double value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putDouble(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The String data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getStringExtra(String)
//         */
//        public Intent putExtra(String name, String value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putString(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The CharSequence data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getCharSequenceExtra(String)
//         */
//        public Intent putExtra(String name, CharSequence value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putCharSequence(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The Parcelable data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getParcelableExtra(String)
//         */
//        public Intent putExtra(String name, Parcelable value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putParcelable(name, value);
//            return this;
//        }
//
//        /**
//         * Add extended data to the intent.  The name must include a package
//         * prefix, for example the app com.android.contacts would use names
//         * like "com.android.contacts.ShowAll".
//         *
//         * @param name The name of the extra data, with package prefix.
//         * @param value The Parcelable[] data value.
//         *
//         * @return Returns the same Intent object, for chaining multiple calls
//         * into a single statement.
//         *
//         * @see #putExtras
//         * @see #removeExtra
//         * @see #getParcelableArrayExtra(String)
//         */
//        public Intent putExtra(String name, Parcelable[] value) {
//            if (mExtras == null) {
//                mExtras = new Bundle();
//            }
//            mExtras.putParcelableArray(name, value);
//            return this;
//        }


    }
}
