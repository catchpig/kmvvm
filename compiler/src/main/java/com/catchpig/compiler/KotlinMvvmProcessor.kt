package com.catchpig.compiler

import com.catchpig.annotation.GlobalConfig
import com.catchpig.annotation.ObserverError
import com.catchpig.annotation.ServiceApi
import com.catchpig.compiler.exception.KAptException
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.MirroredTypesException

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
        private val CLASS_NAME_MAP = ClassName("kotlin.collections", "HashMap")
        private val CLASS_NAME_SERVICE_PARAM =
            ClassName("com.catchpig.mvvm.entity", "ServiceParam")
        private val CLASS_NAME_INTERCEPTOR =
            ClassName("okhttp3", "Interceptor")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        var set = HashSet<String>()
        set.add(GlobalConfig::class.java.canonicalName)
        set.add(ObserverError::class.java.canonicalName)
        set.add(ServiceApi::class.java.canonicalName)
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
                    .addProperty(initObserverProperty())
                    .addProperty(initServiceProperty())
                    .addFunction(getGlobalConfigFun())
                    .addFunction(onErrorFun())
                    .addFunction(getServiceParamFun())
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
        val observerErrorElements = roundEnv.getElementsAnnotatedWith(ObserverError::class.java)
        var constructorBuilder = FunSpec.constructorBuilder()
        observerErrorElements.map {
            it as TypeElement
        }.forEach {
            constructorBuilder = constructorBuilder.addStatement("observerErrors.add(%T())", it)
        }
        val serviceElements = roundEnv.getElementsAnnotatedWith(ServiceApi::class.java)
        serviceElements.map {
            it as TypeElement
        }.forEach {
            val className = it.asClassName().simpleName
            val service = it.getAnnotation(ServiceApi::class.java)
            val factory = try {
                service.factory
            } catch (e: MirroredTypeException) {
                e.typeMirror
            }

            val inteceptors = try {
                service.interceptors.toList()
            } catch (e: MirroredTypesException) {
                e.typeMirrors
            }
            constructorBuilder =
                constructorBuilder.addStatement(
                    "val list = mutableListOf<%T>()",
                    CLASS_NAME_INTERCEPTOR
                )
            inteceptors.forEach { inteceptor ->
                constructorBuilder =
                    constructorBuilder.addStatement("list.add(%T())", inteceptor)
            }
            constructorBuilder = constructorBuilder.addStatement(
                "serviceMap.put(%S, %T(%S, %T.create(), %L, %L, list))",
                className,
                CLASS_NAME_SERVICE_PARAM,
                service.baseUrl,
                factory,
                service.connectTimeout,
                service.readTimeout,
            )
        }
        return constructorBuilder.build()
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

    private fun initServiceProperty(): PropertySpec {
        var builder = PropertySpec
            .builder(
                "serviceMap",
                CLASS_NAME_MAP.parameterizedBy(
                    String::class.asClassName(),
                    CLASS_NAME_SERVICE_PARAM
                )
            )
            .addModifiers(KModifier.PRIVATE)
        builder.initializer("hashMapOf()")
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
            .addStatement("observerErrors.forEach {")
            .addStatement("  it.onError(any, t)")
            .addStatement("}")
        return funSpecBuilder.build()
    }

    private fun getServiceParamFun(): FunSpec {
        var funSpecBuilder = FunSpec
            .builder("getServiceParam")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("className", String::class)
            .addStatement("return serviceMap.get(className)!!")
            .returns(CLASS_NAME_SERVICE_PARAM)
        return funSpecBuilder.build()
    }

}