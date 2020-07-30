package com.xanarry.onlinejudge.controller;

import com.xanarry.onlinejudge.OjConfiguration;
import com.xanarry.onlinejudge.utils.Tools;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by xanarry on 18-1-4.
 */

@Controller
public class imageIOController {
    @Autowired
    private OjConfiguration configuration;

    @PostMapping(value = "/img/upload")
    private void uploadImage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part imagePart = request.getPart("upload");
        // CKEditor提交的很重要的一个参数
        String callback = request.getParameter("CKEditorFuncNum");

        String contentType = imagePart.getContentType();

        String jsPattern = "<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(%s, '%s', '')</script>";
        PrintWriter printWriter = response.getWriter();

        if (contentType.startsWith("image")) {

            String sbName = imagePart.getSubmittedFileName();
            String suffix = sbName.substring(sbName.lastIndexOf(".")); // 文件扩展名
            System.out.println("suffix: " + suffix);

            String fileName = java.util.UUID.randomUUID().toString() + suffix; // 采用时间+UUID的方式随即命名
            //图片上传路径
            String uploadPath = configuration.getImageHome() + "/" + fileName;
            //图片的浏览器访问路径
            String webPath = "/img/" + fileName;


            Tools.saveFile(imagePart.getInputStream(), uploadPath);

            System.out.println("webpath:" + webPath);
            System.out.println("uploadpath:" + uploadPath);

            String js = String.format(jsPattern, callback, webPath);
            printWriter.write(js);
        } else {
            String js = String.format(jsPattern, callback, "文件非图片类型");
            printWriter.write(js);
        }
        printWriter.close();
    }


    @GetMapping(value = "/img/{imageName}")
    public void getImage(@PathVariable("imageName") String imageName,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        File imageFile = new File(configuration.getImageHome() + "/" + imageName);
        OutputStream out = response.getOutputStream();
        response.setContentType("image/*"); //如果设置这个，图片将以文件的形式被下载
        response.setContentLength((int) imageFile.length());
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        FileUtils.copyFile(imageFile, out);
        out.flush();
    }
}
