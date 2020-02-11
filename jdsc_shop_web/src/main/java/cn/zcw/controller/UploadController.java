package cn.zcw.controller;


import cn.zcw.entity.Result;
import cn.zcw.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String path;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        try {
//            获取文件服务器对象
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
//            获取文件的文件名
            String filename = file.getOriginalFilename();
//            获取后缀名
            int index = filename.lastIndexOf(".");
//            截取
            String extName = filename.substring(index + 1);
//            上传到文件服务器并返回数据
            String s = client.uploadFile(file.getBytes(), extName);
//            拼接
            String resultStr = path+s;

            return new Result(true,"上传成功",resultStr);


        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"上传失败");
        }


    }


}
