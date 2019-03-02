package com.jeremy.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    private String name;

    public Person(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Person(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    //writeToParcel方法和readFromParcel方法里面的写和读取顺序是需要一一对应的，写的顺序和读的顺序必须一致，因为Parcel类是快速serialization和deserialization机制，和bundle不同，没有索引机制，是线性的数据存贮和读取。 
    //其中readFromParcel()并不是overrider，而是我们自己提供的方法，
    public void readFromParcel(Parcel in){
        name = in.readString();
    }

}
