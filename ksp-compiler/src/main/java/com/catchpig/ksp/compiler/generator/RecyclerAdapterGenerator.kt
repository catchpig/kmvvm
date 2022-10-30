package com.catchpig.ksp.compiler.generator

import com.catchpig.annotation.Adapter
import com.catchpig.annotation.GlobalConfig
import com.catchpig.ksp.compiler.ext.getKSClassDeclaration
import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.*

class RecyclerAdapterGenerator(codeGenerator: CodeGenerator, kspLogger: KSPLogger) :
    BaseGenerator(codeGenerator, kspLogger) {
    companion object {
        private const val TAG = "RecyclerAdapterGenerator"
        private val CLASS_NAME_VIEW_GROUP = ClassName("android.view", "ViewGroup")
        private val CLASS_NAME_LAYOUT_INFLATER = ClassName("android.view", "LayoutInflater")
        private val CLASS_NAME_ADAPTER_BANDING =
            ClassName("com.catchpig.mvvm.entity", "AdapterBinding")
        private val CLASS_NAME_RECYCLER_ADAPTER_COMPILER =
            ClassName("com.catchpig.mvvm.apt.interfaces", "RecyclerAdapterCompiler")
    }

    lateinit var resolver: Resolver

    override fun process(resolver: Resolver): List<KSAnnotated> {
        this.resolver = resolver
        val symbols = resolver.getSymbolsWithAnnotation(Adapter::class.qualifiedName!!)
        val list = symbols.filterIsInstance<KSClassDeclaration>().toList()
        if (list.isNotEmpty()) {
            generate(list)
        }
        return list
    }

    @OptIn(KotlinPoetKspPreview::class, KspExperimental::class)
    private fun generate(list: List<KSClassDeclaration>) {
        list.forEach {
            val className = it.toClassName().simpleName
            warning(TAG, "${className}被Adapter注解${it.toClassName().canonicalName}")
            val fullPackageName = it.toClassName().packageName
            val typeSpecBuilder = TypeSpec
                .classBuilder("${className}_Compiler")
                .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                .addSuperinterface(CLASS_NAME_RECYCLER_ADAPTER_COMPILER)
//                .addProperty(globalConfigProperty())
                .addFunction(onViewBandingFun(it))

            val typeSpec = typeSpecBuilder.build()
            FileSpec
                .builder(fullPackageName, typeSpec.name!!)
                .addType(typeSpec)
                .build()
                .writeTo(codeGenerator, false)
        }
    }

    @OptIn(KotlinPoetKspPreview::class)
    private fun globalConfigProperty(): PropertySpec {
        val ksClassDeclaration =
            resolver.getKSClassDeclaration<GlobalConfig>()
        return PropertySpec
            .builder("globalConfig", ksClassDeclaration.toClassName())
            .addModifiers(KModifier.PRIVATE)
            .initializer("${ksClassDeclaration.toClassName().simpleName}()")
            .build()
    }

    @OptIn(KotlinPoetKspPreview::class)
    private fun onViewBandingFun(ksClassDeclaration: KSClassDeclaration): FunSpec {
        val bindingName = ksClassDeclaration.superTypes.filter {
            it.resolve().declaration.modifiers.contains(Modifier.ABSTRACT)
        }.map {
            it.resolve().declaration as KSClassDeclaration
        }.first()
        val classTypeParams = bindingName.typeParameters.toTypeParameterResolver()
        val ksFunctionDeclaration = bindingName.getDeclaredProperties().find {
            it.simpleName.getShortName()=="vb"
        } as KSPropertyDeclaration
        warning(TAG,"${ksFunctionDeclaration.type.toTypeName(classTypeParams)}")
        var ks = bindingName.superTypes.first().resolve().declaration as KSClassDeclaration
//        val classTypeParams = ks.typeParameters.toTypeParameterResolver()
        warning(TAG,"${ks.simpleName.getShortName()}")
        val vbKSPropertyDeclaration = ks.getAllProperties().find {
            warning(TAG,"${it.simpleName.getShortName()}")
            it.simpleName.getShortName() == "itemViewBinding"
        } as KSPropertyDeclaration
        warning(TAG,"${vbKSPropertyDeclaration.type.toTypeName(classTypeParams)}")
        var funSpecBuilder = FunSpec
            .builder("viewBanding")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("parent", CLASS_NAME_VIEW_GROUP)
            .addStatement(
                "val layoutInflater = %T.from(parent.context)",
                CLASS_NAME_LAYOUT_INFLATER
            )
            .addStatement(
                "val normalBanding = %S.inflate(layoutInflater, parent, false)",
                bindingName
            )
            .addStatement(
                "val emptyBanding = globalConfig.getRecyclerEmptyBanding(parent)"
            )
            .addStatement("return %T(normalBanding,emptyBanding)", CLASS_NAME_ADAPTER_BANDING)
            .returns(CLASS_NAME_ADAPTER_BANDING)
        return funSpecBuilder.build()
    }
}