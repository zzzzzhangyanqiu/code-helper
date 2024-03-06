package com.zhangyq.generate.code.dialog.test;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.refactoring.ui.MemberSelectionTable;
import com.intellij.refactoring.util.classMembers.MemberInfo;
import com.intellij.testIntegration.TestIntegrationUtils;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.zhangyq.generate.code.common.TestDialogPluginSettings;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangyq01
 * @ClassName: MethodSelectDialog
 * @date 2024/3/6
 */
public class MethodSelectDialog extends DialogWrapper {
    private JLabel methodLabel;
    private JPanel methodPanel;
    private JPanel mainPanel;
    private final GridConstraints constraints;
    private final MemberSelectionTable myMethodsTable;


    public MethodSelectDialog(PsiClass psiClass) {
        super(psiClass.getProject());

        this.constraints = new GridConstraints();
        this.constraints.setRow(0);
        this.constraints.setFill(GridConstraints.FILL_BOTH);
        this.myMethodsTable = new MemberSelectionTable(Collections.emptyList(), null);
        setTitle("Choose Methods to Add");
        setSize(TestDialogPluginSettings.getMethodDialogWidth(), TestDialogPluginSettings.getMethodDialogHeight());

        List<MemberInfo> methods = TestIntegrationUtils.extractClassMethods(psiClass, false);
        this.myMethodsTable.setMemberInfos(methods);

        // 将 MemberSelectionTable 放置在滚动窗格中
        JBScrollPane scrollPane = new JBScrollPane(myMethodsTable);

        methodPanel.add(scrollPane, constraints);

        super.init();
    }

    private void saveSize() {
        TestDialogPluginSettings.setMethodDialogWidth(super.getWindow().getWidth());
        TestDialogPluginSettings.setMethodDialogHeight(super.getWindow().getHeight());
    }

    @Override
    protected void dispose() {
        saveSize();
        super.dispose();
    }

    public List<PsiMethod> getChooseMethods() {
        return this.myMethodsTable.getSelectedMemberInfos().stream().map(x -> (PsiMethod)x.getMember()).collect(Collectors.toList());
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.mainPanel;
    }
}
