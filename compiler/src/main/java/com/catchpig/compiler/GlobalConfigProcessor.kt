package com.catchpig.compiler

import com.catchpig.annotation.GlobalConfig
import com.catchpig.compiler.exception.KAptException
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class GlobalConfigProcessor : BaseProcessor() {
    companion object {
        private const val TAG = "GlobalConfigProcessor"
        private val CLASS_NAME_I_GLOBAL_CONFIG_MODEL =
                ClassName("com.catchpig.mvvm.interfaces", "IGlobalConfig")
        private val CLASS_NAME_GLOBAL_CONFIG_COMPILER =
                ClassName("com.catchpig.mvvm.apt.interfaces", "GlobalConfigCompiler")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        var set = HashSet<String>()
        set.add(GlobalConfig::class.java.canonicalName)
        return set
    }

    override fun process(
            annotations: MutableSet<out TypeElement>,
            roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(GlobalConfig::class.java)
        if (elements.isNotEmpty()) {
            warning(TAG, "size:${elements.size}")
            if (elements.size > 1) {
                val message = "只能有一个类被注解GlobalConfig修饰"
                error(TAG, message)
                throw KAptException(message)
            } else {
                val element = elements.toList()[0] as TypeElement
                val className = element.simpleName.toString()
                warning(TAG, "${className}被GlobalConfig注解")
                val typeSpec = TypeSpec
                        .classBuilder("GlobalConfig_Compiler")
                        .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                        .addSuperinterface(CLASS_NAME_GLOBAL_CONFIG_COMPILER)
                        .addProperty(initObserverProperty(element))
                        .addFunction(getGlobalConfigFun())
                        .build()
                val fullPackageName = CLASS_NAME_I_GLOBAL_CONFIG_MODEL.packageName
                val fileSpec = FileSpec
                        .builder(fullPackageName, typeSpec.name!!)
                        .addType(typeSpec)
                fileSpec.build()
                        .writeTo(filer)
            }

        }
        return true
    }

    private fun initObserverProperty(element: TypeElement): PropertySpec {
        var builder = PropertySpec
                .builder("globalConfig", CLASS_NAME_I_GLOBAL_CONFIG_MODEL)
                .addModifiers(KModifier.PRIVATE)
                .initializer("%T()", element)
        return builder.build()
    }

    private fun getGlobalConfigFun(): FunSpec {
        var funSpecBuilder = FunSpec
                .builder("getGlobalConfig")
                .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
                .addStatement("return globalConfig")
                .returns(CLASS_NAME_I_GLOBAL_CONFIG_MODEL)
        return funSpecBuilder.build()
    }

}