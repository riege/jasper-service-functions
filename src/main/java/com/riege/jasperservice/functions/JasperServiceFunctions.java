/*
 * Copyright (c) 2018 Riege Software International. All rights reserved.
 * Use is subject to license terms.
 */

package com.riege.jasperservice.functions;

import java.util.function.BiFunction;

import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.functions.AbstractFunctionSupport;
import net.sf.jasperreports.functions.annotations.Function;
import net.sf.jasperreports.functions.annotations.FunctionCategories;
import net.sf.jasperreports.functions.annotations.FunctionParameter;
import net.sf.jasperreports.functions.annotations.FunctionParameters;

/**
 * This class provides custom functions for JasperService.
 * Currently there are only two functions: {@code LOAD_FORM} and {@code LOAD_IMAGE}.
 *
 * <p>To use this functions in a document a document producer must first set
 * handlers that should be used to load forms and images:
 *
 * <pre> {@code
 * try {
 *
 *     JasperServiceFunctions.setLoadFormHandler(
 *         formName -> myLoadFormFunction(formName));
 *     JasperServiceFunctions.setLoadImageHandler(
 *         (imageName, ext) -> myLoadImageFunction(imageName, ext));
 *
 *     // and now you call the code t
 *
 * } finally {
 *     JasperServiceFunctions.remove();
 * }
 * }</pre>
 *
 * @author <a href="mailto:golovnin@riege.com">Andrej Golovnin</a>
 */
@FunctionCategories(JasperServiceCategory.class)
public final class JasperServiceFunctions extends AbstractFunctionSupport {

    private static final ThreadLocal<java.util.function.Function<String, JasperReport>> LOAD_FORM_HOLDER =
        new InheritableThreadLocal<>();

    private static final ThreadLocal<BiFunction<String, String, JRRenderable>> LOAD_IMAGE_HOLDER =
        new InheritableThreadLocal<>();

    /**
     * Sets the form load handler for the current thread,
     *
     * @param f the function that should be used to load a form.
     */
    public static void setLoadFormHandler(java.util.function.Function<String, JasperReport> f) {
        LOAD_FORM_HOLDER.set(f);
    }

    /**
     * Sets the image load handler for the current thread,
     *
     * @param f the function that should be used to load an image.
     */
    public static void setLoadImageHandler(BiFunction<String, String, JRRenderable> f) {
        LOAD_IMAGE_HOLDER.set(f);
    }

    /**
     * Removes cached form and image handlers.
     */
    public static void remove() {
        LOAD_FORM_HOLDER.remove();
        LOAD_IMAGE_HOLDER.remove();
    }

    @Function("LOAD_FORM")
    @FunctionParameters(@FunctionParameter("formName"))
    public JasperReport LOAD_FORM(String formName) {
        java.util.function.Function<String, JasperReport> f = LOAD_FORM_HOLDER.get();
        if (f == null) {
            throw new IllegalStateException("The handler to load forms was not set");
        }
        return f.apply(formName);
    }

    @Function("LOAD_IMAGE")
    @FunctionParameters({
        @FunctionParameter("imageName"),
        @FunctionParameter("ext")
    })
    public JRRenderable LOAD_IMAGE(String imageName) {
        return LOAD_IMAGE(imageName, null);
    }

    public JRRenderable LOAD_IMAGE(String imageName, String ext) {
        BiFunction<String, String, JRRenderable> f = LOAD_IMAGE_HOLDER.get();
        if (f == null) {
            throw new IllegalStateException("The handler to load images was not set");
        }
        return f.apply(imageName, ext);
    }

}
