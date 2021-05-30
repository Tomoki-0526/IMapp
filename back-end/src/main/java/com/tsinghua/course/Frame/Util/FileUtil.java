package com.tsinghua.course.Frame.Util;

import org.springframework.stereotype.Component;

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
}
