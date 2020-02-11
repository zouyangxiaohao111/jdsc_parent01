package cn.zcw.service;

import cn.zcw.domain.TbSeller;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SellerService {
    PageInfo<TbSeller> findPage( int pageNum, int pageSize);

    PageInfo<TbSeller> findPage(TbSeller seller, int pageNum, int pageSize);

    void delete(String[] ids);

    TbSeller findOne(String id);

    void update(TbSeller seller);

    void add(TbSeller seller);

    List<TbSeller> findAll();

    void auditing(String sellerId, String status);

    void update(TbSeller seller, String sellerId);

}
