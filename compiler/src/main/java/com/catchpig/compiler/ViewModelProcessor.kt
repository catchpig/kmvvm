package com.catchpig.compiler

import com.catchpig.annotation.ObserverError
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ViewModelProcessor : BaseProcessor() {
    companion object {
        private const val TAG = "ViewModelProcessor"
        private val CLASS_NAME_I_OBSERVER_ERROR =
            ClassName("com.catchpig.mvvm.interfaces", "IObserverError")
        private val CLASS_NAME_LIST = ClassName("kotlin.collections", "List")
        private val CLASS_NAME_LIST_OF_I_OBSERVER_ERROR =
            CLASS_NAME_LIST.parameterizedBy(CLASS_NAME_I_OBSERVER_ERROR)
        private val CLASS_NAME_I_BASE_VIEW_MODEL =
            ClassName("com.catchpig.mvvm.base.viewmodel", "IBaseViewModel")
        private val CLASS_NAME_VIEW_MODEL_COMPILER =
            ClassName("com.catchpig.mvvm.apt.interfaces", "ViewModelCompiler")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        var set = HashSet<String>()
        set.add(ObserverError::class.java.canonicalName)
        return set
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(ObserverError::class.java)
        if (elements.isNotEmpty()) {
            warning(TAG, "size:${elements.size}")
            val typeSpecBuilder = TypeSpec
                .classBuilder("ViewModel_Compiler")
                .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                .addSuperinterface(CLASS_NAME_VIEW_MODEL_COMPILER)
                .addProperty(initObserverProperty(elements))
                .addFunction(onErrorFun())

            val typeSpec = typeSpecBuilder.build()
            val fullPackageName = CLASS_NAME_I_BASE_VIEW_MODEL.packageName
            val fileSpec = FileSpec
                .builder(fullPackageName, typeSpec.name!!)
                .addType(typeSpec)
            elements.map {
                it as TypeElement
            }.forEach {
                fileSpec.addImport(it.asClassName().packageName, it.asClassName().simpleName)
            }
            fileSpec.build()
                .writeTo(filer)
        }
        return true
    }

    private fun initObserverProperty(elements: Set<Element>): PropertySpec {
        var builder = PropertySpec
            .builder("observerErrors", CLASS_NAME_LIST_OF_I_OBSERVER_ERROR)
            .addModifiers(KModifier.PRIVATE)
        var format = "listOf("
        elements.map {
            it as TypeElement
        }.forEach {
            format += "${it.asClassName().simpleName}(),"
        }
        format = format.substring(0, format.length - 1)
        format += ")"
        warning(TAG, format)
        builder.initializer(format)
        return builder.build()
    }

    private fun onErrorFun(): FunSpec {
        var funSpecBuilder = FunSpec
            .builder("onError")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("iBaseViewModel", CLASS_NAME_I_BASE_VIEW_MODEL)
            .addParameter("t", Throwable::class)
            .addStatement("observerErrors.forEach {")
            .addStatement("  it.onError(iBaseViewModel, t)")
            .addStatement("}")
        return funSpecBuilder.build()
    }

}