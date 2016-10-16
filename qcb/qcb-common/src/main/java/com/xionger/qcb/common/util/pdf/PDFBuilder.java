package com.xionger.qcb.common.util.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

/**
 * pdf创建工具类
 * @author    leo
 * @date      2016-6-20 下午5:13:30
 * @version   v1.0
 */
public class PDFBuilder {
	private static final Logger logger = LoggerFactory.getLogger(PDFBuilder.class);
	private static Configuration cfg;
	static {
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setTemplateLoader(new ClassTemplateLoader(PDFBuilder.class, "/templates"));
		cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setIncompatibleImprovements(new Version(2, 3, 23));
	}

	/**
	 * 创建pdf
	 * @param  
	 * @author leo 
	 * @throws
	 */
	public static byte[] createPDF(String name, Object obj) throws IOException, TemplateException, DocumentException {
		String html = getHtmlTemplate(name + ".html", obj);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		fontResolver.addFont("/fonts/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(data);
		data.close();
		return data.toByteArray();
	}

	public static String getHtmlTemplate(String name, Object obj) throws IOException, TemplateException {
		Template temp = cfg.getTemplate(name);
		StringBuilderWriter sbw = new StringBuilderWriter();
		temp.process(obj, sbw,ObjectWrapper.BEANS_WRAPPER);
		sbw.close();
		return sbw.getBuilder().toString();
	}
	
   public static void main(String[] args) {
		try {
			Map<String,String> obj = new HashMap<String,String>();
			obj.put("pushMoneyTerms", "54");
			byte[] data = createPDF("housefund_report", obj);
			
			File  file = new File("E:/test.pdf");
			FileUtils.writeByteArrayToFile(file, data, false);
		} catch (Exception e) {
			logger.error("" + e);
		} 
	 }
}
