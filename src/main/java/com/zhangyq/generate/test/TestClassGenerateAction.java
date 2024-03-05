package com.zhangyq.generate.test;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.zhangyq.generate.test.common.ValueContext;
import com.zhangyq.generate.test.dialog.test.FieldAndMethodConfirmPanel;
import com.zhangyq.generate.test.generator.file.UnitTestCodeGenerator;
import com.zhangyq.generate.test.generator.file.FileCreateTask;
import com.zhangyq.generate.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author zhangyq01
 * @ClassName: TestClassGenerateAction
 * @date 2024/1/25
 */
public class TestClassGenerateAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if(Objects.isNull(psiFile)) {
            return;
        }
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        PsiElement element = PluginUtil.findElement(psiFile, editor.getCaretModel().getOffset());

        if(Objects.isNull(element)) {
            return;
        }

        PsiClass psiClass = PluginUtil.getContainingClass(element);

        if(Objects.isNull(psiClass)) {
            return;
        }

        ValueContext.setEvent(e, psiFile, psiClass);
        ValueContext.getContext().loadClass();

        FieldAndMethodConfirmPanel fieldAndMethodConfirmPanel = new FieldAndMethodConfirmPanel(psiClass, editor, e.getData(PlatformDataKeys.PROJECT));
        fieldAndMethodConfirmPanel.show();

        if(fieldAndMethodConfirmPanel.isOK()) {
            try {
                UnitTestCodeGenerator unitTestCodeGenerator = new UnitTestCodeGenerator(fieldAndMethodConfirmPanel.getChooseFields(), fieldAndMethodConfirmPanel.getChooseMethods());
                ApplicationManager.getApplication().runWriteAction(
                        new FileCreateTask(ValueContext.getFilePath(), ValueContext.getFileName(), unitTestCodeGenerator.genContent()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
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
