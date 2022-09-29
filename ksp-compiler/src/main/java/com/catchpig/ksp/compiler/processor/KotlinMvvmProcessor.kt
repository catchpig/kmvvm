package com.catchpig.ksp.compiler.processor

import com.catchpig.annotation.ServiceApi
import com.catchpig.annotation.interfaces.SerializationConverter
import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import java.lang.reflect.Type
import java.lang.reflect.UndeclaredThrowableException
import java.util.stream.Collectors.toList
import javax.lang.model.type.MirroredTypeException

class KotlinMvvmProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    companion object {
        private const val TAG = "KotlinMvvmProcessor"
        private val CLASS_NAME_SERVICE_API_COMPILER =
            ClassName("com.catchpig.mvvm.apt.interfaces", "ServiceApiCompiler")
        private val CLASS_NAME_MAP = ClassName("kotlin.collections", "HashMap")
        private val CLASS_NAME_SERVICE_PARAM = ClassName("com.catchpig.mvvm.entity", "ServiceParam")
        private val CLASS_NAME_INTERCEPTOR = ClassName("okhttp3", "Interceptor")
        private val CLASS_NAME_BASE_RESPONSE_BODY_CONVERTER =
            ClassName("com.catchpig.mvvm.network.converter", "BaseResponseBodyConverter")
        private val CLASS_NAME_GSON_RESPONSE_BODY_CONVERTER =
            ClassName("com.catchpig.mvvm.network.converter", "SerializationResponseBodyConverter")
        private val CLASS_NAME_CONVERTER = ClassName("retrofit2", "Converter")
        private val CLASS_NAME_RESPONSE_BODY = ClassName("okhttp3", "ResponseBody")
        private val CLASS_NAME_CONVERTER_OF_RESPONSE_BODY_AND_ANY =
            CLASS_NAME_CONVERTER.parameterizedBy(CLASS_NAME_RESPONSE_BODY, ANY)
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("$TAG")
        val symbols = resolver.getSymbolsWithAnnotation(ServiceApi::class.qualifiedName!!)
        val list = symbols.filterIsInstance<KSClassDeclaration>().filter {
            it.validate()
        }.toList()
        if (list.isNotEmpty()) {
            generate(list)
        }
        val sysmbolsList = symbols.filter { !it.validate() }.toList()
        logger.warn("${sysmbolsList.size}")
        return sysmbolsList
    }

    @OptIn(KotlinPoetKspPreview::class)
    private fun generate(list: List<KSClassDeclaration>) {
        val typeSpec = TypeSpec
            .classBuilder("ServiceApi_Compiler")
            .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
            .addSuperinterface(CLASS_NAME_SERVICE_API_COMPILER)
            .primaryConstructor(initConstructor(list))
            .addProperty(initServiceProperty())
            .addFunction(getServiceParamFun())
            .addFunction(getResponseBodyConverterFun(list))
            .build()
        val fullPackageName = CLASS_NAME_SERVICE_API_COMPILER.packageName
        var fileSpecBuilder = FileSpec
            .builder(fullPackageName, typeSpec.name!!)
            .addType(typeSpec)
        fileSpecBuilder.build()
            .writeTo(codeGenerator, false)
    }

    @OptIn(KotlinPoetKspPreview::class, KspExperimental::class)
    private fun initConstructor(list: List<KSClassDeclaration>): FunSpec {
        var constructorBuilder = FunSpec.constructorBuilder()
        list.forEachIndexed { index, ksClassDeclaration ->
            val clsName = ksClassDeclaration.toClassName()
            val className = clsName.simpleName
            val packageName = clsName.packageName
            logger.warn("${TAG}:${className}被ServiceApi注解")
            val service = ksClassDeclaration.getAnnotationsByType(ServiceApi::class).first()
            val inteceptors = try {
                service.interceptors.asList()
            } catch (e: KSTypesNotPresentException) {
                e.ksTypes
            }
            val debugInteceptors = try {
                service.debugInterceptors.asList()
            } catch (e: KSTypesNotPresentException) {
                e.ksTypes
            }
            val interceptorName = "interceptor$index"
            val debugInterceptorName = "debugInterceptor$index"
            constructorBuilder =
                constructorBuilder.addStatement(
                    "val $interceptorName = mutableListOf<%T>()",
                    CLASS_NAME_INTERCEPTOR
                ).addStatement(
                    "val $debugInterceptorName = mutableListOf<%T>()",
                    CLASS_NAME_INTERCEPTOR
                )
            if (inteceptors.isNotEmpty()) {
                inteceptors.filterIsInstance<KSType>().forEach { interceptor ->
                    constructorBuilder =
                        constructorBuilder.addStatement(
                            "$interceptorName.add(%T())",
                            interceptor.toClassName()
                        )
                }
            }

            if (debugInteceptors.isNotEmpty()) {
                debugInteceptors.filterIsInstance<KSType>().forEach { interceptor ->
                    logger.warn("${interceptor.toClassName()}")
                    constructorBuilder =
                        constructorBuilder.addStatement(
                            "$debugInterceptorName.add(%T())",
                            interceptor.toClassName()
                        )
                }
            }
            constructorBuilder = constructorBuilder.addStatement(
                "serviceMap.put(%S, %T(%S, %L, %L, $interceptorName,$debugInterceptorName,%L))",
                "$packageName.$className",
                CLASS_NAME_SERVICE_PARAM,
                service.baseUrl,
                service.connectTimeout,
                service.readTimeout,
                service.rxJava
            )
        }
        return constructorBuilder.build()
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

    private fun getServiceParamFun(): FunSpec {
        var funSpecBuilder = FunSpec
            .builder("getServiceParam")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("className", String::class)
            .addStatement("return serviceMap.get(className)!!")
            .returns(CLASS_NAME_SERVICE_PARAM)
        return funSpecBuilder.build()
    }

    @OptIn(KotlinPoetKspPreview::class, KspExperimental::class)
    private fun getResponseBodyConverterFun(list: List<KSClassDeclaration>): FunSpec {
        var funSpecBuilder = FunSpec
            .builder("getResponseBodyConverter")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("className", String::class)
            .addParameter("type", Type::class)
            .addStatement("val bodyConverter = when(className){")

        list.forEach { typeElement ->
            val clsName = typeElement.toClassName()
            val className = clsName.simpleName
            val packageName = clsName.packageName
            val service = typeElement.getAnnotationsByType(ServiceApi::class).first()
            val converter = try {
                service.responseConverter
            } catch (e: KSTypeNotPresentException) {
                e.ksType
            }
            funSpecBuilder = funSpecBuilder
                .addStatement("  \"$packageName.$className\" ->{")
                .addStatement("    %T()", (converter as KSType).toClassName())
                .addStatement("  }")
        }
        funSpecBuilder = funSpecBuilder
            .addStatement("  else ->{")
            .addStatement("    null")
            .addStatement("  }")
            .addStatement("}")
            .addStatement("when(bodyConverter){")
            .addStatement("  is %T ->{", CLASS_NAME_BASE_RESPONSE_BODY_CONVERTER)
            .addStatement("   bodyConverter.type = type")
            .addStatement("  }")
            .addStatement("  is %T ->{", CLASS_NAME_GSON_RESPONSE_BODY_CONVERTER)
            .addStatement("   bodyConverter.type = type")
            .addStatement("  }")
            .addStatement("}")
            .addStatement("return bodyConverter")
            .returns(CLASS_NAME_CONVERTER_OF_RESPONSE_BODY_AND_ANY.copy(nullable = true))
        return funSpecBuilder.build()
    }
}