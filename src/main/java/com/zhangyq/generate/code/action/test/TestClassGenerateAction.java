package com.zhangyq.generate.code.action.test;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.zhangyq.generate.code.common.ValueContext;
import com.zhangyq.generate.code.dialog.test.FieldAndMethodConfirmPanel;
import com.zhangyq.generate.code.generator.file.FileCreateTask;
import com.zhangyq.generate.code.generator.file.UnitTestCodeGenerator;
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

        FieldAndMethodConfirmPanel fieldAndMethodConfirmPanel = new FieldAndMethodConfirmPanel(psiClass, e.getData(PlatformDataKeys.PROJECT));
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
}
