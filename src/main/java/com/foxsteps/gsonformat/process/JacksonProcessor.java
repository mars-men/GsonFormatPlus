package com.foxsteps.gsonformat.process;

import com.foxsteps.gsonformat.config.Constant;
import com.foxsteps.gsonformat.entity.ClassEntity;
import com.intellij.psi.*;

/**
 * Created by dim on 16/11/7.
 */
class JacksonProcessor extends Processor {

    @Override
    protected void onStartGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, IProcessor visitor) {
        super.onStartGenerateClass(factory, classEntity, parentClass, visitor);
    }

    @Override
    public void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        super.onStarProcess(classEntity, factory, cls, visitor);
        //injectAnnotation(factory, cls);
    }

    @Override
    protected void onEndGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, PsiClass generateClass, IProcessor visitor) {
        super.onEndGenerateClass(factory, classEntity, parentClass, generateClass, visitor);
        //injectAnnotation(factory, generateClass);
    }

    /**
     * 添加ignore注解
     *
     * @param factory
     * @param generateClass
     */
    private void injectAnnotation(PsiElementFactory factory, PsiClass generateClass) {
        if (factory == null || generateClass == null) {
            return;
        }
        PsiModifierList modifierList = generateClass.getModifierList();
        PsiElement firstChild = modifierList.getFirstChild();
        PsiAnnotation[] annotations = modifierList.getAnnotations();
        Boolean isHasJsonIgnoreFlag = Boolean.FALSE;
        if (annotations != null && annotations.length > 0) {
            for (PsiAnnotation annotation : annotations) {
                if (!isHasJsonIgnoreFlag && annotation.getText().contains(Constant.jsonIgnoreAnnotation)) {
                    isHasJsonIgnoreFlag = Boolean.TRUE;
                }
            }
        }
        if (!isHasJsonIgnoreFlag) {
            PsiAnnotation annotationFromText =
                    factory.createAnnotationFromText("@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)", generateClass);
            //添加类注解
            modifierList.addBefore(annotationFromText, firstChild);
        }
    }
}
