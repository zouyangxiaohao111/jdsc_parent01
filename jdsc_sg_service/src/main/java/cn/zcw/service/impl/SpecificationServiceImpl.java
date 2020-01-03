package cn.zcw.service.impl;

import cn.zcw.domain.TbSpecification;
import cn.zcw.mapper.TbSpecificationMapper;
import cn.zcw.mapper.TbSpecificationOptionMapper;
import cn.zcw.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 规格服务接口实现类
 *
 * return @Service
 */

//发布服务，使其暴露在内网中
@Service
//开启事务管理器
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
//    注入mapper对象
    @Autowired
    private TbSpecificationMapper tbSpecificationMapper;
    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;


    //分页方法的实现
    @Override
    public PageInfo<TbSpecification> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
//        条件查询，传入null,表示查询所有数据,并封装到list中
        List<TbSpecification> list = tbSpecificationMapper.selectByExample(null);
//        将list封装到PageInfo类中
        PageInfo<TbSpecification> pageInfo = new PageInfo<>(list);
        return pageInfo;

    }
}
