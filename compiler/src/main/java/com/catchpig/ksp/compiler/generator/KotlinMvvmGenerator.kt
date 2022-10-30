package com.catchpig.ksp.compiler.generator

import com.catchpig.annotation.FlowError
import com.catchpig.annotation.GlobalConfig
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class KotlinMvvmGenerator(
    codeGenerator: CodeGenerator,
    logger: KSPLogger
) : BaseGenerator(codeGenerator, logger) {
    companion object {
        private const val TAG = "KotlinMvvmGenerator"
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

    private lateinit var flowErrorsClassDeclaration: List<KSClassDeclaration>

    override fun process(resolver: Resolver): List<KSAnnotated> {
        flowErrorsClassDeclaration =
            resolver.getSymbolsWithAnnotation(FlowError::class.qualifiedName!!)
                .filterIsInstance<KSClassDeclaration>().filter {
                it.validate()
            }.toList()
        val symbols = resolver.getSymbolsWithAnnotation(GlobalConfig::class.qualifiedName!!)
        val list = symbols.filterIsInstance<KSClassDeclaration>().filter {
            it.validate()
        }.toList()
        if (list.isNotEmpty()) {
            if (list.size > 1) {
                error(TAG, "只能有一个类被注解GlobalConfig修饰")
            }else{
                generate(list[0])
            }
        }
        return emptyList()
    }

    private fun generate(ksClassDeclaration: KSClassDeclaration) {
        val className = ksClassDeclaration.toClassName().simpleName
        warning(TAG, "${className}被GlobalConfig注解")
        val typeSpec = TypeSpec
            .classBuilder("Global_Compiler")
            .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
            .addSuperinterface(CLASS_NAME_KOTLIN_MVVM_COMPILER)
            .primaryConstructor(initConstructor())
            .addProperty(initGlobalConfigProperty(ksClassDeclaration))
            .addProperty(initFlowProperty())
            .addFunction(getGlobalConfigFun())
            .addFunction(onErrorFun())
            .build()
        val fullPackageName = CLASS_NAME_KOTLIN_MVVM_COMPILER.packageName
        var fileSpecBuilder = FileSpec
            .builder(fullPackageName, typeSpec.name!!)
            .addType(typeSpec)
        fileSpecBuilder.build()
            .writeTo(codeGenerator, false)
    }

    private fun initConstructor(): FunSpec {
        var constructorBuilder = FunSpec.constructorBuilder()
        flowErrorsClassDeclaration.forEach {
            constructorBuilder =
                constructorBuilder.addStatement("flowErrors.add(%T())", it.toClassName())
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


    private fun initGlobalConfigProperty(ksClassDeclaration: KSClassDeclaration): PropertySpec {
        var builder = PropertySpec
            .builder("globalConfig", CLASS_NAME_I_GLOBAL_CONFIG_MODEL)
            .addModifiers(KModifier.PRIVATE)
            .initializer("%T()", ksClassDeclaration.toClassName())
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