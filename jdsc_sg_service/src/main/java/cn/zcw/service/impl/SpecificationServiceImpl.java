package cn.zcw.service.impl;

import cn.zcw.domain.TbSpecification;
import cn.zcw.domain.TbSpecificationOption;
import cn.zcw.domain.TbSpecificationOptionExample;
import cn.zcw.groupentity.Specification;
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

    /**
     * 分页方法的实现
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<TbSpecification> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
//        条件查询，传入null,表示查询所有数据,并封装到list中
        List<TbSpecification> list = tbSpecificationMapper.selectByExample(null);
//        将list封装到PageInfo类中
        PageInfo<TbSpecification> pageInfo = new PageInfo<>(list);
        return pageInfo;

    }

    /**
     * 点击修改，进行数据回显
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
//        创建组合类
        Specification specification = new Specification();
//        获取查询到的数据并存入到规格表中
        TbSpecification tbSpecification = tbSpecificationMapper.selectByPrimaryKey(id);
//        创建条件sql
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
//        通过规格表获取规格项表的外键并加入条件中
        example.createCriteria().andSpecIdEqualTo(tbSpecification.getId());
//        通过条件查询获得规格项表
        List<TbSpecificationOption> list = tbSpecificationOptionMapper.selectByExample(example);
//        将数据进行封装
        specification.setTbSpecification(tbSpecification);
        specification.setTbSpecificationOptionList(list);

        return specification;
    }

    /**
     * 保存
     * @param specification
     */
    @Override
    public void save(Specification specification) {
//        将数据存入tbSpecification中
        TbSpecification tbSpecification = specification.getTbSpecification();
//        将数据存入数据库中
        tbSpecificationMapper.insert(tbSpecification);
//        从组合类中获取规格项集合
        List<TbSpecificationOption> list = specification.getTbSpecificationOptionList();
//        遍历集合
        for (TbSpecificationOption tbSpecificationOption : list) {
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            tbSpecificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    /**
     *更新
     * @param specification
     */
    @Override
    public void update(Specification specification) {
//        从前台封装的组合类中取出规格数据
        TbSpecification tbSpecification = specification.getTbSpecification();
//        通过主键修改规格名称
        tbSpecificationMapper.updateByPrimaryKey(tbSpecification);
//        通过规格主键删除对应规格项中的外键依赖所有数据
//        定义条件sql语句
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        example.createCriteria().andSpecIdEqualTo(tbSpecification.getId());
        tbSpecificationOptionMapper.deleteByExample(example);
//        获取从前台获取的规格项集合
        List<TbSpecificationOption> list = specification.getTbSpecificationOptionList();
//        遍历集合
        for (TbSpecificationOption tbSpecificationOption : list) {
//            获取规格表中对应二级分类的id值，也就是设置SpecId,建立外键关联
            tbSpecificationOption.setSpecId(tbSpecification.getId());
//            添加规格项数据
            tbSpecificationOptionMapper.insert(tbSpecificationOption);
        }


    }

    /**
     * 删除
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
//        遍历id数据数组
        for (Long id : ids) {
//            创建条件删除语句
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            example.createCriteria().andSpecIdEqualTo(id);
//            首先删除规格项中的依赖id的数据
            tbSpecificationOptionMapper.deleteByExample(example);
//            再删除规格中id所对应的数据
            tbSpecificationMapper.deleteByPrimaryKey(id);
        }



    }

}

