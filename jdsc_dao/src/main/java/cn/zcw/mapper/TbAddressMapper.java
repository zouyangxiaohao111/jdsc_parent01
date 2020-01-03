package cn.zcw.mapper;

import cn.zcw.domain.TbAddress;
import cn.zcw.domain.TbAddressExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生成地址类mapper接口
 */
public interface TbAddressMapper {
//    条件查询
    int countByExample(TbAddressExample example);
//    条件删除
    int deleteByExample(TbAddressExample example);
//通过主键删除
    int deleteByPrimaryKey(Long id);
//添加
    int insert(TbAddress record);
//选择添加
    int insertSelective(TbAddress record);
//条件查询
    List<TbAddress> selectByExample(TbAddressExample example);
//主键查询
    TbAddress selectByPrimaryKey(Long id);
//条件选择更新
    int updateByExampleSelective(@Param("record") TbAddress record, @Param("example") TbAddressExample example);
//条件更新
    int updateByExample(@Param("record") TbAddress record, @Param("example") TbAddressExample example);
//主键选择更新
    int updateByPrimaryKeySelective(TbAddress record);
//通过主键更新
    int updateByPrimaryKey(TbAddress record);
}