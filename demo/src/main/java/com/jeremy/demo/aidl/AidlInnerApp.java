/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/didi/benben/jeremy/demo/src/main/aidl/com/jeremy/demo/AidlInnerApp.aidl
 */
package com.jeremy.demo.aidl;

public interface AidlInnerApp extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements AidlInnerApp {
        private static final String DESCRIPTOR = "com.jeremy.demo.AidlInnerApp";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.jeremy.demo.AidlInnerApp interface,
         * generating a proxy if needed.
         */
        public static AidlInnerApp asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof AidlInnerApp))) {
                return ((AidlInnerApp) iin);
            }
            return new Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_sayHello: {
                    data.enforceInterface(DESCRIPTOR);
                    com.jeremy.demo.model.Person _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = com.jeremy.demo.model.Person.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    String _result = this.sayHello(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case TRANSACTION_updateName: {
                    data.enforceInterface(DESCRIPTOR);
                    com.jeremy.demo.model.Person _arg0;
                    _arg0 = new com.jeremy.demo.model.Person();
                    this.updateName(_arg0);
                    reply.writeNoException();
                    if ((_arg0 != null)) {
                        reply.writeInt(1);
                        _arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements AidlInnerApp {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public String sayHello(com.jeremy.demo.model.Person person) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((person != null)) {
                        _data.writeInt(1);
                        person.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_sayHello, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void updateName(com.jeremy.demo.model.Person person) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_updateName, _data, _reply, 0);
                    _reply.readException();
                    if ((0 != _reply.readInt())) {
                        person.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_sayHello = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_updateName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    }

    public String sayHello(com.jeremy.demo.model.Person person) throws android.os.RemoteException;

    public void updateName(com.jeremy.demo.model.Person person) throws android.os.RemoteException;
}
