package cn.zcw.service.impl;

import cn.zcw.domain.TbSeller;
import cn.zcw.domain.TbSellerExample;
import cn.zcw.mapper.TbSellerMapper;
import cn.zcw.service.SellerService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @Service 开启服务，将接口暴露在内网中
 * @Transactional 开启事务
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {

//    注入mapper
    @Autowired
    private TbSellerMapper tbSellerMapper;

    /**
     * 查询+分页
     *
     * */
    @Override
    public PageInfo<TbSeller> findPage( int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
//            查询所有数据并封装到list中
        List<TbSeller> list = tbSellerMapper.selectByExample(null);
//            将list封装到pageinfo中
        PageInfo<TbSeller> pageInfo = new PageInfo<>(list);
        return pageInfo;

    }

    /**
     * 删除
     * @param ids
     */
    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            tbSellerMapper.deleteByPrimaryKey(id);
        }

    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    @Override
    public TbSeller findOne(String id) {
        TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(id);
        return tbSeller;
    }

    /**
     * 更新
     * @param seller
     */
    @Override
    public void update(TbSeller seller) {
        tbSellerMapper.updateByPrimaryKey(seller);
    }

    /**
     * 添加
     * @param seller
     */
    @Override
    public void add(TbSeller seller) {
        // 设置状态  未审批的状态
        seller.setStatus("0");
        tbSellerMapper.insert(seller);
    }

    /**
     * 查询所有数据
     * @return
     */
    @Override
    public List<TbSeller> findAll() {
        List<TbSeller> list = tbSellerMapper.selectByExample(null);
        return list;
    }

    @Override
    public void auditing(String sellerId, String status) {
        // 先通过主键查询
        TbSeller seller = tbSellerMapper.selectByPrimaryKey(sellerId);
        // 设置状态
        seller.setStatus(status);
        // 修改
        tbSellerMapper.updateByPrimaryKey(seller);
    }

    @Override
    public void update(TbSeller seller, String sellerId) {
        tbSellerMapper.updateByPrimaryKey(seller);
    }

    /**
     * 分页条件查询
     * @param seller
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageInfo<TbSeller> findPage(TbSeller seller, int pageNum, int pageSize) {
        // 设置当前页和每页条数
        PageHelper.startPage(pageNum, pageSize);
        // 拼接查询的条件
        TbSellerExample example=new TbSellerExample();
        TbSellerExample.Criteria criteria = example.createCriteria();
        // 如果商家不为空
        if(seller!=null){
            // 获取状态的值
            String status = seller.getStatus();
            // 判断
            if(status != null && !status.trim().isEmpty()){
                // 拼接查询的条件
                criteria.andStatusEqualTo(status);
            }
        }
        // 查询数据
        List<TbSeller> list = tbSellerMapper.selectByExample(example);
        // 数据的封装
        return new PageInfo<>(list);
    }
}
