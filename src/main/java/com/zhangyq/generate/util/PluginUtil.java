package com.zhangyq.generate.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testIntegration.TestIntegrationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author zhangyq01
 * @ClassName: PluginUtil
 * @date 2024/1/25
 */
public class PluginUtil {
    public static void update(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if(Objects.isNull(psiFile)) {
            return;
        }
        FileType fileType = psiFile.getFileType();
        if (!"JAVA".equals(fileType.getName())) {
            e.getPresentation().setEnabled(false);
        }
    }

    public static PsiClass getContainingClass(PsiElement element) {
        PsiClass aClass = PsiTreeUtil.getParentOfType(element, PsiClass.class, false);
        return aClass == null ? null : TestIntegrationUtils.findOuterClass(element);
    }

    public static PsiElement findElement(PsiFile file, int offset) {
        PsiElement element = file.findElementAt(offset);
        if (element == null && offset == file.getTextLength()) {
            element = file.findElementAt(offset - 1);
        }

        return element;
    }
}
