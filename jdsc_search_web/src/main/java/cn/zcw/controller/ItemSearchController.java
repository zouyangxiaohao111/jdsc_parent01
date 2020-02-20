package cn.zcw.controller;

import cn.zcw.entity.Result;
import cn.zcw.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {

//    注入依赖
    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Result search(@RequestBody Map paramMap){
        try {
            Map resultMap =itemSearchService.search(paramMap);
            return new Result(true,"查询成功",resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"查询失败");
        }


    }
}
