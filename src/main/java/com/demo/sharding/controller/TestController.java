package com.demo.sharding.controller;

import cn.hutool.core.date.SystemClock;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.sharding.constants.DBConstants;
import com.demo.sharding.entity.Test;
import com.demo.sharding.service.ITestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @date 2023/3/14 17:32
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Resource
    ITestService iTestService;

    @RequestMapping(value = "insert",method = RequestMethod.POST)
    @DS(DBConstants.SHARDING) // 切换成 shardingsphere 数据源
    public Boolean insert(Test test){
        if(test == null){
            log.info("关键参数未传入");
            return false;
        }
        test.setCreateTime(new Date(SystemClock.now()));
        test.setLastUpdateTime(new Date(SystemClock.now()));
        boolean save = iTestService.save(test);
        return save;
    }

    @RequestMapping(value ="update",method = RequestMethod.POST)
    @DS(DBConstants.SHARDING) // 切换成 shardingsphere 数据源
    public Boolean update(Test test){
        if(test == null && test.getId() == null){
            log.info("关键参数未传入");
            return false;
        }
        boolean save = iTestService.updateById(test);
        return save;
    }

    @RequestMapping(value ="delete",method = RequestMethod.POST)
    @DS(DBConstants.SHARDING) // 切换成 主库 数据源
    public Boolean delete(Test test){
        if(test == null && test.getId() == null){
            log.info("关键参数未传入");
            return false;
        }
        boolean save = iTestService.removeById(test.getId());
        return save;
    }

    @RequestMapping(value ="selectList",method = RequestMethod.POST)
    @DS(DBConstants.SHARDING) // 切换成 从库 数据源
    public String selectList(Test test){
        LambdaQueryWrapper<Test> testLambdaQueryWrapper = new LambdaQueryWrapper<>();
        testLambdaQueryWrapper.like(Test::getContent, test.getContent());
        List<Test> list = iTestService.list(testLambdaQueryWrapper);
        return JSONUtil.toJsonStr(list);
    }

}
