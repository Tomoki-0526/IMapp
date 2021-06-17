package com.tsinghua.course.Frame.Util;

import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;

/**
 * @描述 上传文件相关工具
 **/
@Component
public class FileUtil {
    /**
     * 获取文件真实名称
     * 由于浏览器的不同获取的名称可能为"c:/upload/1.png"或"1.png"
     * 最终获取的为"1.png"
     * @param filename 上传上来的文件名称
     * @return 真实名称
     */
    public static String getRealName(String filename) {
        // 获取最后一个“/”
        int index = filename.lastIndexOf("\\");
        return filename.substring(index + 1);
    }

    /**
     * 获取随机名称
     * @param realName 真实名称
     * @return uuid 随机名称
     */
    public static String getUUIDName(String realName) {
        // 获取后缀名
        int index = realName.lastIndexOf(".");
        if (index == -1) {
            return UUID.randomUUID().toString().replace("-", "").toUpperCase();
        } else {
            return UUID.randomUUID().toString().replace("-", "").toUpperCase()
                    + realName.substring(index);
        }
    }

    /**
     * 获取文件目录，可以获取256个随机目录
     * @return 随机目录
     */
    public static String getDir() {
        Random random = new Random();
        return "/" + HEX_STR.charAt(random.nextInt(16)) + "/" + HEX_STR.charAt(random.nextInt(16));
    }

    /**
     * 将FileUpload写入文件
     * @param fileUpload 上传来的文件
     * @param dir 保存到的目录
     * @return 文件存储在服务器中的绝对路径
     */
    public static String fileUploadToFile(FileUpload fileUpload, String dir) {
        if (fileUpload.isCompleted()) {
            // 获取原始文件名
            String originalFilename = fileUpload.getFilename();
            // 生成uuid名称
            String uuidFilename = getUUIDName(originalFilename);
            // 检查是否有该目录
            File dirFile = new File(dir);
            if (!dirFile.exists())
                dirFile.mkdirs();
            // 创建新的文件
            File file = new File(dir, uuidFilename);
            // 将FileUpload写入目标文件
            try {
                fileUpload.renameTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dir + uuidFilename;
        }
        return "";
    }
}
