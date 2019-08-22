package com.example.xdemo.mods;

import android.graphics.Color;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findClassIfExists;

public class Mods implements IXposedHookLoadPackage {
    static String strClassName = "";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        String package_name = "com.yitong.zjrc.mfs.android";
        if (lpparam.packageName.equals(package_name)) {
            XposedBridge.log("Loaded app: " + lpparam.packageName);
            XposedBridge.log("开始获取属性：");
//            Field[] fields = param.thisObject.getClass().getDeclaredFields();
//            for (int i = 0; i < fields.length; i++) {
//                XposedBridge.log("======【属性】==args[1]=" + fields[i].getName());
//            }
//            XposedBridge.log("获取属性完成：");

            XposedBridge.log("----------------,test");
//            System.out.println(lpparam.classLoader);
//            findClassIfExists("com.yitong.zjrc.mfs.android/com.yitong.mbank.app.android.activity.MainActivity",lpparam.classLoader);

//            System.out.println(findClass("com.yitong.mbank.app.android.activity.MainActivity",lpparam.classLoader));
//            System.out.println(findClass("com.yitong.zjrc.mfs.android", lpparam.classLoader));
//            System.out.println(findClassIfExists("com.yitong.zjrc.mfs.android", lpparam.classLoader));
//            XposedBridge.log("---");


            findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {

                // 在类方法loadClass执行之后执行的代码
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    // 参数的检查
                    if (param.hasThrowable()) {
                        return;
                    }
                    // 获取指定名称的类加载之后的Class<?>
                    Class<?> clazz = (Class<?>) param.getResult();
                    // 获取加载的指定类的名称
                    String strClazz = clazz.getName();
//                  // 被Hook操作的目标类名称
//                  String strClazzName = "";
//                  // 被Hook操作的类方法的名称
//                  String strMethodName = "";
//                    XposedBridge.log("LoadClass : " + strClazz);
                    // 所有的类都是通过loadClass方法加载的
                    // 过滤掉Android系统的类以及一些常见的java类库
                    if (strClazz.startsWith("com.yitong") && strClazz.startsWith("com.yitong.mbank.app.android.application.MyApplication") && !strClazz.startsWith("com.yitong.mbank.app.android.application.MyApplication$")) {
                        // 或者只Hook加密算法类、网络数据传输类、按钮事件类等协议分析的重要类
//                        XposedBridge.log("HookedClass : " + strClazz);
                        // 获取到指定名称类声明的所有方法的信息
//                        Method[] m = clazz.getMethods();
//                        XposedBridge.log(m.length + "");
                        // 同步处理一下
                        synchronized (this.getClass()) {
                            // 获取被Hook的目标类的名称
                            strClassName = strClazz;
                            XposedBridge.log("HookedClass : " + strClazz);
                            // 获取到指定名称类声明的所有方法的信息
                            Method[] m = clazz.getMethods();
                            XposedBridge.log(m.length + "");
                            // 打印获取到的所有的类方法的信息
                            for (int i = 0; i < m.length; i++) {
//                                XposedBridge.log("HOOKED CLASS-METHOD: "+strClazz+"-"+m[i].toString());
                                if (!Modifier.isAbstract(m[i].getModifiers())           // 过滤掉指定名称类中声明的抽象方法
                                        && !Modifier.isNative(m[i].getModifiers())     // 过滤掉指定名称类中声明的Native方法
                                        && !Modifier.isInterface(m[i].getModifiers())  // 过滤掉指定名称类中声明的接口方法
                                ) {
//                                    // 对指定名称类中声明的非抽象方法进行java Hook处理
                                    XposedBridge.hookMethod(m[i], new XC_MethodHook() {
                                        //                                        // 被java Hook的类方法执行完毕之后，打印log日志
                                        @Override
                                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                            // 打印被java Hook的类方法的名称和参数类型等信息
                                            if (!param.method.toString().contains("java.")
                                                    && !param.method.toString().contains("android.content.pm.ApplicationInfo")
                                                    && !param.method.toString().contains("android.content.res.TypedArray")
                                                    && !param.method.toString().contains("android.content.Context")) {
                                                XposedBridge.log("HOOKED METHOD: " + strClassName + "-" + param.method.toString());
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            });


        }

//        if (!lpparam.packageName.equals("com.android.systemui"))
//            return;

//        findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                TextView tv = (TextView) param.thisObject;
//                String text = tv.getText().toString();
//                tv.setText(text + " :)");
//                tv.setTextColor(Color.GREEN);
//            }
//        });
    }
}
