package com.catchpig.compiler

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/**
 * @author catchpig
 * @date 2019/10/29 00:29
 */
abstract class BaseProcessor : AbstractProcessor() {
    protected lateinit var elementUtils: Elements
    private lateinit var messager: Messager
    protected lateinit var filer: Filer

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        elementUtils = processingEnv.elementUtils
        messager = processingEnv.messager
        filer = processingEnv.filer
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    protected fun warning(tag: String, msg: String) {
        messager.printMessage(Diagnostic.Kind.WARNING, "$tag:$msg")
    }

    protected fun error(tag: String, msg: String) {
        messager.printMessage(Diagnostic.Kind.ERROR, "$tag:$msg")
    }
}