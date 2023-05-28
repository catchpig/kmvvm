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
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class PrefsGenerator(
    codeGenerator: CodeGenerator,
    logger: KSPLogger
) : BaseGenerator(codeGenerator, logger) {

    companion object {
        private const val TAG = "PrefsGenerator"
        private const val DEFAULT_VALUE = "defaultValue"
        private val CLASS_NAME_SHARED_PREFERENCES_EDITOR =
            ClassName("android.content.SharedPreferences", "Editor")
        private val CLASS_NAME_SHARED_PREFERENCES =
            ClassName("android.content", "SharedPreferences")

        private val CLASS_NAME_CONTEXT_MANAGER =
            ClassName("com.catchpig.utils.manager", "ContextManager")

    }

    lateinit var ksBuiltIns: KSBuiltIns

    override fun process(resolver: Resolver) {
        ksBuiltIns = resolver.builtIns
        val list = resolver.getKSClassDeclarations<Prefs>()
        if (list.isNotEmpty()) {
            generate(list)
        }
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
                    if (isCreateMethod(it)) {
                        //apply方法
                        funSpecs.add(createApplyFunction(it, fieldName, prefsKey))
                        //commit方法
                        funSpecs.add(createCommitFunction(it, fieldName, prefsKey))
                        //get方法
                        funSpecs.add(createGetFunction(it, fieldName, prefsKey))
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
        fieldName: String,
        prefsKey: String
    ): FunSpec {
        var funName =
            if (fieldName.startsWith("is")) {
                fieldName
            } else {
                "get${fieldName.substring(0, 1).uppercase()}${fieldName.substring(1)}"
            }
        var getFunSpecBuilder = FunSpec
            .builder(funName)

        when (ksPropertyDeclaration.type.resolve()) {
            ksBuiltIns.booleanType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .addBooleanParameter(false)
                    .addStatement("return sharedPrefs.getBoolean(%S,${DEFAULT_VALUE})", prefsKey)
                    .returns(BOOLEAN)
            }

            ksBuiltIns.floatType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .addFloatParameter(0f)
                    .addStatement("return sharedPrefs.getFloat(%S,${DEFAULT_VALUE})", prefsKey)
                    .returns(FLOAT)
            }

            ksBuiltIns.intType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(INT)
                    .addIntParameter(0)
                    .addStatement("return sharedPrefs.getInt(%S,${DEFAULT_VALUE})", prefsKey)
            }

            ksBuiltIns.longType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(LONG)
                    .addLongParameter(0L)
                    .addStatement("return sharedPrefs.getLong(%S,${DEFAULT_VALUE})", prefsKey)
            }

            ksBuiltIns.doubleType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(DOUBLE)
                    .addDoubleParameter(0.0)
                    .addStatement(
                        "return sharedPrefs.getString(%S,${DEFAULT_VALUE}.toString())!!.toDouble()",
                        prefsKey
                    )
            }

            ksBuiltIns.stringType -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(STRING)
                    .addStringParameter("")
                    .addStatement("return sharedPrefs.getString(%S,${DEFAULT_VALUE})!!", prefsKey)
            }

            else -> {
            }
        }
        return getFunSpecBuilder.build()
    }

    /**
     * 创建apply方法
     */
    private fun createApplyFunction(
        ksPropertyDeclaration: KSPropertyDeclaration,
        fieldName: String,
        prefsKey: String
    ): FunSpec {
        var funName =
            if (fieldName.startsWith("is")) {
                fieldName.substring(2)
            } else {
                "${fieldName.substring(0, 1).uppercase()}${fieldName.substring(1)}"
            }
        var applyFunSpecBuilder = FunSpec.builder("apply${funName}")
        val remark = "异步存储"
        when (ksPropertyDeclaration.type.resolve()) {
            ksBuiltIns.booleanType -> {
                applyFunSpecBuilder = applyFunSpecBuilder
                    .addParameter(fieldName, BOOLEAN)
                    .addComment(remark)
                    .addStatement("sharedEditor.putBoolean(%S,$fieldName).apply()", prefsKey)
            }

            ksBuiltIns.floatType -> {
                applyFunSpecBuilder = applyFunSpecBuilder
                    .addParameter(fieldName, FLOAT)
                    .addComment(remark)
                    .addStatement("sharedEditor.putFloat(%S,$fieldName).apply()", prefsKey)
            }

            ksBuiltIns.intType -> {
                applyFunSpecBuilder = applyFunSpecBuilder
                    .addParameter(fieldName, INT)
                    .addComment(remark)
                    .addStatement("sharedEditor.putInt(%S,$fieldName).apply()", prefsKey)
            }

            ksBuiltIns.longType -> {
                applyFunSpecBuilder = applyFunSpecBuilder
                    .addParameter(fieldName, LONG)
                    .addComment(remark)
                    .addStatement("sharedEditor.putLong(%S,$fieldName).apply()", prefsKey)
            }

            ksBuiltIns.doubleType -> {
                applyFunSpecBuilder = applyFunSpecBuilder
                    .addParameter(fieldName, DOUBLE)
                    .addComment(remark)
                    .addStatement(
                        "sharedEditor.putString(%S,${fieldName}.toString()).apply()",
                        prefsKey
                    )
            }

            ksBuiltIns.stringType -> {
                applyFunSpecBuilder = applyFunSpecBuilder
                    .addParameter(fieldName, STRING)
                    .addComment(remark)
                    .addStatement("sharedEditor.putString(%S,${fieldName}).apply()", prefsKey)
            }

            else -> {
            }
        }
        return applyFunSpecBuilder.build()
    }

    /**
     * 创建apply方法
     */
    private fun createCommitFunction(
        ksPropertyDeclaration: KSPropertyDeclaration,
        fieldName: String,
        prefsKey: String
    ): FunSpec {
        var funName =
            if (fieldName.startsWith("is")) {
                fieldName.substring(2)
            } else {
                "${fieldName.substring(0, 1).uppercase()}${fieldName.substring(1)}"
            }
        var commitFunSpecBuilder = FunSpec.builder("commit${funName}")
        val remark = "同步存储"
        when (ksPropertyDeclaration.type.resolve()) {
            ksBuiltIns.booleanType -> {
                commitFunSpecBuilder = commitFunSpecBuilder
                    .addParameter(fieldName, BOOLEAN)
                    .addComment(remark)
                    .returns(BOOLEAN)
                    .addStatement(
                        "return sharedEditor.putBoolean(%S,$fieldName).commit()",
                        prefsKey
                    )
            }

            ksBuiltIns.floatType -> {
                commitFunSpecBuilder = commitFunSpecBuilder
                    .addParameter(fieldName, FLOAT)
                    .addComment(remark)
                    .returns(BOOLEAN)
                    .addStatement("return sharedEditor.putFloat(%S,$fieldName).commit()", prefsKey)
            }

            ksBuiltIns.intType -> {
                commitFunSpecBuilder = commitFunSpecBuilder
                    .addParameter(fieldName, INT)
                    .addComment(remark)
                    .returns(BOOLEAN)
                    .addStatement("return sharedEditor.putInt(%S,$fieldName).commit()", prefsKey)
            }

            ksBuiltIns.longType -> {
                commitFunSpecBuilder = commitFunSpecBuilder
                    .addParameter(fieldName, LONG)
                    .addComment(remark)
                    .returns(BOOLEAN)
                    .addStatement("return sharedEditor.putLong(%S,$fieldName).commit()", prefsKey)
            }

            ksBuiltIns.doubleType -> {
                commitFunSpecBuilder = commitFunSpecBuilder
                    .addParameter(fieldName, DOUBLE)
                    .addComment(remark)
                    .returns(BOOLEAN)
                    .addStatement(
                        "return sharedEditor.putString(%S,${fieldName}.toString()).commit()",
                        prefsKey
                    )
            }

            ksBuiltIns.stringType -> {
                commitFunSpecBuilder = commitFunSpecBuilder
                    .addParameter(fieldName, STRING)
                    .addComment(remark)
                    .returns(BOOLEAN)
                    .addStatement(
                        "return sharedEditor.putString(%S,${fieldName}).commit()",
                        prefsKey
                    )
            }

            else -> {
            }
        }
        return commitFunSpecBuilder.build()
    }

    private inline fun <reified T : Any> FunSpec.Builder.addBooleanParameter(defaultValue: T): FunSpec.Builder {
        return addParameter(
            ParameterSpec.builder(DEFAULT_VALUE, T::class).defaultValue("%L", defaultValue).build()
        )
    }

    private inline fun <reified T : Any> FunSpec.Builder.addFloatParameter(defaultValue: T): FunSpec.Builder {
        return addParameter(
            ParameterSpec.builder(DEFAULT_VALUE, T::class).defaultValue("%LF", defaultValue)
                .build()
        )
    }

    private inline fun <reified T : Any> FunSpec.Builder.addIntParameter(defaultValue: T): FunSpec.Builder {
        return addParameter(
            ParameterSpec.builder(DEFAULT_VALUE, T::class).defaultValue("%L", defaultValue).build()
        )
    }

    private inline fun <reified T : Any> FunSpec.Builder.addLongParameter(defaultValue: T): FunSpec.Builder {
        return addParameter(
            ParameterSpec.builder(DEFAULT_VALUE, T::class).defaultValue("%LL", defaultValue)
                .build()
        )
    }

    private inline fun <reified T : Any> FunSpec.Builder.addDoubleParameter(defaultValue: T): FunSpec.Builder {
        return addParameter(
            ParameterSpec.builder(DEFAULT_VALUE, T::class).defaultValue("%L", defaultValue).build()
        )
    }

    private inline fun <reified T : Any> FunSpec.Builder.addStringParameter(defaultValue: T): FunSpec.Builder {
        return addParameter(
            ParameterSpec.builder(DEFAULT_VALUE, T::class).defaultValue("%S", defaultValue).build()
        )
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
                "sharedPrefs = %T.getInstance().getContext().getSharedPreferences(\"SharedPrefs_$sharedPrefsName\",%L)",
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
        warning(TAG, "ksPropertyDeclaration.type.resolve():" + ksPropertyDeclaration.type.resolve())
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