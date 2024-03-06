package com.zhangyq.generate;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.zhangyq.generate.code.action.test.TestClassGenerateAction;
import com.zhangyq.generate.code.action.test.TestMethodGenerateAction;
import com.zhangyq.generate.util.PluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyq01
 * @ClassName: CodeHelperGroup
 * @date 2024/1/24
 */
public class CodeHelperGroup extends ActionGroup {

    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        if (e == null) {
            return AnAction.EMPTY_ARRAY;
        }
        Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project == null) {
            return AnAction.EMPTY_ARRAY;
        }
        final List<AnAction> children = new ArrayList<>();
        children.add(new TestClassGenerateAction());
        children.add(new TestMethodGenerateAction());
        return children.toArray(new AnAction[0]);
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
