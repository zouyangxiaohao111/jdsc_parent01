package cn.zcw.controller;

import cn.zcw.domain.TbSpecification;
import cn.zcw.entity.Result;
import cn.zcw.groupentity.Specification;
import cn.zcw.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


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

    /**
     * 保存
     * @param specification
     * @return
     */
    @RequestMapping("/save")
    public Result save(@RequestBody Specification specification){
        try {
            specificationService.save(specification);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }



    }
    /**
     * 更新
     * @param specification
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Specification specification){
        try {
//            通过bean注入调用接口层update方法
            specificationService.update(specification);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }



    }

    /**
     * 点击前端页面中修改按钮，数据回显
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public Result findOne(@PathVariable("id") Long id){
        try {
            Specification specification = specificationService.findOne(id);
            return  new Result(true,"操作成功",specification);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }

    }
    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") Long [] ids){
        try {
//            调用interface层
            specificationService.delete(ids);
            return  new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }

    }
    @RequestMapping("/findSpecificationList")
    public Result findSpecificationList(){
        try {
//            调用interface层
             List<Map<String, Object>> mapList;
            mapList = specificationService.findSpecificationList();

            return  new Result(true,"操作成功",mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }


}
