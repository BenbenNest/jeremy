// AidlInnerApp.aidl
package com.jeremy.demo;

// Declare any non-default types here with import statements
import com.jeremy.demo.model.Person;
interface AidlInnerApp {
    //in：参数由客户端设置，或者理解成客户端传入参数值。
    //out：参数由服务端设置，或者理解成由服务端返回值。
    //inout：客户端输入端都可以设置，或者理解成可以双向通信。
    String sayHello(in Person person);
    void updateName(out Person person);
}
