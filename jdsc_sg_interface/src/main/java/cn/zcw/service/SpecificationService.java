package cn.zcw.service;

import cn.zcw.domain.TbSpecification;
import cn.zcw.groupentity.Specification;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface SpecificationService {


    PageInfo<TbSpecification> findPage(int pageNum, int pageSize);

    Specification findOne(Long id);

    void save(Specification specification);

    void  update(Specification specification);

    void delete(Long[] ids);


    List<Map<String,Object>> findSpecificationList();

}
