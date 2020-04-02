package cn.zcw.service;

import cn.zcw.domain.TbAddress;

import java.util.List;

public interface AddressService {
    List<TbAddress> findAddressList(String username);


}
