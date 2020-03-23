package cn.zcw.service.impl;

import cn.zcw.domain.TbUser;
import cn.zcw.entity.Result;

public interface UserService {
    void sendCode(String phone);

    Result register(String code, TbUser tbUser);
}
