package com.catchpig.compiler

import com.catchpig.annotation.Prefs
import com.catchpig.annotation.PrefsField
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind

/**
 * Prefs注解生成器
 * @author catchpig
 * @date 2019/10/29 00:29
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class PrefsProcessor : BaseProcessor() {
    companion object {
        private const val TAG = "PrefsProcessor"
        private val CLASS_NAME_SHARED_PREFERENCES_EDITOR =
            ClassName("android.content.SharedPreferences", "Editor")
        private val CLASS_NAME_SHARED_PREFERENCES =
            ClassName("android.content", "SharedPreferences")

        private val CLASS_NAME_CONTEXT_MANAGER = ClassName("com.catchpig.mvvm.manager","ContextManager")

        private const val JAVA_STRING = "java.lang.String"

    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        var set = HashSet<String>()
        set.add(Prefs::class.java.canonicalName)
        return set
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(Prefs::class.java)
        elements.map {
            it as TypeElement
        }.forEach {
            val prefs = it.getAnnotation(Prefs::class.java)
            val className = it.simpleName.toString()
            warning(TAG,"${className}被SharedPrefs注解")
            val fullPackageName = elementUtils.getPackageOf(it).qualifiedName.toString()

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
                .writeTo(filer)
        }
        return true
    }

    private fun addFuns(element: TypeElement): MutableList<FunSpec> {
        val className = element.simpleName.toString()
        var funSpecs = ArrayList<FunSpec>()
        val fieldElements = elementUtils.getAllMembers(element)
        fieldElements.forEach {
            it.getAnnotation(PrefsField::class.java)?.let { prefsField ->
                val fieldName = it.simpleName.toString()
                warning(TAG,"${className}->${fieldName}被PrefsField注解")
                val prefsKey = if (prefsField.value.isEmpty()) {
                    fieldName
                } else {
                    prefsField.value
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
    private fun createGetFunction(element: Element, funName: String, prefsKey: String): FunSpec {
        var getFunSpecBuilder = FunSpec
            .builder("get${funName}")

        when (element.asType().kind) {
            TypeKind.BOOLEAN -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .addStatement("return sharedPrefs.getBoolean(%S,false)", prefsKey)
                    .returns(BOOLEAN)
            }
            TypeKind.FLOAT -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .addStatement("return sharedPrefs.getFloat(%S,0f)", prefsKey)
                    .returns(FLOAT)
            }
            TypeKind.INT -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(INT)
                    .addStatement("return sharedPrefs.getInt(%S,0)", prefsKey)
            }
            TypeKind.LONG -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(LONG)
                    .addStatement("return sharedPrefs.getLong(%S,0)", prefsKey)
            }
            TypeKind.DOUBLE -> {
                getFunSpecBuilder = getFunSpecBuilder
                    .returns(DOUBLE)
                    .addStatement(
                        "return sharedPrefs.getString(%S,%S)!!.toDouble()",
                        prefsKey,
                        "0.0"
                    )
            }
            TypeKind.DECLARED -> {
                if (element.asType().toString() == JAVA_STRING) {
                    getFunSpecBuilder = getFunSpecBuilder
                        .returns(STRING)
                        .addStatement("return sharedPrefs.getString(%S,%S)!!", prefsKey, "")
                }
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
        element: Element,
        funName: String,
        fieldName: String,
        prefsKey: String
    ): FunSpec {
        var setFunSpecBuilder = FunSpec
            .builder("set${funName}")
        when (element.asType().kind) {
            TypeKind.BOOLEAN -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, BOOLEAN)
                    .addStatement("sharedEditor.putBoolean(%S,$fieldName).apply()", prefsKey)
            }
            TypeKind.FLOAT -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, FLOAT)
                    .addStatement("sharedEditor.putFloat(%S,$fieldName).apply()", prefsKey)
            }
            TypeKind.INT -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, INT)
                    .addStatement("sharedEditor.putInt(%S,$fieldName).apply()", prefsKey)
            }
            TypeKind.LONG -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, LONG)
                    .addStatement("sharedEditor.putLong(%S,$fieldName).apply()", prefsKey)
            }
            TypeKind.DOUBLE -> {
                setFunSpecBuilder = setFunSpecBuilder
                    .addParameter(fieldName, DOUBLE)
                    .addStatement(
                        "sharedEditor.putString(%S,${fieldName}.toString()).apply()",
                        prefsKey
                    )
            }
            TypeKind.DECLARED -> {
                if (element.asType().toString() == JAVA_STRING) {
                    setFunSpecBuilder = setFunSpecBuilder
                        .addParameter(fieldName, STRING)
                        .addStatement("sharedEditor.putString(%S,${fieldName}).apply()", prefsKey)
                }
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
                "sharedPrefs = %T.getInstance().getSharedPreferences(\"SharedPrefs_$sharedPrefsName\",%L)",
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
    private fun isCreateMethod(element: Element): Boolean {
        return when (element.asType().kind) {
            TypeKind.BOOLEAN, TypeKind.FLOAT, TypeKind.INT, TypeKind.LONG, TypeKind.DOUBLE -> {
                true
            }
            TypeKind.DECLARED -> {
                element.asType().toString() == JAVA_STRING
            }
            else -> {
                false
            }
        }
    }
}
