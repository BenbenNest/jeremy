package com.jeremy.demo.designpattern.simplefactory;

/**
 * Created by changqing on 2018/3/15.
 */

public class SimpleFactory {

    public Product create(String type) {
        Product product = null;
        if (type == null) {
            throw new RuntimeException("请输入产品类型");
        } else if (type.equals("A")) {
            product = new ProductA();
        } else if (type.equals("B")) {
            product = new ProductB();
        } else {
            throw new RuntimeException("暂不支持该产品类型");
        }
        return product;
    }

}
