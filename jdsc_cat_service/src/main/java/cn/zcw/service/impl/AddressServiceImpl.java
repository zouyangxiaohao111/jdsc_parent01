package cn.zcw.service.impl;

import cn.zcw.domain.TbAddress;
import cn.zcw.domain.TbAddressExample;
import cn.zcw.mapper.TbAddressMapper;
import cn.zcw.service.AddressService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    /**
     * mapper注入
     */
    @Autowired
    private TbAddressMapper tbAddressMapper;


    /**
     * 查询用户的收货地址
     * @param username
     * @return
     */
    @Override
    public List<TbAddress> findAddressList(String username) {
//        创建自定义查询语句
        TbAddressExample example = new TbAddressExample();
        example.createCriteria().andUserIdEqualTo(username);
//         查询
        List<TbAddress> addressList = tbAddressMapper.selectByExample(example);
        if (addressList.size() == 0){
            addressList = null;
        }
        return addressList;
    }
}
