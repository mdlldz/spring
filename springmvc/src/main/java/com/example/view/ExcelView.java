package com.example.view;

import com.example.entity.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 自定义Excel导出视图（59~66集）
 * 功能：将用户数据导出为Excel文件
 */
public class ExcelView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置响应头，告诉浏览器下载Excel文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=user_list.xlsx");

        // 获取模型中的用户数据
        List<User> userList = (List<User>) model.get("userList");

        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("用户列表");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("用户名");
        headerRow.createCell(2).setCellValue("年龄");
        headerRow.createCell(3).setCellValue("邮箱");

        // 填充数据
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getUsername());
            row.createCell(2).setCellValue(user.getAge());
            row.createCell(3).setCellValue(user.getEmail());
        }

        // 写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}