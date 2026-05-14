package com.example.resolver;

import com.example.view.ExcelView;
import com.example.view.PdfView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import java.util.Locale;

/**
 * 自定义视图解析器
 * 功能：根据视图名解析为自定义视图（Excel/Pdf导出视图）
 */
public class CustomViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        // 解析Excel视图
        if ("excelView".equals(viewName)) {
            return new ExcelView();
        }
        // 解析Pdf视图
        if ("pdfView".equals(viewName)) {
            return new PdfView();
        }
        // 不支持的视图名返回null，让其他解析器处理
        return null;
    }
}