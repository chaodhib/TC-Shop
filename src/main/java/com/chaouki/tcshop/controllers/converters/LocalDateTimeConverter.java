package com.chaouki.tcshop.controllers.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.TimeZone;

@FacesConverter(forClass=LocalDateTime.class)
public class LocalDateTimeConverter implements Converter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }

        if (modelValue instanceof LocalDateTime) {
            return getFormatter(context, component).format(ZonedDateTime.of((LocalDateTime) modelValue, ZoneOffset.UTC));
        } else {
            throw new ConverterException(new FacesMessage(modelValue + " is not a valid LocalDateTime"));
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }

        try {
            return ZonedDateTime.parse(submittedValue, getFormatter(context, component)).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        } catch (DateTimeParseException e) {
            throw new ConverterException(new FacesMessage(submittedValue + " is not a valid local date time"), e);
        }
    }

    private DateTimeFormatter getFormatter(FacesContext context, UIComponent component) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getPattern(component), getLocale(context, component));
        ZoneId zone = getZoneId(component);
        return (zone != null) ? formatter.withZone(zone) : formatter;
    }

    private String getPattern(UIComponent component) {
        String pattern = (String) component.getAttributes().get("pattern");

        if (pattern == null) {
            throw new IllegalArgumentException("pattern attribute is required");
        }

        return pattern;
    }

    private Locale getLocale(FacesContext context, UIComponent component) {
        Object locale = component.getAttributes().get("locale");
        return (locale instanceof Locale) ? (Locale) locale
                : (locale instanceof String) ? new Locale((String) locale)
                : context.getViewRoot().getLocale();
    }

    private ZoneId getZoneId(UIComponent component) {
        Object timeZone = component.getAttributes().get("timeZone");
        return (timeZone instanceof TimeZone) ? ((TimeZone) timeZone).toZoneId()
                : (timeZone instanceof String) ? ZoneId.of((String) timeZone)
                : ZoneId.systemDefault();
    }

}