package com.foxsteps.gsonformat.process;

import com.foxsteps.gsonformat.common.FieldHelper;
import com.foxsteps.gsonformat.common.JavaDocUtils;
import com.foxsteps.gsonformat.common.PsiClassUtil;
import com.foxsteps.gsonformat.common.Try;
import com.foxsteps.gsonformat.config.Config;
import com.foxsteps.gsonformat.config.Constant;
import com.foxsteps.gsonformat.entity.ClassEntity;
import com.foxsteps.gsonformat.entity.ConvertLibrary;
import com.foxsteps.gsonformat.entity.FieldEntity;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.http.util.TextUtils;

import java.util.HashMap;
import java.util.Map;

import static com.foxsteps.gsonformat.common.StringUtils.captureName;

/**
 * Created by dim on 16/11/7.
 */
public abstract class Processor {

    private static Map<ConvertLibrary, Processor> sProcessorMap = new HashMap<>();

    protected String mainPackage;

    static {
        sProcessorMap.put(ConvertLibrary.Gson, new GsonProcessor());
        sProcessorMap.put(ConvertLibrary.Jackson, new JacksonProcessor());
        sProcessorMap.put(ConvertLibrary.FastJson, new FastJsonProcessor());
        sProcessorMap.put(ConvertLibrary.AutoValue, new AutoValueProcessor());
        sProcessorMap.put(ConvertLibrary.LoganSquare, new LoganSquareProcessor());
        sProcessorMap.put(ConvertLibrary.Other, new OtherProcessor());
        sProcessorMap.put(ConvertLibrary.Lombok, new LombokProcessor());
    }

    static Processor getProcessor(ConvertLibrary convertLibrary) {
        return sProcessorMap.get(convertLibrary);
    }

    public void process(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        mainPackage = PsiClassUtil.getPackage(cls);
        onStarProcess(classEntity, factory, cls, visitor);
        for (FieldEntity fieldEntity : classEntity.getFields()) {
            generateField(factory, fieldEntity, cls, classEntity);
        }
        for (ClassEntity innerClass : classEntity.getInnerClasses()) {
            generateClass(factory, innerClass, cls, visitor);
        }
        generateGetterAndSetter(factory, cls, classEntity);
        generateConvertMethod(factory, cls, classEntity);
        onEndProcess(classEntity, factory, cls, visitor);
    }

    protected void onEndProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        if (visitor != null) {
            visitor.onEndProcess(classEntity, factory, cls);
        }
        formatJavCode(cls);
    }

    protected void formatJavCode(PsiClass cls) {
        if (cls == null) {
            return;
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(cls.getProject());
        styleManager.optimizeImports(cls.getContainingFile());
        styleManager.shortenClassReferences(cls);
    }

    protected void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        if (visitor != null) {
            visitor.onStarProcess(classEntity, factory, cls);
            //使用Lombok生成Lombok注解
            if (Config.getInstant().isUseLombok()) {
                injectLombokAnnotation(factory, cls);
            }
        }
    }

    protected void generateConvertMethod(PsiElementFactory factory, PsiClass cls, ClassEntity classEntity) {
        if (cls == null || cls.getName() == null) {
            return;
        }
        if (Config.getInstant().isObjectFromData()) {
            createMethod(factory, Config.getInstant().getObjectFromDataStr().replace("$ClassName$", cls.getName()).trim(), cls);
        }
        if (Config.getInstant().isObjectFromData1()) {
            createMethod(factory, Config.getInstant().getObjectFromDataStr1().replace("$ClassName$", cls.getName()).trim(), cls);
        }
        if (Config.getInstant().isArrayFromData()) {
            createMethod(factory, Config.getInstant().getArrayFromDataStr().replace("$ClassName$", cls.getName()).trim(), cls);
        }
        if (Config.getInstant().isArrayFromData1()) {
            createMethod(factory, Config.getInstant().getArrayFromData1Str().replace("$ClassName$", cls.getName()).trim(), cls);
        }
    }

    protected void generateGetterAndSetter(PsiElementFactory factory, PsiClass cls, ClassEntity classEntity) {
        //使用Lombok无需生成Getter与Setter
        if (Config.getInstant().isUseLombok()) {
            return;
        }
        if (Config.getInstant().isFieldPrivateMode()) {
            for (FieldEntity field : classEntity.getFields()) {
                createGetAndSetMethod(factory, cls, field);
            }
        }
    }

    protected void createMethod(PsiElementFactory factory, String method, PsiClass cls) {
        Try.run(new Try.TryListener() {
            @Override
            public void run() {
                cls.add(factory.createMethodFromText(method, cls));
            }

            @Override
            public void runAgain() {

            }

            @Override
            public void error() {

            }
        });
    }

    protected void createGetAndSetMethod(PsiElementFactory factory, PsiClass cls, FieldEntity field) {
        if (field.isGenerate()) {
            String fieldName = field.getGenerateFieldName();
            String typeStr = field.getRealType();
            if (Config.getInstant().isUseFieldNamePrefix()) {
                String temp = fieldName.replaceAll("^" + Config.getInstant().getFiledNamePreFixStr(), "");
                if (!TextUtils.isEmpty(temp)) {
                    fieldName = temp;
                }
            }
            if (typeStr.equals("boolean")) {
                String method = "public ".concat(typeStr).concat("   is").concat(
                        captureName(fieldName)).concat("() {   return ").concat(
                        field.getGenerateFieldName()).concat(" ;} ");
                cls.add(factory.createMethodFromText(method, cls));
            } else {
                String method = "public ".concat(typeStr).concat("   get").concat(
                        captureName(fieldName)).concat(
                        "() {   return ").concat(
                        field.getGenerateFieldName()).concat(" ;} ");
                cls.add(factory.createMethodFromText(method, cls));
            }

            String arg = fieldName;
            if (Config.getInstant().isUseFieldNamePrefix()) {
                String temp = fieldName.replaceAll("^" + Config.getInstant().getFiledNamePreFixStr(), "");
                if (!TextUtils.isEmpty(temp)) {
                    fieldName = temp;
                    arg = fieldName;
                    if (arg.length() > 0) {

                        if (arg.length() > 1) {
                            arg = (arg.charAt(0) + "").toLowerCase() + arg.substring(1);
                        } else {
                            arg = arg.toLowerCase();
                        }
                    }
                }
            }

            String method = "public void  set".concat(captureName(fieldName)).concat("( ").concat(typeStr).concat(" ").concat(arg).concat(") {   ");
            if (field.getGenerateFieldName().equals(arg)) {
                method = method.concat("this.").concat(field.getGenerateFieldName()).concat(" = ").concat(arg).concat(";} ");
            } else {
                method = method.concat(field.getGenerateFieldName()).concat(" = ").concat(arg).concat(";} ");
            }

            String finalMethod = method;
            String finalFieldName = fieldName;
            Try.run(new Try.TryListener() {
                @Override
                public void run() {
                    cls.add(factory.createMethodFromText(finalMethod, cls));
                }

                @Override
                public void runAgain() {
                    cls.addBefore(factory.createCommentFromText("// FIXME generate failure  method  set and get " + captureName(finalFieldName), cls), cls.getChildren()[0]);

                }

                @Override
                public void error() {

                }
            });
        }
    }

    protected void generateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, IProcessor visitor) {

        onStartGenerateClass(factory, classEntity, parentClass, visitor);
        PsiClass generateClass = null;
        if (classEntity.isGenerate()) {
            //// TODO: 16/11/9  待重构 
            if (Config.getInstant().isSplitGenerate()) {
                //单独生成子类
                try {
                    generateClass = PsiClassUtil.getPsiClass(
                            parentClass.getContainingFile(), parentClass.getProject(), classEntity.getQualifiedName());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else {
                //生成内部静态类
                String classContent =
                        "public static class " + classEntity.getClassName() + "{}";
                generateClass = factory.createClassFromText(classContent, null).getInnerClasses()[0];
            }

            if (generateClass != null) {

                for (ClassEntity innerClass : classEntity.getInnerClasses()) {
                    generateClass(factory, innerClass, generateClass, visitor);
                }
                if (!Config.getInstant().isSplitGenerate()) {
                    generateClass = (PsiClass) parentClass.add(generateClass);
                }
                for (FieldEntity fieldEntity : classEntity.getFields()) {
                    generateField(factory, fieldEntity, generateClass, classEntity);
                }
                generateGetterAndSetter(factory, generateClass, classEntity);
                generateConvertMethod(factory, generateClass, classEntity);
            }
        }
        onEndGenerateClass(factory, classEntity, parentClass, generateClass, visitor);
        if (Config.getInstant().isSplitGenerate()) {
            formatJavCode(generateClass);
        }
    }

    /**
     * 开始生成类时
     *
     * @param factory
     * @param classEntity
     * @param parentClass
     * @param visitor
     */
    protected void onStartGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, IProcessor visitor) {
        if (visitor != null) {
            visitor.onStartGenerateClass(factory, classEntity, parentClass);
        }
    }

    /**
     * 结束生成类时
     *
     * @param factory
     * @param classEntity
     * @param parentClass
     * @param generateClass
     * @param visitor
     */
    protected void onEndGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, PsiClass generateClass, IProcessor visitor) {
        if (visitor != null) {
            visitor.onEndGenerateClass(factory, classEntity, parentClass, generateClass);
            //使用Lombok生成Lombok注解
            if (Config.getInstant().isUseLombok()) {
                PsiDocComment docComment = generateClass.getDocComment();
                if (docComment == null && Config.getInstant().isUseComment()) {
                    JavaDocUtils.addClassComment(generateClass, classEntity, factory);
                }
                injectLombokAnnotation(factory, generateClass);
            }
        }
    }

    protected void generateField(PsiElementFactory factory, FieldEntity fieldEntity, PsiClass cls, ClassEntity classEntity) {

        if (fieldEntity.isGenerate()) {
            Try.run(new Try.TryListener() {
                @Override
                public void run() {
                    PsiField field = factory.createFieldFromText(generateFieldText(classEntity, fieldEntity, null), cls);
                    if (Config.getInstant().isUseComment()) {
                        JavaDocUtils.addFieldComment(field, fieldEntity, factory);
                    }
                    cls.add(field);
                }

                @Override
                public void runAgain() {
                    //生成自定义名字
                    fieldEntity.setFieldName(FieldHelper.generateLuckyFieldName(fieldEntity.getFieldName()));
                    cls.add(factory.createFieldFromText(generateFieldText(classEntity, fieldEntity, Constant.FIXME), cls));
                }

                @Override
                public void error() {
                    cls.addBefore(factory.createCommentFromText("// FIXME generate failure  field " + fieldEntity.getFieldName(), cls), cls.getChildren()[0]);
                }
            });
        }
    }

    /**
     * 生成字段文本
     * FIXME: 2020/2/23 如果未知字段名如_$349634Bean则设置为Map<String,Entity>
     *
     * @param classEntity
     * @param fieldEntity
     * @param fixme
     * @return
     */
    private String generateFieldText(ClassEntity classEntity, FieldEntity fieldEntity, String fixme) {
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
        //添加字段序列化注解
        if (!isNumberKeyFieldAsMap(fieldEntity)
                && (!filedName.equals(fieldEntity.getKey()) || Config.getInstant().isUseSerializedName())) {
            fieldSb.append(Config.getInstant().geFullNameAnnotation().replaceAll("\\{filed\\}", fieldEntity.getKey()));
        }
        //添加字段类型与名称
        if (Config.getInstant().isFieldPrivateMode()) {
            fieldSb.append("private ").append(fieldEntity.getFullNameType()).append(" ").append(filedName).append(";");
        } else {
            fieldSb.append("public ").append(fieldEntity.getFullNameType()).append(" ").append(filedName).append(";");
        }
        String fieldText = fieldSb.toString().concat(fixme);


        if (isNumberKeyFieldAsMap(fieldEntity)) {
            fieldText = fieldText.replace(fieldEntity.getFullNameType(),
                    "java.util.Map<String," + fieldEntity.getFullNameType() + ">");
        }
        return fieldText;
    }

    protected void injectLombokAnnotation(PsiElementFactory factory, PsiClass generateClass) {
        if (factory == null || generateClass == null) {
            return;
        }

        PsiModifierList modifierList = generateClass.getModifierList();
        if (modifierList != null) {
            //添加类注解
            PsiElement firstChild = modifierList.getFirstChild();
            PsiAnnotation[] annotations = modifierList.getAnnotations();
            Boolean isHasDataFlag = Boolean.FALSE;
            Boolean isHasNoArgsConstructoryFlag = Boolean.FALSE;
            if (annotations != null && annotations.length > 0) {
                for (PsiAnnotation annotation : annotations) {
                    if (!isHasNoArgsConstructoryFlag && annotation.getText().contains(Constant.noArgsConstructorAnnotation)) {
                        isHasNoArgsConstructoryFlag = Boolean.TRUE;
                    }

                    if (!isHasDataFlag && annotation.getText().contains(Constant.dataAnnotation)) {
                        isHasDataFlag = Boolean.TRUE;
                    }
                }
            }

            if (!isHasNoArgsConstructoryFlag) {
                PsiAnnotation annotationFromText = factory.createAnnotationFromText("@lombok.NoArgsConstructor", generateClass);
                modifierList.addBefore(annotationFromText, firstChild);
            }
            if (!isHasDataFlag) {
                PsiAnnotation annotationFromText = factory.createAnnotationFromText("@lombok.Data", generateClass);
                modifierList.addBefore(annotationFromText, firstChild);
            }
        }
    }

    /**
     * 未知字段名如_$349634Bean则修改为Map<String,_$349634Bean>
     *
     * @param fieldEntity
     * @return
     */
    private boolean isNumberKeyFieldAsMap(FieldEntity fieldEntity) {
        return Config.getInstant().isUseNumberKeyAsMap() && fieldEntity.getFullNameType().startsWith("_$");
    }

}
