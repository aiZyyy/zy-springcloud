#!/usr/bin/env bash
# 服务组名称(包名第三段)
GROUP='modules'
# 服务名称(包名第四段)
SERVER='angle'
# 指定对应文件生成目录(如果不是微服务项目 请自行修改完整包名)
TARGET_ENTITY="com.hd.rtu.${GROUP}.${SERVER}.entity"
TARGET_MAPPER="com.hd.rtu.${GROUP}.${SERVER}.mapper"

# 数据库地址
DB_HOST="172.16.22.208"
# 数据库端口
DB_PORT=3316
# 数据库名称
DB_NAME="irtuv20"
# 数据库用户名
DB_USER="root"
# 数据库密码
DB_PASS="123456789Aa"

# 表名 多个表用空格分隔 所有的表用 % 代替
TABLES='rtu_angle_current'

###############################
# 请勿修改以下部分代码 除非你看得懂 #
###############################
# 链接字符串
CONNECTION_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&amp;characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull&amp;allowMultiQueries=true&amp;serverTimezone=Asia/Shanghai"

# 代码生成根目录
TARGET_ROOT=target/mybatis-generator/${SERVER}
TARGET_JAVA="${TARGET_ROOT}/java"
TARGET_RESOURCES="${TARGET_ROOT}/resources"

# 删除已有代码
rm -rf target/mybatis-generator/${SERVER}

# 新建目录
mkdir -p ${TARGET_ROOT}/java
mkdir -p ${TARGET_ROOT}/resources

# 初始化表XML字符串
TABLE_XML=''
for TABLE in ${TABLES}; do
    # 新增表 默认添加INSERT返回主键
    TABLE_XML="${TABLE_XML}<table tableName=\"${TABLE}\" delimitIdentifiers=\"true\"><generatedKey column=\"id\" sqlStatement=\"JDBC\"/></table>"
done

# 写入XML文件
cat > generator.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="info" targetRuntime="MyBatis3">
        <property name="javaFileEncoding" value="UTF-8"/>
        <property name="autoDelimitKeywords" value="true"/>
        <property name="beginningDelimiter" value="\`"/>
        <property name="endingDelimiter" value="\`"/>
        <!--启用Lombok插件-->
        <plugin type="com.itzy.commonplugin.LombokPlugin"/>
        <commentGenerator type="com.itzy.commonplugin.CommentGenerator">
            <!--去除自动生成的注释-->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="${CONNECTION_URL}"
                        userId="${DB_USER}"
                        password="${DB_PASS}">
            <!--解决mysql驱动升级到8.0后不生成指定数据库代码的问题-->
            <property name="nullCatalogMeansCurrent" value="true" />
        </jdbcConnection>
        <javaTypeResolver>
            <!-- 设置 DECIMAL 和 NUMERIC 类型解析为 java.math.BigDecimal -->
            <property name="forceBigDecimals" value="true"/>
        </javaTypeResolver>
        <javaModelGenerator targetPackage="${TARGET_ENTITY}"
                            targetProject="${TARGET_JAVA}">
            <property name="enableSubPackages" value="false"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <sqlMapGenerator targetPackage="${TARGET_MAPPER}"
                         targetProject="${TARGET_RESOURCES}">
            <property name="enableSubPackages" value="false"/>
        </sqlMapGenerator>
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="${TARGET_MAPPER}"
                             targetProject="${TARGET_JAVA}">
            <property name="enableSubPackages" value="false"/>
        </javaClientGenerator>
        ${TABLE_XML}
    </context>
</generatorConfiguration>
EOF

# 执行Maven构建
mvn mybatis-generator:generate

sleep 1000000