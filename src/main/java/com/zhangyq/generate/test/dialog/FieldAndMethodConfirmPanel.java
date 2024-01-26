package com.zhangyq.generate.test.dialog;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.ide.util.gotoByName.ChooseByNamePanel;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.*;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.zhangyq.generate.test.config.DialogPluginSettings;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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

    public FieldAndMethodConfirmPanel(PsiClass psiClass) {
        super(psiClass.getProject());
        this.psiClass = psiClass;
        this.constraints = new GridConstraints();
        init();
    }

    private void initFieldPanel() {
        List<PsiField> collect = Arrays.stream(psiClass.getAllFields()).filter(
                a -> !(a.getType() instanceof PsiPrimitiveType)).collect(
                Collectors.toList());
        CollectionListModel<PsiField> myFields = new CollectionListModel<>(collect);
        JList<PsiField> psiFields = new JBList<>(myFields);
        psiFields.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(psiFields);
        JPanel panel = decorator.createPanel();
        LabeledComponent<JPanel> labeledComponent = LabeledComponent.create(panel, "");
        fieldPanel.add(labeledComponent, constraints);
    }

    private void initMethodPanel() {
        List<PsiMethod> collect = Arrays.stream(psiClass.getMethods()).filter(
                a -> !a.getModifierList().hasModifierProperty(PsiModifier.PRIVATE)).collect(
                Collectors.toList());
        CollectionListModel<PsiMethod> myMethod = new CollectionListModel<>(collect);
        JBList<PsiMethod> jMethodList = new JBList<>(myMethod);
        jMethodList.setCellRenderer(new DefaultPsiElementCellRenderer());
        jMethodList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(jMethodList);
        JPanel panel = decorator.createPanel();
        LabeledComponent<JPanel> labeledComponent = LabeledComponent.create(panel, "");
        methodPanel.add(labeledComponent, constraints);
        //String[] methodNames = getMethodNames(this.psiClass);
        //JList<String> methodList = new JBList<>(methodNames);
        //methodPanel.add(new JScrollPane(methodList), constraints);
    }



    private String[] getMethodNames(PsiClass selectedClass) {
        // 在这里实现获取选中类的所有方法名的逻辑
        return new String[]{"abc", "cdb"};
    }

    @Override
    protected void dispose() {
        saveSize();
        super.dispose();
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
