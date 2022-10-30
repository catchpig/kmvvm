package com.catchpig.ksp.compiler.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo

abstract class BaseGenerator(
    val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {

    abstract fun process(resolver: Resolver): List<KSAnnotated>

    fun warning(tag: String, msg: String) {
        logger.warn("$tag:$msg")
    }

    fun error(tag: String, msg: String) {
        logger.error("$tag:$msg")
    }
}
