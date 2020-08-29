package com.foxsteps.gsonformat.process;

import com.foxsteps.gsonformat.common.FieldHelper;
import com.foxsteps.gsonformat.common.JavaDocUtils;
import com.foxsteps.gsonformat.common.Try;
import com.foxsteps.gsonformat.config.Config;
import com.foxsteps.gsonformat.config.Constant;
import com.foxsteps.gsonformat.entity.ClassEntity;
import com.foxsteps.gsonformat.entity.FieldEntity;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import org.apache.http.util.TextUtils;

/**
 * Created by dim on 16/11/7.
 */
public class LombokProcessor extends Processor {

    @Override
    protected void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        super.onStarProcess(classEntity, factory, cls, visitor);
    }

    @Override
    protected void generateField(PsiElementFactory factory, FieldEntity fieldEntity, PsiClass cls, ClassEntity classEntity) {
        if (fieldEntity.isGenerate()) {
            Try.run(new Try.TryListener() {
                @Override
                public void run() {
                    PsiField field = factory.createFieldFromText(generateLombokFieldText(classEntity, fieldEntity, null), cls);
                    if (Config.getInstant().isUseComment()) {
                        JavaDocUtils.addFieldComment(field, fieldEntity, factory);
                    }
                    cls.add(field);
                }

                @Override
                public void runAgain() {
                    fieldEntity.setFieldName(FieldHelper.generateLuckyFieldName(fieldEntity.getFieldName()));
                    cls.add(factory.createFieldFromText(generateLombokFieldText(classEntity, fieldEntity, Constant.FIXME), cls));
                }

                @Override
                public void error() {
                    cls.addBefore(factory.createCommentFromText("// FIXME generate failure  field " + fieldEntity.getFieldName(), cls), cls.getChildren()[0]);
                }
            });
        }
    }

    @Override
    protected void createGetAndSetMethod(PsiElementFactory factory, PsiClass cls, FieldEntity field) {
    }

    @Override
    protected void onEndGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, PsiClass generateClass, IProcessor visitor) {
        super.onEndGenerateClass(factory, classEntity, parentClass, generateClass, visitor);
    }

    /**
     * 去除getter和setter
     *
     * @param classEntity
     * @param fieldEntity
     * @param fixme
     * @return
     */
    private String generateLombokFieldText(ClassEntity classEntity, FieldEntity fieldEntity, String fixme) {
        fixme = fixme == null ? "" : fixme;

        StringBuilder fieldSb = new StringBuilder();
        String filedName = fieldEntity.getGenerateFieldName();
        if (!TextUtils.isEmpty(classEntity.getExtra())) {
            fieldSb.append(classEntity.getExtra()).append("\n");
            classEntity.setExtra(null);
        }
        if (fieldEntity.getTargetClass() != null) {
            fieldEntity.getTargetClass().setGenerate(true);
        }

        if (Config.getInstant().isFieldPrivateMode()) {
            fieldSb.append("private ").append(fieldEntity.getFullNameType()).append(" ").append(filedName).append(";");
        } else {
            fieldSb.append("public ").append(fieldEntity.getFullNameType()).append(" ").append(filedName).append(";");
        }
        return fieldSb.append(fixme).toString();
    }

}
