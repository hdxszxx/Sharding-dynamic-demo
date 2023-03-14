package com.demo.sharding.service.impl;

import com.demo.sharding.entity.Test;
import com.demo.sharding.mapper.TestMapper;
import com.demo.sharding.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author PC001
 * @since 2023-03-14
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
