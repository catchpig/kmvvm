package com.catchpig.ksp.compiler.generator

import com.catchpig.annotation.Prefs
import com.catchpig.annotation.PrefsField
import com.catchpig.ksp.compiler.ext.getKSClassDeclarations
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSBuiltIns
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class PrefsGenerator(
    codeGenerator: CodeGenerator,
    logger: KSPLogger
) : BaseGenerator(codeGenerator, logger) {

    companion object {
        private const val TAG = "PrefsGenerator"
        private val CLASS_NAME_SHARED_PREFERENCES_EDITOR =
            ClassName("android.content.SharedPreferences", "Editor")
        private val CLASS_NAME_SHARED_PREFERENCES =
            ClassName("android.content", "SharedPreferences")

        private val CLASS_NAME_CONTEXT_MANAGER =
            ClassName("com.catchpig.mvvm.manager", "ContextManager")

    }

    lateinit var ksBuiltIns: KSBuiltIns

    override fun process(resolver: Resolver): List<KSAnnotated> {
        ksBuiltIns = resolver.builtIns
        val list = resolver.getKSClassDeclarations<Prefs>()
        if (list.isNotEmpty()) {
            generate(list)
        }
        return emptyList()
    }

    @OptIn(KspExperimental::class)
    private fun generate(list: List<KSClassDeclaration>) {
        list.forEach {
            val prefs = it.getAnnotationsByType(Prefs::class).first()
            val className = it.toClassName().simpleName
            warning(TAG, "${className}被SharedPrefs注解")
            val fullPackageName = it.toClassName().packageName
            val typeSpec = TypeSpec
                .objectBuilder("${className}SharedPrefs")
                .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                .addProperty(addSharedPrefsProperty())
                .addProperty(addSharedPrefsEditorProperty())
                .addInitializerBlock(addCodeBlock(prefs, className))
                .addFunctions(addFuns(it))
                .build()

            FileSpec
                .builder(fullPackageName, typeSpec.name!!)
                .addType(typeSpec)
                .build()
                .writeTo(codeGenerator, false)
        }
    }

    @OptIn(KspExperimental::class)
    private fun addFuns(ksClassDeclaration: KSClassDeclaration): MutableList<FunSpec> {
        val className = ksClassDeclaration.toClassName().simpleName
        var funSpecs = ArrayList<FunSpec>()
        val fieldElements = ksClassDeclaration.getAllProperties()
        fieldElements.forEach {
            val annotations = it.getAnnotationsByType(PrefsField::class).toList()
            if (annotations.isNotEmpty()) {
                annotations.first().let { prefsField ->
                    val fieldName = it.simpleName.getShortName()
                    warning(TAG, "${className}->${fieldName}被PrefsField注解")
                    val prefsKey = prefsField.value.ifEmpty {
                        fieldName
                    }
                    val funName = fieldName.substring(0, 1).uppercase() + fieldName.substring(1)
                    if (isCreateMethod(it)) {
                        //set方法
                        funSpecs.add(createSetFunction(it, funName, fieldName, prefsKey))
                        //get方法
                        funSpecs.add(createGetFunction(it, funName, prefsKey))
                    } else {
                        error("${fieldName}的类型不支持,只支持Double,Float,Int,Long,String,Boolean")
                    }

                }
            }

        }
        val clearFunSpec = FunSpec
            .builder("clear")
            .addStatement("sharedEditor.clear()")
            .addStatement("sharedEditor.commit()")
            .build()
        funSpecs.add(clearFunSpec)
        return funSpecs
    }

    /**
     * 创建get方法
     */
    private fun createGetFunction(
        ksPropertyDeclaration: KSPropertyDeclaration,
        funName: String,
        prefsKey: String
    ): FunSpec {
        var getFunSpecBuilder = FunSpec
            .builder("get${funName}")

        when (ksPropertyDeclaration.type.resolve()) {
            ksBuiltIns.booleanType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .addStatement("return sharedPrefs.getBoolean(%S,false)", prefsKey)
                    .returns(BOOLEAN)
            }
            ksBuiltIns.floatType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .addStatement("return sharedPrefs.getFloat(%S,0f)", prefsKey)
                    .returns(FLOAT)
            }
            ksBuiltIns.intType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(INT)
                    .addStatement("return sharedPrefs.getInt(%S,0)", prefsKey)
            }
            ksBuiltIns.longType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(LONG)
                    .addStatement("return sharedPrefs.getLong(%S,0)", prefsKey)
            }
            ksBuiltIns.doubleType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(DOUBLE)
                    .addStatement(
                        "return sharedPrefs.getString(%S,%S)!!.toDouble()",
                        prefsKey,
                        "0.0"
                    )
            }
            ksBuiltIns.stringType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(STRING)
                    .addStatement("return sharedPrefs.getString(%S,%S)!!", prefsKey, "")
            }
            else -> {
            }
        }
        return getFunSpecBuilder.build()
    }

    /**
     * 创建set方法
     */
    private fun createSetFunction(
        ksPropertyDeclaration: KSPropertyDeclaration,
        funName: String,
        fieldName: String,
        prefsKey: String
    ): FunSpec {
        var setFunSpecBuilder = FunSpec
            .builder("set${funName}")
        when (ksPropertyDeclaration.type.resolve()) {
            ksBuiltIns.booleanType -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, BOOLEAN)
                    .addStatement("sharedEditor.putBoolean(%S,$fieldName).apply()", prefsKey)
            }
            ksBuiltIns.floatType -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, FLOAT)
                    .addStatement("sharedEditor.putFloat(%S,$fieldName).apply()", prefsKey)
            }
            ksBuiltIns.intType -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, INT)
                    .addStatement("sharedEditor.putInt(%S,$fieldName).apply()", prefsKey)
            }
            ksBuiltIns.longType -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, LONG)
                    .addStatement("sharedEditor.putLong(%S,$fieldName).apply()", prefsKey)
            }
            ksBuiltIns.doubleType -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, DOUBLE)
                    .addStatement(
                        "sharedEditor.putString(%S,${fieldName}.toString()).apply()",
                        prefsKey
                    )
            }
            ksBuiltIns.stringType -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, STRING)
                    .addStatement("sharedEditor.putString(%S,${fieldName}).apply()", prefsKey)
            }
            else -> {
            }
        }
        return setFunSpecBuilder.build()
    }

    /**
     * 初始化参数
     * 包括sharedPrefs和sharedEditor
     */
    private fun addCodeBlock(prefs: Prefs, className: String): CodeBlock {
        var sharedPrefsName = if (prefs.value.isEmpty()) {
            className
        } else {
            prefs.value
        }
        return CodeBlock
            .builder()
            .addStatement(
                "sharedPrefs = %T.context.getSharedPreferences(\"SharedPrefs_$sharedPrefsName\",%L)",
                CLASS_NAME_CONTEXT_MANAGER,
                prefs.mode.value
            )
            .addStatement("sharedEditor = sharedPrefs.edit()")
            .build()
    }

    /**
     * 添加sharedEditor变量
     */
    private fun addSharedPrefsEditorProperty(): PropertySpec {
        return PropertySpec
            .builder("sharedEditor", CLASS_NAME_SHARED_PREFERENCES_EDITOR)
            .addModifiers(KModifier.PRIVATE)
            .build()
    }

    /**
     * 添加sharedPrefs变量
     */
    private fun addSharedPrefsProperty(): PropertySpec {
        return PropertySpec
            .builder("sharedPrefs", CLASS_NAME_SHARED_PREFERENCES)
            .addModifiers(KModifier.PRIVATE)
            .build()
    }

    /**
     * 是否声场set和get方法
     *
     * 只有类型为Double,Float,Int,Long,String,Boolean才能生成方法
     */
    private fun isCreateMethod(ksPropertyDeclaration: KSPropertyDeclaration): Boolean {
        return when (ksPropertyDeclaration.type.resolve()) {
            ksBuiltIns.booleanType, ksBuiltIns.floatType, ksBuiltIns.intType, ksBuiltIns.longType, ksBuiltIns.doubleType, ksBuiltIns.stringType -> {
                true
            }
            else -> {
                false
            }
        }
    }
}