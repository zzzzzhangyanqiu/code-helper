package com.zhangyq.generate.code.action.test;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.zhangyq.generate.code.common.ValueContext;
import com.zhangyq.generate.code.dialog.test.MethodSelectDialog;
import com.zhangyq.generate.code.generator.file.FileAppendTask;
import com.zhangyq.generate.code.generator.file.UnitTestCodeGenerator;
import com.zhangyq.generate.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangyq01
 * @ClassName: TestMethodGenerateAction
 * @date 2024/3/06
 */
public class TestMethodGenerateAction extends AnAction {

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

        if (!Files.exists(ValueContext.getPath())) {
            String message = "测试类不存在, 请先生成单元测试类";
            Messages.showMessageDialog(e.getProject(), message, "Generate Failed", null);
            return;
        }

        ValueContext.getContext().loadClass();

        MethodSelectDialog methodSelectDialog = new MethodSelectDialog(psiClass);
        methodSelectDialog.show();

        if(methodSelectDialog.isOK()) {
            try {
                List<PsiMethod> chooseMethods = methodSelectDialog.getChooseMethods();
                UnitTestCodeGenerator unitTestCodeGenerator = new UnitTestCodeGenerator(psiClass, chooseMethods, ValueContext.getPsiFile());
                String s = unitTestCodeGenerator.genMethodBody();
                new FileAppendTask(s).run();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public TestMethodGenerateAction() {
        super();
        getTemplatePresentation().setText("Add Test Method");
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
