package com.mysiteforme.admin.config;

import com.mysiteforme.admin.freemark.*;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * todo:
 */
@Component
public class FreemarkerConfig {

    @Autowired
    private Configuration configuration;

    @Autowired
    private SystemDirective systemDirective;


    @Autowired
    private SysUserTempletModel sysUserTempletModel;


    @PostConstruct
    public void setSharedVariable() {
        //系统字典标签
        configuration.setSharedVariable("my",systemDirective);
        //获取系统用户信息
        configuration.setSharedVariable("sysuser",sysUserTempletModel);
    }
}
