package com.zhangyq.generate.code.dialog.test;

import com.intellij.ide.util.TreeFileChooser;
import com.intellij.ide.util.TreeFileChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.*;
import com.intellij.refactoring.ui.MemberSelectionTable;
import com.intellij.refactoring.util.classMembers.MemberInfo;
import com.intellij.testIntegration.TestIntegrationUtils;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.zhangyq.generate.code.common.TestDialogPluginSettings;
import com.zhangyq.generate.code.common.ValueContext;
import com.zhangyq.generate.util.FileUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
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
    private JLabel baseTestLabel;
    private JPanel baseTestPanel;
    private JLabel baseTestChooseLabel;
    private PsiClass psiClass;
    final GridConstraints constraints;
    private final Project project;
    private final MemberSelectionTable myMethodsTable;
    private final MemberSelectionTable myFieldsTable;
    private TextFieldWithBrowseButton fileTextField;
    private final TreeFileChooserFactory instance;
    private final TreeFileChooser.PsiFileFilter fileFilter = file -> file.getName().endsWith(".java");


    public FieldAndMethodConfirmPanel(PsiClass psiClass, Project project) {
        super(psiClass.getProject());
        this.psiClass = psiClass;
        this.project = project;
        this.constraints = new GridConstraints();
        this.myFieldsTable = new MemberSelectionTable(Collections.emptyList(), null);
        this.myMethodsTable = new MemberSelectionTable(Collections.emptyList(), null);
        this.instance = TreeFileChooserFactory.getInstance(project);
        myMethodsTable.setName("Methods");
        myFieldsTable.setName("Fields");
        init();

        baseTestChooseLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int button = e.getButton();
                //左键
                if(MouseEvent.BUTTON1 == button) {
                    showTreeFileChooser();
                    return;
                }

                if (MouseEvent.BUTTON3 == button) {
                    //右键
                    baseTestChooseLabel.setText("choose BaseTest");
                    ValueContext.setBaseTestFile(null);
                }
            }
        });

        PsiFile baseTestFile = ValueContext.getBaseTestFile();
        if(Objects.nonNull(baseTestFile)) {
            baseTestChooseLabel.setText(FileUtil.getRelativePath(project, baseTestFile));
        }

    }

    private void showTreeFileChooser() {
        // 创建树形结构的文件选择器
        TreeFileChooser javaFileChooser = instance.createFileChooser("请选择BaseTest" , null, null, fileFilter);
        javaFileChooser.showDialog();
        PsiFile selectedFile = javaFileChooser.getSelectedFile();
        if(Objects.isNull(selectedFile)) {
            return;
        }
        ValueContext.setBaseTestFile(selectedFile);
        baseTestChooseLabel.setText(FileUtil.getRelativePath(project, selectedFile));

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
        setSize(TestDialogPluginSettings.getClassDialogWidth(), TestDialogPluginSettings.getClassDialogHeight());
        initMethodPanel();
        initFieldPanel();
        super.init();
    }

    private void saveSize() {
        TestDialogPluginSettings.setClassDialogWidth(super.getWindow().getWidth());
        TestDialogPluginSettings.setClassDialogHeight(super.getWindow().getHeight());
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }
}
