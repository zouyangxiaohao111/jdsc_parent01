package cn.zcw.service;

import cn.zcw.domain.TbSpecification;
import com.github.pagehelper.PageInfo;

public interface SpecificationService {


    PageInfo<TbSpecification> findPage(int pageNum, int pageSize);
}
