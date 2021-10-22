package com.catchpig.compiler

import com.catchpig.annotation.GlobalConfig
import com.catchpig.annotation.ObserverError
import com.catchpig.compiler.exception.KAptException
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class KotlinMvvmProcessor : BaseProcessor() {
    companion object {
        private const val TAG = "KotlinMvvmProcessor"
        private val CLASS_NAME_I_GLOBAL_CONFIG_MODEL =
            ClassName("com.catchpig.mvvm.interfaces", "IGlobalConfig")
        private val CLASS_NAME_KOTLIN_MVVM_COMPILER =
            ClassName("com.catchpig.mvvm.apt.interfaces", "GlobalCompiler")
        private val CLASS_NAME_I_OBSERVER_ERROR =
            ClassName("com.catchpig.mvvm.interfaces", "IObserverError")
        private val CLASS_NAME_LIST = ClassName("kotlin.collections", "ArrayList")
        private val CLASS_NAME_LIST_OF_I_OBSERVER_ERROR =
            CLASS_NAME_LIST.parameterizedBy(CLASS_NAME_I_OBSERVER_ERROR)
        private val CLASS_NAME_I_BASE_VIEW_MODEL =
            ClassName("com.catchpig.mvvm.base.viewmodel", "IBaseViewModel")
        private val CLASS_NAME_VIEW_MODEL_COMPILER =
            ClassName("com.catchpig.mvvm.apt.interfaces", "ViewModelCompiler")
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
                    .classBuilder("Global_Compiler")
                    .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                    .addSuperinterface(CLASS_NAME_KOTLIN_MVVM_COMPILER)
                    .addProperty(initGlobalConfigProperty(element))
                    .addProperty(initObserverProperty())
                    .addFunction(getGlobalConfigFun())
                    .addFunction(onErrorFun(roundEnv))
                    .build()
                val fullPackageName = CLASS_NAME_KOTLIN_MVVM_COMPILER.packageName
                var fileSpecBuilder = FileSpec
                    .builder(fullPackageName, typeSpec.name!!)
                    .addType(typeSpec)
                fileSpecBuilder.build()
                    .writeTo(filer)
            }
        }
        return true
    }

    private fun initObserverProperty(): PropertySpec {
        var builder = PropertySpec
            .builder("observerErrors", CLASS_NAME_LIST_OF_I_OBSERVER_ERROR)
            .addModifiers(KModifier.PRIVATE)
        builder.initializer("arrayListOf()")
        return builder.build()
    }


    private fun initGlobalConfigProperty(element: TypeElement): PropertySpec {
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

    private fun onErrorFun(roundEnv: RoundEnvironment): FunSpec {
        val elements = roundEnv.getElementsAnnotatedWith(ObserverError::class.java)
        var funSpecBuilder = FunSpec
            .builder("onError")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("iBaseViewModel", CLASS_NAME_I_BASE_VIEW_MODEL)
            .addParameter("t", Throwable::class)
            .addStatement("if(observerErrors.isEmpty()){")
        elements.map {
            it as TypeElement
        }.forEach {
            funSpecBuilder = funSpecBuilder.addStatement("  observerErrors.add(%T())", it)
        }
        funSpecBuilder = funSpecBuilder.addStatement("}")
            .addStatement("observerErrors.forEach {")
            .addStatement("  it.onError(iBaseViewModel, t)")
            .addStatement("}")
        return funSpecBuilder.build()
    }

}