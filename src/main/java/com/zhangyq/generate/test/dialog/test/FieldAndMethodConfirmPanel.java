package com.zhangyq.generate.test.dialog.test;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.refactoring.ui.MemberSelectionTable;
import com.intellij.refactoring.util.classMembers.MemberInfo;
import com.intellij.testIntegration.TestIntegrationUtils;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.zhangyq.generate.test.common.DialogPluginSettings;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangyq01
 * @ClassName: FieldAndMethodConfirmPanel
 * @date 2024/1/25
 */
public class FieldAndMethodConfirmPanel extends DialogWrapper {
    private JPanel mainPanel;
    private JLabel fieldLabel;
    private JLabel methodLabel;
    private JPanel methodPanel;
    private JPanel fieldPanel;
    private PsiClass psiClass;
    final GridConstraints constraints;
    private Project project;
    private Editor editor;
    private final MemberSelectionTable myMethodsTable;
    private final MemberSelectionTable myFieldsTable;

    public FieldAndMethodConfirmPanel(PsiClass psiClass, Editor editor, Project project) {
        super(psiClass.getProject());
        this.psiClass = psiClass;
        this.constraints = new GridConstraints();
        this.editor = editor;
        this.project = project;
        this.myFieldsTable = new MemberSelectionTable(Collections.emptyList(), null);
        this.myMethodsTable = new MemberSelectionTable(Collections.emptyList(), null);
        init();
    }

    private void initFieldPanel() {
        List<PsiField> fields = Arrays.stream(psiClass.getFields()).filter(
                a -> !(a.getType() instanceof PsiPrimitiveType)).collect(
                Collectors.toList());
        List<MemberInfo> fieldInfos = new ArrayList<>();
        for (PsiField field : fields) {
            fieldInfos.add(new MemberInfo(field));
        }

        myFieldsTable.setMemberInfos(fieldInfos);

        JBScrollPane scrollPane = new JBScrollPane(myFieldsTable);

        fieldPanel.add(scrollPane, constraints);
    }

    private void initMethodPanel() {
        List<MemberInfo> methods = TestIntegrationUtils.extractClassMethods(this.psiClass, false);
        this.myMethodsTable.setMemberInfos(methods);

        // 将 MemberSelectionTable 放置在滚动窗格中
        JBScrollPane scrollPane = new JBScrollPane(myMethodsTable);

        methodPanel.add(scrollPane, constraints);
    }

    @Override
    protected void dispose() {
        saveSize();
        super.dispose();
    }

    public List<PsiField> getChooseFields() {
        return this.myFieldsTable.getSelectedMemberInfos().stream().map(x -> (PsiField)x.getMember()).collect(Collectors.toList());
    }

    public List<PsiMethod> getChooseMethods() {
        return this.myMethodsTable.getSelectedMemberInfos().stream().map(x -> (PsiMethod)x.getMember()).collect(Collectors.toList());
    }

    @Override
    protected void init() {
        constraints.setRow(0);
        constraints.setFill(GridConstraints.FILL_BOTH);
        setTitle("Choose Fields and Methods to Generate");
        setSize(DialogPluginSettings.getDialogWidth(), DialogPluginSettings.getDialogHeight());
        initMethodPanel();
        initFieldPanel();
        super.init();
    }

    private void saveSize() {
        DialogPluginSettings.setDialogWidth(super.getWindow().getWidth());
        DialogPluginSettings.setDialogHeight(super.getWindow().getHeight());
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }
}
