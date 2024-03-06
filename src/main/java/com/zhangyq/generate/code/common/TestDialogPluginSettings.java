package com.zhangyq.generate.code.common;

/**
 * @author zhangyq01
 * @ClassName: TestDialogPluginSettings
 * @date 2024/1/26
 */
public class TestDialogPluginSettings {
    private static int classDialogWidth;
    private static int classDialogHeight;
    private static int methodDialogWidth;
    private static int methodDialogHeight;

    public static int getClassDialogWidth() {
        return (classDialogWidth > 0) ? classDialogWidth : 800;
    }

    public static void setClassDialogWidth(int classDialogWidth) {
        TestDialogPluginSettings.classDialogWidth = classDialogWidth;
    }

    public static int getClassDialogHeight() {
        return (classDialogHeight > 0) ? classDialogHeight : 700;
    }

    public static void setClassDialogHeight(int classDialogHeight) {
        TestDialogPluginSettings.classDialogHeight = classDialogHeight;
    }

    public static int getMethodDialogWidth() {
        return methodDialogWidth > 0 ? methodDialogWidth : 800;
    }

    public static void setMethodDialogWidth(int methodDialogWidth) {
        TestDialogPluginSettings.methodDialogWidth = methodDialogWidth;
    }

    public static int getMethodDialogHeight() {
        return methodDialogHeight > 0 ? methodDialogHeight : 400;
    }

    public static void setMethodDialogHeight(int methodDialogHeight) {
        TestDialogPluginSettings.methodDialogHeight = methodDialogHeight;
    }
}
