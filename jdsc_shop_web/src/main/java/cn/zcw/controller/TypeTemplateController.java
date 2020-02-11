package cn.zcw.controller;

import cn.zcw.domain.TbTypeTemplate;
import cn.zcw.entity.Result;
import cn.zcw.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//RestController=@Controller + @ResponseBody
//接受json格式数据
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
//    从注册中心获取暴露的服务接口
    @Reference
    private TypeTemplateService typeTemplateService;


    /**
     * 分页方法的实现
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize ){
        try {
//            通过服务调用分页方法并存入pageInfo中
            PageInfo<TbTypeTemplate> pageInfo = typeTemplateService.findPage(pageNum,pageSize);
//            返回前端数据
            return new Result(true,"查询成功1",pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
//            返回前端数据
            return new Result(true,"查询失败");
        }

    }

    /**
     * 保存
     *
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbTypeTemplate tbTypeTemplate){
        try {
//            从注册中心调取服务
            typeTemplateService.add(tbTypeTemplate);
            return new Result(true,"操作成功1");

        } catch (Exception e) {
            e.printStackTrace();
//            返回前端数据
            return new Result(false,"操作失败");
        }

    }

    /**
     * 修改
     *
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbTypeTemplate typeTemplate) {
        try {
            typeTemplateService.update(typeTemplate);
            return new Result(true, "修改成功1");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }
    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public Result findOne(@PathVariable("id") Long id) {
        try {
//            从注册中心调取服务
            TbTypeTemplate tbTypeTemplate = typeTemplateService.findOne(id);
            return new Result(true,"操作成功1",tbTypeTemplate);
        } catch (Exception e) {
            e.printStackTrace();
//            返回前端数据
            return new Result(false,"操作失败");
        }

    }
    /**
     * 查询+分页
     *
     * @param
     * @param
     * @param
     * @return
     */
    @RequestMapping("/search/{pageNum}/{pageSize}")
    public Result search(@RequestBody TbTypeTemplate typeTemplate, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        try {
            // 分页条件查询
            PageInfo<TbTypeTemplate> page = typeTemplateService.findPage(typeTemplate, pageNum, pageSize);
            return new Result(true, "查询成功1", page.getTotal(), page.getList());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询失败");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") Long[] ids) {
        try {
            typeTemplateService.delete(ids);
            return new Result(true, "删除成功1");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
    /**
     * 获取所有
     *
     * @param
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
//            从注册中心调取服务
            List<TbTypeTemplate> list = typeTemplateService.findAll();
            return new Result(true,"操作成功1",list);
        } catch (Exception e) {
            e.printStackTrace();
//            返回前端数据
            return new Result(false,"操作失败");
        }

    }
}
