package com.catchpig.compiler

import com.catchpig.annotation.ServiceApi
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.reflect.Type
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.MirroredTypesException

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ServiceApiProcessor : BaseProcessor() {
    companion object {
        private const val TAG = "ServiceApiProcessor"
        private val CLASS_NAME_SERVICE_API_COMPILER =
            ClassName("com.catchpig.mvvm.apt.interfaces", "ServiceApiCompiler")
        private val CLASS_NAME_MAP = ClassName("kotlin.collections", "HashMap")
        private val CLASS_NAME_SERVICE_PARAM =
            ClassName("com.catchpig.mvvm.entity", "ServiceParam")
        private val CLASS_NAME_INTERCEPTOR =
            ClassName("okhttp3", "Interceptor")
        private val CLASS_NAME_TYPE_ADAPTER = ClassName("com.google.gson", "TypeAdapter")
        private val CLASS_NAME_BASE_RESPONSE_BODY_CONVERTER =
            ClassName("com.catchpig.mvvm.network.converter", "BaseResponseBodyConverter")
        private val CLASS_NAME_GSON_RESPONSE_BODY_CONVERTER =
            ClassName("com.catchpig.mvvm.network.converter", "SerializationResponseBodyConverter")
        private val CLASS_NAME_CONVERTER =
            ClassName("retrofit2", "Converter")
        private val CLASS_NAME_RESPONSE_BODY = ClassName("okhttp3", "ResponseBody")
        private val CLASS_NAME_CONVERTER_OF_RESPONSE_BODY_AND_ANY =
            CLASS_NAME_CONVERTER.parameterizedBy(CLASS_NAME_RESPONSE_BODY, ANY)
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        var set = HashSet<String>()
        set.add(ServiceApi::class.java.canonicalName)
        return set
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(ServiceApi::class.java)
        if (elements.isNotEmpty()) {
            val typeSpec = TypeSpec
                .classBuilder("ServiceApi_Compiler")
                .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                .addSuperinterface(CLASS_NAME_SERVICE_API_COMPILER)
                .primaryConstructor(initConstructor(elements))
                .addProperty(initServiceProperty())
                .addFunction(getServiceParamFun())
                .addFunction(getResponseBodyConverterFun(elements))
                .build()
            val fullPackageName = CLASS_NAME_SERVICE_API_COMPILER.packageName
            var fileSpecBuilder = FileSpec
                .builder(fullPackageName, typeSpec.name!!)
                .addType(typeSpec)
            fileSpecBuilder.build()
                .writeTo(filer)
        }
        return true
    }

    private fun initConstructor(serviceElements: Set<Element>): FunSpec {
        var constructorBuilder = FunSpec.constructorBuilder()
        serviceElements.map {
            it as TypeElement
        }.forEachIndexed { index, typeElement ->
            val clsName = typeElement.asClassName()
            val className = clsName.simpleName
            val packageName = clsName.packageName
            warning(TAG, "${className}被ServiceApi注解")
            val service = typeElement.getAnnotation(ServiceApi::class.java)
            val inteceptors = try {
                service.interceptors.toList()
            } catch (e: MirroredTypesException) {
                e.typeMirrors
            }

            val debugInteceptors = try {
                service.debugInterceptors.toList()
            } catch (e: MirroredTypesException) {
                e.typeMirrors
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
                inteceptors.forEach { interceptor ->
                    constructorBuilder =
                        constructorBuilder.addStatement("$interceptorName.add(%T())", interceptor)
                }
            }

            if (debugInteceptors.isNotEmpty()) {
                debugInteceptors.forEach { interceptor ->
                    constructorBuilder =
                        constructorBuilder.addStatement(
                            "$debugInterceptorName.add(%T())",
                            interceptor
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

    private fun getResponseBodyConverterFun(serviceElements: Set<Element>): FunSpec {
        var funSpecBuilder = FunSpec
            .builder("getResponseBodyConverter")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("className", String::class)
            .addParameter("type", Type::class)
            .addStatement("val bodyConverter = when(className){")

        serviceElements.map {
            it as TypeElement
        }.forEach { typeElement ->
            val clsName = typeElement.asClassName()
            val className = clsName.simpleName
            val packageName = clsName.packageName
            val service = typeElement.getAnnotation(ServiceApi::class.java)
            val converter = try {
                service.responseConverter
            } catch (e: MirroredTypeException) {
                e.typeMirror
            }
            funSpecBuilder = funSpecBuilder
                .addStatement("  \"$packageName.$className\" ->{")
                .addStatement("    %T()", converter)
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