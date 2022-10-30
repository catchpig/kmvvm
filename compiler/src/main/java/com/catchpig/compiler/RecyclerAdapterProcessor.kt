package com.catchpig.compiler

import com.catchpig.annotation.Adapter
import com.catchpig.annotation.GlobalConfig
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RecyclerAdapterProcessor : BaseProcessor() {
    companion object {
        private const val TAG = "RecyclerAdapterProcessor"
        private val CLASS_NAME_VIEW_GROUP = ClassName("android.view", "ViewGroup")
        private val CLASS_NAME_LAYOUT_INFLATER = ClassName("android.view", "LayoutInflater")
        private val CLASS_NAME_ADAPTER_BANDING =
                ClassName("com.catchpig.mvvm.entity", "AdapterBinding")
        private val CLASS_NAME_RECYCLER_ADAPTER_COMPILER =
                ClassName("com.catchpig.mvvm.apt.interfaces", "RecyclerAdapterCompiler")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        var set = HashSet<String>()
        set.add(Adapter::class.java.canonicalName)
        return set
    }

    override fun process(
            annotations: MutableSet<out TypeElement>,
            roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(Adapter::class.java)
        elements.map {
            it as TypeElement
        }.forEach {
            val className = it.simpleName.toString()
            warning(TAG, "${className}被Adapter注解")
            val fullPackageName = elementUtils.getPackageOf(it).qualifiedName.toString()
            val typeSpecBuilder = TypeSpec
                    .classBuilder("${className}_Compiler")
                    .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                    .addSuperinterface(CLASS_NAME_RECYCLER_ADAPTER_COMPILER)
                    .addProperty(globalConfigProperty(roundEnv))
                    .addFunction(onViewBandingFun(it))

            val typeSpec = typeSpecBuilder.build()
            val fileSpec = FileSpec
                    .builder(fullPackageName, typeSpec.name!!)
                    .addType(typeSpec)

            fileSpec.build()
                    .writeTo(filer)
        }
        return true
    }

    private fun globalConfigProperty(roundEnv: RoundEnvironment): PropertySpec {
        val elements = roundEnv.getElementsAnnotatedWith(GlobalConfig::class.java)
        val globalConfigElement = elements.toList()[0] as TypeElement
        return PropertySpec
                .builder("globalConfig", globalConfigElement.asClassName())
                .addModifiers(KModifier.PRIVATE)
                .initializer("${globalConfigElement.asClassName().simpleName}()")
                .build()
    }

    private fun onViewBandingFun(typeElement: TypeElement): FunSpec {
        val recyclerAdapterType = typeElement.superclass as DeclaredType
        val bandingType = recyclerAdapterType.typeArguments[1] as DeclaredType
        val bandingElement = bandingType.asElement() as TypeElement
        var funSpecBuilder = FunSpec
                .builder("viewBanding")
                .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
                .addParameter("parent", CLASS_NAME_VIEW_GROUP)
                .addStatement(
                        "val layoutInflater = %T.from(parent.context)",
                        CLASS_NAME_LAYOUT_INFLATER
                )
                .addStatement(
                        "val normalBanding = %T.inflate(layoutInflater, parent, false)",
                        bandingElement.asClassName()
                )
                .addStatement(
                        "val emptyBanding = globalConfig.getRecyclerEmptyBanding(parent)",
                        bandingElement.asClassName()
                )
                .addStatement("return %T(normalBanding,emptyBanding)", CLASS_NAME_ADAPTER_BANDING)
                .returns(CLASS_NAME_ADAPTER_BANDING)
        return funSpecBuilder.build()
    }

}