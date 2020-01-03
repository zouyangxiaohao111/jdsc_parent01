package cn.zcw.controller;

import cn.zcw.domain.TbSpecification;
import cn.zcw.entity.Result;
import cn.zcw.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 规格管理模块
 *
 */
//RestController=@Controller + @ResponseBody
//接受json格式数据
@RestController
@RequestMapping("/specification")
public class SpecificationController {
//    生成代理对象，调用远程zookeeper服务对象，使用reference进行关联
    @Reference
    private SpecificationService specificationService;



    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize")int pageSize){

        try {
//            将获取的数据封装到pageinfo中
            PageInfo<TbSpecification> pageInfo = specificationService.findPage(pageNum, pageSize);
//            返回result中并将其传入到前端页面中
            return new Result(true,"查询成功",pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }


    }






}
