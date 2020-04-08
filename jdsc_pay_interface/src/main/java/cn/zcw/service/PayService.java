package cn.zcw.service;

import java.util.Map;

public interface PayService {
    Map<String,String> createNative(String userId) throws Exception;

    Map<String,String> queryPayStatus(String orderCode) throws Exception;

    void updatePayLogStatus(String userId, String transaction_id);
}
