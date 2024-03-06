package com.zhangyq.generate.code.common;

/**
 * @author zhangyq01
 * @ClassName: DialogPluginSettings
 * @date 2024/1/26
 */
public class DialogPluginSettings {
    private static int dialogWidth;
    private static int dialogHeight;

    public static int getDialogWidth() {
        return (dialogWidth > 0) ? dialogWidth : 800;
    }

    public static void setDialogWidth(int dialogWidth) {
        DialogPluginSettings.dialogWidth = dialogWidth;
    }

    public static int getDialogHeight() {
        return (dialogHeight > 0) ? dialogHeight : 700;
    }

    public static void setDialogHeight(int dialogHeight) {
        DialogPluginSettings.dialogHeight = dialogHeight;
    }
}
