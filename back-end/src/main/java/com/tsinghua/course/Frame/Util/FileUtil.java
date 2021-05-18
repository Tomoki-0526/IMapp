package com.tsinghua.course.Frame.Util;

//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileItemFactory;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
     *
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

//    /**
//     * 将本地文件转换为MultipartFile以便返回给前端
//     * @param filename 文件名
//     * @return MultipartFile文件
//     */
//    public static MultipartFile fileToMultipartfile(String filename) {
//        String OSName = System.getProperty("os.name");
//        String avatarPath = OSName.toLowerCase().startsWith("win") ? WINDOWS_AVATAR_PATH : LINUX_AVATAR_PATH;
//        filename = avatarPath + filename;
//
//        FileItemFactory factory = new DiskFileItemFactory(16, null);
//        String textFieldName = "textField";
//        int num = filename.lastIndexOf(".");
//        String extFile = filename.substring(num);
//        FileItem item = factory.createItem(textFieldName, "text/plain", true, "AvatarFile");
//        File newfile = new File(filename);
//        int bytesRead = 0;
//        byte[] buffer = new byte[8192];
//        try {
//            FileInputStream inputStream = new FileInputStream(newfile);
//            OutputStream outputStream = item.getOutputStream();
//            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//            outputStream.close();
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new CommonsMultipartFile(item);
//    }
}
