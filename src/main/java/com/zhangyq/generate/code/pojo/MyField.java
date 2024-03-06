package com.zhangyq.generate.code.pojo;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;

/**
 * @author zhangyq01
 * @ClassName: MyField
 * @date 2024/3/6
 */
public class MyField {
    private final PsiType psiType;

    private final PsiField psiField;

    private final String presentableText;

    private final String name;


    public MyField(PsiField psiField) {
        this.psiField = psiField;
        this.psiType = psiField.getType();
        this.presentableText = psiField.getType().getPresentableText();
        this.name = psiField.getName();
    }

    public PsiType getPsiType() {
        return psiType;
    }

    public PsiField getPsiField() {
        return psiField;
    }

    public String getPresentableText() {
        return presentableText;
    }

    public String getName() {
        return name;
    }
}
