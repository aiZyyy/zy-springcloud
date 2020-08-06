package com.itzy.commonplugin;

/**
 * @Author: ZY
 * @Date: 2019/8/1 17:58
 * @Version 1.0
 */

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LombokPlugin extends PluginAdapter {
    private String javadoc = "/**\n * Created with Mybatis Generator Plugin\n *\n * @Author ZY\n * @Date " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")) + "\n */";
    private FullyQualifiedJavaType repoAnnotation = new FullyQualifiedJavaType("org.springframework.stereotype.Repository");
    private FullyQualifiedJavaType getterAnnotation = new FullyQualifiedJavaType("lombok.Getter");
    private FullyQualifiedJavaType setterAnnotation = new FullyQualifiedJavaType("lombok.Setter");
    private FullyQualifiedJavaType builderAnnotation = new FullyQualifiedJavaType("lombok.Builder");
    private FullyQualifiedJavaType noArgsAnnotation = new FullyQualifiedJavaType("lombok.NoArgsConstructor");
    private FullyQualifiedJavaType allArgsAnnotation = new FullyQualifiedJavaType("lombok.AllArgsConstructor");

    public LombokPlugin() {
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addDataAnnotation(topLevelClass);
        return true;
    }

    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addDataAnnotation(topLevelClass);
        return true;
    }

    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addDataAnnotation(topLevelClass);
        return true;
    }

    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine(this.javadoc);
        return true;
    }

    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addRepository(interfaze);
        return true;
    }

    private void addRepository(Interface interfaze) {
        interfaze.addImportedType(this.repoAnnotation);
        interfaze.addAnnotation("@Repository");
        interfaze.addJavaDocLine(this.javadoc);
    }

    private void addDataAnnotation(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(this.setterAnnotation);
        topLevelClass.addImportedType(this.getterAnnotation);
        topLevelClass.addImportedType(this.builderAnnotation);
        topLevelClass.addImportedType(this.noArgsAnnotation);
        topLevelClass.addImportedType(this.allArgsAnnotation);
        topLevelClass.addAnnotation("@Getter");
        topLevelClass.addAnnotation("@Setter");
        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        topLevelClass.addJavaDocLine(this.javadoc);
    }
}
