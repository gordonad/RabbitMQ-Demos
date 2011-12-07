package com.chariotsolutions.rabbitmq.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.chariotsolutions.rabbitmq.domain.Mezzage;
import com.chariotsolutions.rabbitmq.service.MezzageService;

/**
 * A central place to register application converters and formatters. 
 */
@Configurable
public class ApplicationConversionServiceFactoryBean extends
		FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
	MezzageService mezzageService;

	public Converter<Mezzage, java.lang.String> getMezzageToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.chariotsolutions.rabbitmq.domain.Mezzage, java.lang.String>() {
			@Override
			public String convert(Mezzage mezzage) {
				return new StringBuilder().append(mezzage.getMessageBody())
						.toString();
			}
		};
	}

	public Converter<java.lang.Long, Mezzage> getIdToMezzageConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.chariotsolutions.rabbitmq.domain.Mezzage>() {
			@Override
			public com.chariotsolutions.rabbitmq.domain.Mezzage convert(
					java.lang.Long id) {
				return mezzageService.findMezzage(id);
			}
		};
	}

	public Converter<java.lang.String, Mezzage> getStringToMezzageConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.chariotsolutions.rabbitmq.domain.Mezzage>() {
			@Override
			public com.chariotsolutions.rabbitmq.domain.Mezzage convert(
					String id) {
				return getObject().convert(
						getObject().convert(id, java.lang.Long.class),
						Mezzage.class);
			}
		};
	}

	public void installLabelConverters(FormatterRegistry registry) {
		registry.addConverter(getMezzageToStringConverter());
		registry.addConverter(getIdToMezzageConverter());
		registry.addConverter(getStringToMezzageConverter());
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		installLabelConverters(getObject());
	}
}
