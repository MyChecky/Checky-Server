package com.whu.checky.util;

import com.whu.checky.domain.Record;
import com.whu.checky.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static String getFileTypePostFix(String filename){
        return filename.substring(filename.indexOf("."));
    }


}
