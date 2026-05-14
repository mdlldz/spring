package com.example.view;

import com.example.entity.User;
import org.springframework.web.servlet.view.AbstractView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 自定义Pdf视图（示例）
 * 功能：将用户数据导出为简单的文本格式（模拟Pdf，实际项目可使用iText生成真正的PDF）
 */
public class PdfView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置响应头，模拟PDF文件下载
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=user_list.txt");

        // 获取模型中的用户数据
        List<User> userList = (List<User>) model.get("userList");

        // 写入响应流
        PrintWriter writer = response.getWriter();
        writer.println("用户列表（模拟PDF）");
        writer.println("====================");
        for (User user : userList) {
            writer.println("ID: " + user.getId() + ", 用户名: " + user.getUsername() + ", 年龄: " + user.getAge());
        }
        writer.flush();
        writer.close();
    }
}