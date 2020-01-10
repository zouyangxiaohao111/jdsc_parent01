package cn.zcw.controller;

import cn.zcw.domain.Brand;
import cn.zcw.entity.Result;
import cn.zcw.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//RestController=@Controller + @ResponseBody
//接受json格式数据
@RestController
@RequestMapping("/brand")

/**
 * 品牌模块
 *
 * 消费者
 */
public class BrandController {

//    生成代理对象,存放的service远程调用的地址
    @Reference
    private BrandService brandService;

    /**
     *查询所有品牌信息
     */
    @RequestMapping("/findAll")
    public Result findAll(){

//        底层为远程调用
        List<Brand> brandList = brandService.findAll();
        return new Result(true,"查询成功",brandList);

    }
    /**
     * 进行分页查询,成功则返回正确的信息,失败则返回错误信息并打印异常
     * 查询分页信息
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum")int pageNum, @PathVariable("pageSize")int pageSize ){
        try {
//      通过service层 通过ioc注入brandService实现findPage分页操作
            PageInfo<Brand> pageInfo = brandService.findPage(pageNum, pageSize);
//            返回成功的结果
            return new Result(true,"查询成功",pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e){
//            打印失败的异常
            e.printStackTrace();
//            返回失败的结果
            return new Result(false,"查询失败");
        }
    }

    /**
     * 保存
     * @param brand
     * @return
     */
    @RequestMapping("/save")
        public Result save(@RequestBody Brand brand){

            try {
//                通过注入的方式去调用保存方法
                brandService.save(brand);
//                返回前端页面，形式固定，必须传递result
                return new Result(true,"操作成功");
            }catch (Exception e){
                e.printStackTrace();
//                返回前端页面，形式固定，必须传递result
                return new Result(true,"操作失败");
            }
        }
    /**
     * 点击修改时回显数据
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public Result findOne(@PathVariable("id") Long id){

        try {
//                通过注入的方式去调用修改方法
            Brand brand = brandService.findOne(id);
//                返回前端页面，形式固定，必须传递result
            return new Result(true,"操作成功",brand);
        }catch (Exception e){
            e.printStackTrace();
//                返回前端页面，形式固定，必须传递result
            return new Result(true,"操作失败");
        }
    }
    /**
     * 修改
     * @param
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Brand brand){

        try {
//                通过注入的方式去调用修改方法
                  brandService.update(brand);
//                返回前端页面，形式固定，必须传递result
            return new Result(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
//                返回前端页面，形式固定，必须传递result
            return new Result(true,"操作失败");
        }
    }
    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") Long [] ids){

        try {
//                通过注入的方式去调用删除方法
            brandService.delete(ids);
//                返回前端页面，形式固定，必须传递result
            return new Result(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
//                返回前端页面，形式固定，必须传递result
            return new Result(true,"操作失败");
        }
    }
    @RequestMapping("/findBrandList")
    public Result findBrandList(){
        try {
            List<Map<String,Object>> mapList = brandService.findBrandList();
            return new Result(true,"操作成功",mapList);
        } catch (Exception e) {

            return new Result(false,"操作失败");
        }

    }

}
