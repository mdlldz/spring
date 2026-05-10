package com.example.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final String PATH = "D:/upload/";

    /**
     * 单文件上传
     */
    @PostMapping("/single")
    public String upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) return "请选择文件";

        String originalName = file.getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        String newName = UUID.randomUUID() + suffix;

        try {
            file.transferTo(new File(PATH + newName));
            return "上传成功：" + newName;
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
    }
}