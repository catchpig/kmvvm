package com.catchpig.ksp.compiler.generator

import com.catchpig.annotation.*
import com.catchpig.ksp.compiler.ext.getAnnotation
import com.catchpig.ksp.compiler.ext.getAnnotations
import com.catchpig.ksp.compiler.ext.getKSClassDeclarations
import com.catchpig.ksp.compiler.getAnnotation
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class ActivityGenerator(
    codeGenerator: CodeGenerator,
    logger: KSPLogger
) : BaseGenerator(codeGenerator, logger) {
    companion object {
        private const val TAG = "ActivityGenerator"
        private val CLASS_NAME_TITLE_PARAM = ClassName("com.catchpig.mvvm.entity", "TitleParam")
        private val CLASS_NAME_STATUS_BAR_PARAM =
            ClassName("com.catchpig.mvvm.entity", "StatusBarParam")
        private val CLASS_NAME_ACTIVITY_COMPILER =
            ClassName("com.catchpig.mvvm.ksp.interfaces", "ActivityCompiler")
        private val CLASS_NAME_BASE_ACTIVITY =
            ClassName("com.catchpig.mvvm.base.activity", "BaseActivity")
        private val CLASS_NAME_ACTIVITY = ClassName("android.app", "Activity")

        private val TYPE_VIEW_STUB = ClassName("android.view", "ViewStub")
        private val TYPE_TITLE_BAR_CONTROLLER =
            ClassName("com.catchpig.mvvm.controller", "TitleBarController")
        private val TYPE_STATUS_BAR_CONTROLLER =
            ClassName("com.catchpig.mvvm.controller", "StatusBarController")
        private val TYPE_TEXT_VIEW = ClassName("android.widget", "TextView")
        private val TYPE_IMAGE_VIEW = ClassName("android.widget", "ImageView")
        private val TYPE_VIEW = ClassName("android.view", "View")
        private val CLASS_NAME_LOADING_VIEW_CONTROLLER =
            ClassName("com.catchpig.mvvm.controller", "LoadingViewController")
    }

    override fun process(resolver: Resolver) {
        val statusBarClassDeclarations = resolver.getKSClassDeclarations<StatusBar>()
        val titleClassDeclarations = resolver.getKSClassDeclarations<Title>()
        val ksClassDeclarations = statusBarClassDeclarations.toMutableSet()
        ksClassDeclarations.addAll(titleClassDeclarations)
        generate(ksClassDeclarations)
    }

    private fun generate(list: Set<KSClassDeclaration>) {
        list.forEach {
            val title = it.getAnnotation<Title>()

            val statusBar = it.getAnnotation<StatusBar>()

            val className = it.toClassName().simpleName
            val fullPackageName = it.toClassName().packageName

            val typeSpecBuilder = TypeSpec
                .classBuilder("${className}_Compiler")
                .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                .addSuperinterface(CLASS_NAME_ACTIVITY_COMPILER)
                .addProperty(initTitleProperty(title, className))
                .addProperty(initStatusBarProperty(statusBar, className))
            val funSpec = initTitleMenuOnClick(it, title)
            funSpec?.let { fsc ->
                typeSpecBuilder.addFunction(fsc)
            }
            typeSpecBuilder.addFunction(injectFun(className, funSpec != null))

            val typeSpec = typeSpecBuilder.build()
            FileSpec
                .builder(fullPackageName, typeSpec.name!!)
                .addType(typeSpec)
                .addImport("com.catchpig.mvvm", "R")
                .build()
                .writeTo(codeGenerator, false)
        }
    }

    /**
     * 初始化标题栏的点击事件
     */
    private fun initTitleMenuOnClick(
        ksClassDeclaration: KSClassDeclaration,
        title: Title?
    ): FunSpec? {
        val functions = ksClassDeclaration.getAllFunctions()

        /**
         * OnClickFirstText,OnClickFirstDrawable,OnClickSecondText,OnClickSecondDrawable是否有注解
         *
         * 只要其中有一个注解,则为true
         */
        var flag = false
        var builder = FunSpec
            .builder("initTitleMenuOnClick")
            .addParameter("activity", ksClassDeclaration.toClassName())
            .addModifiers(KModifier.PRIVATE)
        title?.let {
            //第一个文字按钮
            functions.find {
                return@find it.getAnnotations<OnClickFirstText>().isNotEmpty()
            }?.run {
                val onClickFirstText = getAnnotation<OnClickFirstText>()
                if (onClickFirstText != null) {
                    flag = true
                    builder = builder
                        .addStatement("//第一个文字按钮")
                        .addStatement(
                            "var rightFirstText = activity.findViewById<%T>(R.id.rightFirstText)",
                            TYPE_TEXT_VIEW
                        )
                        .addStatement("rightFirstText.setText(%L)", onClickFirstText.value)
                        .addStatement("rightFirstText.visibility = %T.VISIBLE", TYPE_VIEW)
                        .addStatement("rightFirstText.setOnClickListener {")
                    val funcName = simpleName.getShortName()
                    when (parameters.size) {
                        0 -> {
                            builder = builder.addStatement("  activity.${funcName}()")
                        }

                        1 -> {
                            val paramType = parameters.first().type.toTypeName().toString()
                            if (paramType == TYPE_VIEW.canonicalName) {
                                builder = builder.addStatement("  activity.${funcName}(it)")
                            } else {
                                error(TAG, "OnClickFirstText注解修饰的参数类型只能为View")
                            }
                        }

                        else -> {
                            error(
                                TAG,
                                "OnClickFirstText注解修饰的参数个数只能<=1,且类型类型只能为View"
                            )
                        }
                    }
                    builder = builder.addStatement("}")
                }
            }
            //第一个图片按钮
            functions.find {
                return@find it.getAnnotations<OnClickFirstDrawable>().isNotEmpty()
            }?.run {
                val onClickFirstDrawable = getAnnotation<OnClickFirstDrawable>()
                if (onClickFirstDrawable != null) {
                    flag = true
                    builder = builder
                        .addStatement("//第一个图片按钮")
                        .addStatement(
                            "var rightFirstDrawable = activity.findViewById<%T>(R.id.rightFirstDrawable)",
                            TYPE_IMAGE_VIEW
                        )
                        .addStatement(
                            "rightFirstDrawable.setImageResource(%L)",
                            onClickFirstDrawable.value
                        )
                        .addStatement("rightFirstDrawable.visibility = %T.VISIBLE", TYPE_VIEW)
                        .addStatement("rightFirstDrawable.setOnClickListener {")
                    val funcName = simpleName.getShortName()
                    when (parameters.size) {
                        0 -> {
                            builder = builder.addStatement("  activity.${funcName}()")
                        }

                        1 -> {
                            val paramType = parameters.first().type.toTypeName().toString()
                            if (paramType == TYPE_VIEW.canonicalName) {
                                builder = builder.addStatement("  activity.${funcName}(it)")
                            } else {
                                error(TAG, "OnClickFirstDrawable注解修饰的参数类型只能为View")
                            }
                        }

                        else -> {
                            error(
                                TAG,
                                "OnClickFirstDrawable注解修饰的参数个数只能<=1,且类型类型只能为View"
                            )
                        }
                    }
                    builder = builder.addStatement("}")
                }
            }
            //第二个文字按钮
            functions.find {
                return@find it.getAnnotations<OnClickSecondText>().isNotEmpty()
            }?.run {
                val onClickSecondText = getAnnotation<OnClickSecondText>()
                if (onClickSecondText != null) {
                    flag = true
                    builder = builder
                        .addStatement("//第二个文字按钮")
                        .addStatement(
                            "var rightSecondText = activity.findViewById<%T>(R.id.rightSecondText)",
                            TYPE_TEXT_VIEW
                        )
                        .addStatement("rightSecondText.setText(%L)", onClickSecondText.value)
                        .addStatement("rightSecondText.visibility = %T.VISIBLE", TYPE_VIEW)
                        .addStatement("rightSecondText.setOnClickListener {")
                    val funcName = simpleName.getShortName()
                    when (parameters.size) {
                        0 -> {
                            builder = builder.addStatement("  activity.${funcName}()")
                        }

                        1 -> {
                            val paramType = parameters.first().type.toTypeName().toString()
                            if (paramType == TYPE_VIEW.canonicalName) {
                                builder = builder.addStatement("  activity.${funcName}(it)")
                            } else {
                                error(TAG, "OnClickSecondText注解修饰的参数类型只能为View")
                            }
                        }

                        else -> {
                            error(
                                TAG,
                                "OnClickSecondText注解修饰的参数个数只能<=1,且类型类型只能为View"
                            )
                        }
                    }
                    builder = builder.addStatement("}")
                }
            }
            //第二个图片按钮
            functions.find {
                return@find it.getAnnotations<OnClickSecondDrawable>().isNotEmpty()
            }?.run {
                val onClickSecondDrawable = getAnnotation<OnClickSecondDrawable>()
                if (onClickSecondDrawable != null) {
                    flag = true
                    builder = builder
                        .addStatement("//第二个图片按钮")

                        .addStatement(
                            "var rightSecondDrawable = activity.findViewById<%T>(R.id.rightSecondDrawable)",
                            TYPE_IMAGE_VIEW
                        )
                        .addStatement(
                            "rightSecondDrawable.setImageResource(%L)",
                            onClickSecondDrawable.value
                        )
                        .addStatement("rightSecondDrawable.visibility = %T.VISIBLE", TYPE_VIEW)
                        .addStatement("rightSecondDrawable.setOnClickListener {")
                    val funcName = simpleName.getShortName()
                    when (parameters.size) {
                        0 -> {
                            builder = builder.addStatement("  activity.${funcName}()")
                        }

                        1 -> {
                            val paramType = parameters.first().type.toTypeName().toString()
                            if (paramType == TYPE_VIEW.canonicalName) {
                                builder = builder.addStatement("  activity.${funcName}(it)")
                            } else {
                                error(TAG, "OnClickSecondDrawable注解修饰的参数类型只能为View")
                            }
                        }

                        else -> {
                            error(
                                TAG,
                                "OnClickSecondDrawable注解修饰的参数个数只能<=1,且类型类型只能为View"
                            )
                        }
                    }
                    builder = builder.addStatement("}")
                }
            }

        }
        return if (flag) {
            builder.build()
        } else {
            null
        }
    }

    private fun injectFun(className: String, isInitMenuFun: Boolean): FunSpec {
        var funSpecBuilder = FunSpec
            .builder("inject")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
            .addParameter("activity", CLASS_NAME_ACTIVITY)
            .addStatement("val baseActivity = activity as %T<*>", CLASS_NAME_BASE_ACTIVITY)
            .addStatement(
                "baseActivity.initLoadingViewController(%T(baseActivity,baseActivity.getRootBanding()))",
                CLASS_NAME_LOADING_VIEW_CONTROLLER
            )
            .addStatement("//加载标题栏")
            .addStatement("title?.let{")
            .addStatement(
                "    val titleBarViewStub = baseActivity.findViewById<%T>(R.id.title_bar_view_stub)",
                TYPE_VIEW_STUB
            )
            .addStatement("    titleBarViewStub.setOnInflateListener { _, view ->")


            .addStatement(
                "    val titleBarController = %T(baseActivity,it)",
                TYPE_TITLE_BAR_CONTROLLER
            )
            .addStatement("    titleBarController.initTitleBar(view)")

        if (isInitMenuFun) {
            funSpecBuilder.addStatement("  initTitleMenuOnClick(baseActivity as $className)")
        }
        return funSpecBuilder
            .addStatement("  }")
            .addStatement("  titleBarViewStub.inflate()")
            .addStatement("}")
            .addStatement("//加载状态栏")
            .addStatement(
                "val statusBarController = %T(baseActivity,title,statusBar)",
                TYPE_STATUS_BAR_CONTROLLER
            )
            .addStatement("baseActivity.initStatusBarController(statusBarController)")
            .addStatement("statusBarController.checkStatusBar()")

            .build()
    }

    /**
     * 添加StatusBar属性
     */
    private fun initStatusBarProperty(statusBar: StatusBar?, className: String): PropertySpec {
        var builder = PropertySpec
            .builder("statusBar", CLASS_NAME_STATUS_BAR_PARAM.copy(nullable = true))
            .addModifiers(KModifier.PRIVATE)
        return if (null == statusBar) {
            warning(TAG, "$className:StatusBar注解没有使用")
            builder
                .initializer("null")
                .build()
        } else {
            builder
                .initializer("StatusBarParam(${statusBar.hide},${statusBar.enabled},${statusBar.transparent})")
                .build()
        }
    }

    /**
     * 添加title属性
     */
    private fun initTitleProperty(title: Title?, className: String): PropertySpec {
        var builder = PropertySpec
            .builder("title", CLASS_NAME_TITLE_PARAM.copy(nullable = true))
            .addModifiers(KModifier.PRIVATE)
        return if (null == title) {
            warning(TAG, "$className:Title注解没有使用")
            builder
                .initializer("null")
                .build()
        } else {
            warning(TAG, "$className.title.value${title.value}")
            builder
                .initializer(
                    "TitleParam(%L,%L,%L,%L)",
                    title.value,
                    title.backgroundColor,
                    title.textColor,
                    title.backIcon
                )
                .build()
        }
    }
}