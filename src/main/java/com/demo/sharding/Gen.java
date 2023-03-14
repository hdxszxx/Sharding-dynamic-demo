package com.demo.sharding;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

public class Gen {

    // 实体类生成工具
    public static void main(String[] args) {
        String pkgName = Gen.class.getPackage().getName();
//        + File.separator + pkgName.replaceAll("\\.", "/")
        String projectPath = getMyDIR();
        FastAutoGenerator.create("jdbc:mysql://xxx.xxx.xxx.xxx:3306/test?useUnicode=true&zeroDateTimeBehavior=CONVERT_TO_NULL&allowMultiQueries=true&serverTimezone=PRC&useSSL=false",
                        "xxxx", "xxxx")
                .globalConfig(builder -> {
                    builder.author("PC001") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java")
                            ; // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.demo.sharding") // 设置父包名
//                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources"  + File.separator + "mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude("test")
                            ; // 设置过滤表前缀
                    builder.entityBuilder().enableLombok();
                    builder.entityBuilder().enableTableFieldAnnotation();
                    builder.entityBuilder().enableFileOverride();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateConfig(c->{
                    c.controller("");
                    c.entity("templates/entity.java");
                })
                .execute();
    }

    public static String getMyDIR(){//获取当前类文件的绝对路径
        String jarWholePath = Gen.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            //保险起见，将路径进行decode转码
            jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
        } catch (UnsupportedEncodingException e) { System.out.println(e.toString()); }
        //获取jar包的上级目录
        String jarPath = new File(jarWholePath).getParentFile().getParentFile().getAbsolutePath();
        return jarPath;
    }
}
