package com.jeremy.agent;

import java.lang.instrument.Instrumentation;

public class MyAgent {

    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain(String agentOps, Instrumentation inst)方法执行========");
        System.out.println(agentOps);
        // 添加Transformer
        inst.addTransformer(new MyTransformer());
    }

    /**
     * 如果不存在 premain(String agentOps, Instrumentation inst)
     * 则会执行 premain(String agentOps)
     */
    public static void premain(String agentOps) {
        System.out.println("=========premain(String agentOps)方法执行========");
        System.out.println(agentOps);
    }

}
