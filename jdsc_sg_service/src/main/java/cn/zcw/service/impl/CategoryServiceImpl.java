package cn.zcw.service.impl;

import cn.zcw.domain.TbContentCategory;
import cn.zcw.mapper.TbContentCategoryMapper;
import cn.zcw.service.CategoryService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<TbContentCategory> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(null);
        PageInfo<TbContentCategory> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    /**
     * 更新
     * @param tbContentCategory
     */
    @Override
    public void update(TbContentCategory tbContentCategory) {
        tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);

    }

    /**
     * 保存
     * @param tbContentCategory
     */
    @Override
    public void save(TbContentCategory tbContentCategory) {
        tbContentCategoryMapper.insert(tbContentCategory);
    }

    @Override
    public TbContentCategory findOne(Long id) {
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        return tbContentCategory;
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbContentCategoryMapper.deleteByPrimaryKey(id);
        }
    }
}
