package com.zhangyq.generate.test;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtil;
import com.zhangyq.generate.test.config.DialogPluginSettings;
import com.zhangyq.generate.test.dialog.FieldAndMethodConfirmPanel;
import com.zhangyq.generate.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_ELEMENT;

/**
 * @author zhangyq01
 * @ClassName: TestClassGenerateAction
 * @date 2024/1/25
 */
public class TestClassGenerateAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //Project project = e.getData(PlatformDataKeys.PROJECT);
        //
        //PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        PsiElement data = e.getData(PSI_ELEMENT);

        if(Objects.isNull(data)) {
            return;
        }

        PsiClass psiClass = PsiUtil.getTopLevelClass(data);

        FieldAndMethodConfirmPanel fieldAndMethodConfirmPanel = new FieldAndMethodConfirmPanel(psiClass);
        fieldAndMethodConfirmPanel.show();
    }

    public TestClassGenerateAction() {
        super();
        getTemplatePresentation().setText("Generate Test Class");
    }

    /**
     * 只有java文件才能使用
     *
     * @see AnAction#update(AnActionEvent)
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        PluginUtil.update(e);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
