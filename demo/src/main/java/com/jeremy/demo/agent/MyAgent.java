package com.jeremy.demo.agent;

//import android.app.Instrumentation;
import java.lang.instrument.Instrumentation;


public class MyAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("this is an agent.");
        System.out.println("args:" + agentArgs + "\n");
    }

}
