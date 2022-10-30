package com.catchpig.compiler

import com.catchpig.annotation.FlowError
import com.catchpig.annotation.GlobalConfig
import com.catchpig.compiler.exception.KAptException
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

//@AutoService(Processor::class)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
class KotlinMvvmProcessor : BaseProcessor() {
    companion object {
        private const val TAG = "KotlinMvvmProcessor"
        private val CLASS_NAME_I_GLOBAL_CONFIG_MODEL =
            ClassName("com.catchpig.mvvm.interfaces", "IGlobalConfig")
        private val CLASS_NAME_KOTLIN_MVVM_COMPILER =
            ClassName("com.catchpig.mvvm.apt.interfaces", "GlobalCompiler")
        private val CLASS_NAME_I_OBSERVER_ERROR =
            ClassName("com.catchpig.mvvm.interfaces", "IFlowError")
        private val CLASS_NAME_LIST = ClassName("kotlin.collections", "ArrayList")
        private val CLASS_NAME_BASE_ACTIVITY =
            ClassName("com.catchpig.mvvm.base.activity", "BaseActivity")
        private val CLASS_NAME_BASE_FRAGMENT =
            ClassName("com.catchpig.mvvm.base.fragment", "BaseFragment")
        private val CLASS_NAME_LIST_OF_I_OBSERVER_ERROR =
            CLASS_NAME_LIST.parameterizedBy(CLASS_NAME_I_OBSERVER_ERROR)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        var set = HashSet<String>()
        set.add(GlobalConfig::class.java.canonicalName)
        set.add(FlowError::class.java.canonicalName)
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
                    .primaryConstructor(initConstructor(roundEnv))
                    .addProperty(initGlobalConfigProperty(element))
                    .addProperty(initFlowProperty())
                    .addFunction(getGlobalConfigFun())
                    .addFunction(onErrorFun())
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

    private fun initConstructor(roundEnv: RoundEnvironment): FunSpec {
        val observerErrorElements = roundEnv.getElementsAnnotatedWith(FlowError::class.java)
        var constructorBuilder = FunSpec.constructorBuilder()
        observerErrorElements.map {
            it as TypeElement
        }.forEach {
            constructorBuilder = constructorBuilder.addStatement("flowErrors.add(%T())", it)
        }
        return constructorBuilder.build()
    }

    private fun initFlowProperty(): PropertySpec {
        var builder = PropertySpec
            .builder("flowErrors", CLASS_NAME_LIST_OF_I_OBSERVER_ERROR)
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

    private fun onErrorFun(): FunSpec {
        var funSpecBuilder = FunSpec
            .builder("onError")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("any", ANY)
            .addParameter("t", Throwable::class)
            .addStatement("flowErrors.forEach {")
            .addStatement("  it.onError(any, t)")
            .addStatement("  if(any is %T<*>){", CLASS_NAME_BASE_ACTIVITY)
            .addStatement("     it.onBaseActivityError(any, t)")
            .addStatement("  }else if(any is %T<*>){", CLASS_NAME_BASE_FRAGMENT)
            .addStatement("     it.onBaseFragmentError(any, t)")
            .addStatement("  }")
            .addStatement("}")
        return funSpecBuilder.build()
    }


}