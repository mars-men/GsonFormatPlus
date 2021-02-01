package com.foxsteps.gsonformat.ui;

import com.foxsteps.gsonformat.config.Config;
import com.foxsteps.gsonformat.config.Constant;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.VariableKind;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.awt.event.*;

public class SettingDialog extends JFrame {

    private JPanel contentPane;

    private JButton objectButton;
    private JButton object1Button;
    private JButton arrayButton;
    private JButton array1Button;
    private JTextField suffixEdit;
    private JCheckBox objectFromDataCB;
    private JCheckBox objectFromData1CB;
    private JCheckBox arrayFromDataCB;
    private JCheckBox arrayFromData1CB;
    private JCheckBox reuseEntityCB;
    private JButton cancelButton;
    private JButton okButton;
    private JTextField filedPrefixTF;
    private JCheckBox filedPrefixCB;
    private JTextField annotationTF;
    private JCheckBox virgoModelCB;
    //private JCheckBox generateCommentsCB;
    private JCheckBox useWrapperClassCB;
    private JCheckBox useCommentCB;
    private JCheckBox splitGenerateCB;

    private JRadioButton fieldPublicRadioButton;
    private JRadioButton fieldPrivateRadioButton;
    private JCheckBox useSerializedNameCB;
    private JCheckBox useLombokCB;
    private JCheckBox useNumberKeyAsMapCB;


    /**
     * 转换类库
     */
    private JRadioButton gsonRB;
    private JRadioButton jacksonRB;
    private JRadioButton fastJsonRB;
    private JRadioButton otherRB;
    private JRadioButton loganSquareRB;
    private JRadioButton autoValueRB;
    private JRadioButton lombokRB;


    //注解字符串
    private String annotaionStr;

    public SettingDialog(Project project) {
        setContentPane(contentPane);
//        setModal(true);
        getRootPane().setDefaultButton(okButton);
        this.setAlwaysOnTop(true);
        setTitle("Setting");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //窗体关闭
                onCancel();
            }
        });
        //内容面板
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //ESC键关闭
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        //字段私有模型
        if (Config.getInstant().isFieldPrivateMode()) {
            fieldPrivateRadioButton.setSelected(true);
        } else {
            fieldPublicRadioButton.setSelected(true);
        }
        //处女模式CheckBox
        virgoModelCB.setSelected(Config.getInstant().isVirgoMode());
        //生成注释
        //generateCommentsCB.setSelected(Config.getInstant().isGenerateComments());
        //字段前缀checkbox
        filedPrefixCB.setSelected(Config.getInstant().isUseFieldNamePrefix());
        filedPrefixTF.setEnabled(Config.getInstant().isUseFieldNamePrefix());
        //序列化名checkbox
        useSerializedNameCB.setSelected(Config.getInstant().isUseSerializedName());
        //转换数据CheckBox
        objectFromDataCB.setSelected(Config.getInstant().isObjectFromData());
        objectFromData1CB.setSelected(Config.getInstant().isObjectFromData1());
        arrayFromDataCB.setSelected(Config.getInstant().isArrayFromData());
        arrayFromData1CB.setSelected(Config.getInstant().isArrayFromData1());
        //重用实体CheckBox
        reuseEntityCB.setSelected(Config.getInstant().isReuseEntity());
        objectButton.setEnabled(objectFromDataCB.isSelected());
        object1Button.setEnabled(objectFromData1CB.isSelected());
        arrayButton.setEnabled(arrayFromDataCB.isSelected());
        array1Button.setEnabled(arrayFromData1CB.isSelected());
        //实体后缀
        suffixEdit.setText(Config.getInstant().getSuffixStr());
        //分割生成模式
        splitGenerateCB.setSelected(Config.getInstant().isSplitGenerate());
        //使用包装类checkbox
        useWrapperClassCB.setSelected(Config.getInstant().isUseWrapperClass());
        //增加字段注释checkBox
        useCommentCB.setSelected(Config.getInstant().isUseComment());
        //使用LombokCheckbox
        useLombokCB.setSelected(Config.getInstant().isUseLombok());
        //使用数字key作为key
        useNumberKeyAsMapCB.setSelected(Config.getInstant().isUseNumberKeyAsMap());

        /**
         * ==========
         * 按钮监听器
         * ==========
         */
        objectFromDataCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                objectButton.setEnabled(objectFromDataCB.isSelected());
            }
        });
        objectFromData1CB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                object1Button.setEnabled(objectFromData1CB.isSelected());
            }
        });
        arrayFromDataCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                arrayButton.setEnabled(arrayFromDataCB.isSelected());
            }
        });
        arrayFromData1CB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                array1Button.setEnabled(arrayFromData1CB.isSelected());
            }
        });
        //字段前缀checkbox
        filedPrefixCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                filedPrefixTF.setEnabled(filedPrefixCB.isSelected());
            }
        });
        String filedPrefix = null;
        filedPrefix = Config.getInstant().getFiledNamePreFixStr();
        if (TextUtils.isEmpty(filedPrefix)) {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
            filedPrefix = styleManager.getPrefixByVariableKind(VariableKind.FIELD);
        }
        filedPrefixTF.setText(filedPrefix);

        //Convert Library
        gsonRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (gsonRB.isSelected()) {
                    annotationTF.setText(Constant.gsonAnnotation);
                }
                objectFromDataCB.setEnabled(true);
                objectFromData1CB.setEnabled(true);
                arrayFromDataCB.setEnabled(true);
                arrayFromData1CB.setEnabled(true);
                annotationTF.setEnabled(false);
            }
        });
        jacksonRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (jacksonRB.isSelected()) {
                    annotationTF.setText(Constant.jacksonAnnotation);
                }
                annotationTF.setEnabled(false);
                annotationTF.setEnabled(false);
                objectFromDataCB.setEnabled(false);
                objectFromDataCB.setSelected(false);
                objectFromData1CB.setEnabled(false);
                objectFromData1CB.setSelected(false);
                arrayFromDataCB.setEnabled(false);
                arrayFromDataCB.setSelected(false);
                arrayFromData1CB.setEnabled(false);
                arrayFromData1CB.setSelected(false);
                objectButton.setEnabled(false);
                object1Button.setEnabled(false);
                arrayButton.setEnabled(false);
                array1Button.setEnabled(false);
            }
        });
        fastJsonRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fastJsonRB.isSelected()) {
                    annotationTF.setText(Constant.fastAnnotation);
                }
                annotationTF.setEnabled(false);
                objectFromDataCB.setEnabled(false);
                objectFromDataCB.setSelected(false);
                objectFromData1CB.setEnabled(false);
                objectFromData1CB.setSelected(false);
                arrayFromDataCB.setEnabled(false);
                arrayFromDataCB.setSelected(false);
                arrayFromData1CB.setEnabled(false);
                arrayFromData1CB.setSelected(false);
                objectButton.setEnabled(false);
                object1Button.setEnabled(false);
                arrayButton.setEnabled(false);
                array1Button.setEnabled(false);
            }
        });
        loganSquareRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (loganSquareRB.isSelected()) {
                    annotationTF.setText(Constant.loganSquareAnnotation);
                }
                annotationTF.setEnabled(otherRB.isSelected());
                objectFromDataCB.setEnabled(false);
                objectFromDataCB.setSelected(false);
                objectFromData1CB.setEnabled(false);
                objectFromData1CB.setSelected(false);
                arrayFromDataCB.setEnabled(false);
                arrayFromDataCB.setSelected(false);
                arrayFromData1CB.setEnabled(false);
                arrayFromData1CB.setSelected(false);
                objectButton.setEnabled(false);
                object1Button.setEnabled(false);
                arrayButton.setEnabled(false);
                array1Button.setEnabled(false);
            }
        });
        autoValueRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (autoValueRB.isSelected()) {
                    annotationTF.setText(Constant.autoValueAnnotation);
                }
            }
        });
        lombokRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (lombokRB.isSelected()) {
                    annotationTF.setText(Constant.lombokAnnotation);
                }
                annotationTF.setEnabled(otherRB.isSelected());
                objectFromDataCB.setEnabled(false);
                objectFromDataCB.setSelected(false);
                objectFromData1CB.setEnabled(false);
                objectFromData1CB.setSelected(false);
                arrayFromDataCB.setEnabled(false);
                arrayFromDataCB.setSelected(false);
                arrayFromData1CB.setEnabled(false);
                arrayFromData1CB.setSelected(false);
                objectButton.setEnabled(false);
                object1Button.setEnabled(false);
                arrayButton.setEnabled(false);
                array1Button.setEnabled(false);
            }
        });
        otherRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                annotationTF.setText("@{filed}");
                annotationTF.setEnabled(otherRB.isSelected());
                objectFromDataCB.setEnabled(false);
                objectFromDataCB.setSelected(false);
                objectFromData1CB.setEnabled(false);
                objectFromData1CB.setSelected(false);
                arrayFromDataCB.setEnabled(false);
                arrayFromDataCB.setSelected(false);
                arrayFromData1CB.setEnabled(false);
                arrayFromData1CB.setSelected(false);
                objectButton.setEnabled(false);
                object1Button.setEnabled(false);
                arrayButton.setEnabled(false);
                array1Button.setEnabled(false);
            }
        });

        annotaionStr = Config.getInstant().getAnnotationStr();
        if (annotaionStr.equals(Constant.gsonAnnotation)) {
            gsonRB.setSelected(true);
            annotationTF.setEnabled(false);
        } else if (annotaionStr.equals(Constant.fastAnnotation)) {
            fastJsonRB.setSelected(true);
            annotationTF.setEnabled(false);
        } else if (annotaionStr.equals(Constant.jacksonAnnotation)) {
            jacksonRB.setSelected(true);
            annotationTF.setEnabled(false);
        } else if (annotaionStr.equals(Constant.loganSquareAnnotation)) {
            loganSquareRB.setSelected(true);
            annotationTF.setEnabled(false);
        } else if (annotaionStr.equals(Constant.autoValueAnnotation)) {
            autoValueRB.setSelected(true);
            annotationTF.setEnabled(false);
        } else {
            otherRB.setSelected(true);
            annotationTF.setEnabled(true);
        }
        annotationTF.setText(annotaionStr);
        objectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EditDialog editDialog = new EditDialog(EditDialog.Type.OBJECT_FROM_DATA);
                editDialog.setSize(600, 360);
                editDialog.setLocationRelativeTo(null);
                editDialog.setResizable(false);
                editDialog.setVisible(true);
            }
        });
        object1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EditDialog editDialog = new EditDialog(EditDialog.Type.OBJECT_FROM_DATA1);
                editDialog.setSize(600, 360);
                editDialog.setLocationRelativeTo(null);
                editDialog.setResizable(false);
                editDialog.setVisible(true);
            }
        });
        arrayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EditDialog editDialog = new EditDialog(EditDialog.Type.ARRAY_FROM_DATA);
                editDialog.setSize(600, 600);
                editDialog.setLocationRelativeTo(null);
                editDialog.setResizable(false);
                editDialog.setVisible(true);
            }
        });
        array1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EditDialog editDialog = new EditDialog(EditDialog.Type.ARRAY_FROM_DATA1);
                editDialog.setSize(600, 600);
                editDialog.setLocationRelativeTo(null);
                editDialog.setResizable(false);
                editDialog.setVisible(true);
            }
        });
    }


    private void onOK() {

        Config.getInstant().setFieldPrivateMode(fieldPrivateRadioButton.isSelected());
        Config.getInstant().setUseSerializedName(useSerializedNameCB.isSelected());
        Config.getInstant().setArrayFromData(arrayFromDataCB.isSelected());
        Config.getInstant().setArrayFromData1(arrayFromData1CB.isSelected());
        Config.getInstant().setObjectFromData(objectFromDataCB.isSelected());
        Config.getInstant().setObjectFromData1(objectFromData1CB.isSelected());
        Config.getInstant().setReuseEntity(reuseEntityCB.isSelected());
        Config.getInstant().setSuffixStr(suffixEdit.getText());
        Config.getInstant().setVirgoMode(virgoModelCB.isSelected());
        //Config.getInstant().setGenerateComments(generateCommentsCB.isSelected());
        Config.getInstant().setFiledNamePreFixStr(filedPrefixTF.getText());
        Config.getInstant().setAnnotationStr(annotationTF.getText());
        Config.getInstant().setUseFieldNamePrefix(filedPrefixCB.isSelected());
        Config.getInstant().setSplitGenerate(splitGenerateCB.isSelected());
        Config.getInstant().setUseWrapperClass(useWrapperClassCB.isSelected());
        Config.getInstant().setUseComment(useCommentCB.isSelected());
        Config.getInstant().setUseLombok(useLombokCB.isSelected());
        Config.getInstant().setUseNumberKeyAsMap(useNumberKeyAsMapCB.isSelected());
        Config.getInstant().save();

        dispose();
    }

    private void createUIComponents() {
    }


    private void onCancel() {
        dispose();
    }


}
