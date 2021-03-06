package net.shopec.service.impl;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import net.shopec.TemplateConfig;
import net.shopec.service.TemplateService;
import net.shopec.util.SystemUtils;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service - 模板
 * 
 */
@Service
@PropertySource("classpath:shopec.properties")
public class TemplateServiceImpl implements TemplateService {

	@Value("${template.loader_path}")
	private String templateLoaderPath;

	@Inject
	private ServletContext servletContext;

	public String read(String templateConfigId) {
		Assert.hasText(templateConfigId, "hasText");

		TemplateConfig templateConfig = SystemUtils.getTemplateConfig(templateConfigId);
		return read(templateConfig);
	}

	public String read(TemplateConfig templateConfig) {
		Assert.notNull(templateConfig, "notNull");

		try {
			String templatePath = servletContext.getRealPath(templateLoaderPath + templateConfig.getTemplatePath());
			File templateFile = new File(templatePath);
			return FileUtils.readFileToString(templateFile, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void write(String templateConfigId, String content) {
		Assert.hasText(templateConfigId, "hasText");

		TemplateConfig templateConfig = SystemUtils.getTemplateConfig(templateConfigId);
		write(templateConfig, content);
	}

	public void write(TemplateConfig templateConfig, String content) {
		Assert.notNull(templateConfig, "notNull");

		try {
			String templatePath = servletContext.getRealPath(templateLoaderPath + templateConfig.getTemplatePath());
			File templateFile = new File(templatePath);
			FileUtils.writeStringToFile(templateFile, content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}