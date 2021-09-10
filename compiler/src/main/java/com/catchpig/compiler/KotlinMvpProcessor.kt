package com.catchpig.compiler

import com.catchpig.annotation.*
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class KotlinMvpProcessor : BaseProcessor() {
    companion object {
        private val CLASS_NAME_TITLE_PARAM = ClassName("com.catchpig.mvp.entity", "TitleParam")
        private val CLASS_NAME_STATUS_BAR_PARAM = ClassName("com.catchpig.mvp.entity", "StatusBarParam")
        private val CLASS_NAME_MVP_COMPILER = ClassName("com.catchpig.mvp.apt", "MvpCompiler")
        private val CLASS_NAME_BASE_ACTIVITY = ClassName("com.catchpig.mvp.base.activity", "BaseActivity")

        private val TYPE_VIEW_STUB = Class.forName("android.view.ViewStub")
        private val TYPE_TITLE_BAR_CONTROLLER = Class.forName("com.catchpig.mvp.controller.TitleBarController")
        private val TYPE_STATUS_BAR_CONTROLLER = Class.forName("com.catchpig.mvp.controller.StatusBarController")
        private val TYPE_TEXT_VIEW = Class.forName("android.widget.TextView")
        private val TYPE_IMAGE_VIEW = Class.forName("android.widget.ImageView")
        private val TYPE_VIEW = Class.forName("android.view.View")

    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        var set = HashSet<String>()
        set.add(Title::class.java.canonicalName)
        set.add(StatusBar::class.java.canonicalName)
        set.add(OnClickFirstDrawable::class.java.canonicalName)
        set.add(OnClickFirstText::class.java.canonicalName)
        set.add(OnClickSecondDrawable::class.java.canonicalName)
        set.add(OnClickSecondText::class.java.canonicalName)
        return set
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val allElements = roundEnv.rootElements
        val elements = allElements.filter {
            if (it is TypeElement) {
                if (it.getAnnotation(Title::class.java) != null
                        || it.getAnnotation(StatusBar::class.java) != null
                        || superClassIsBaseActivity(it)) {
                    return@filter true
                }
            }
            return@filter false
        }.map {
            it as TypeElement
        }
        elements.forEach {
            val title = it.getAnnotation(Title::class.java)
            val statusBar = it.getAnnotation(StatusBar::class.java)

            val className = it.simpleName.toString()
            val fullPackageName = elementUtils.getPackageOf(it).qualifiedName.toString()
            val typeSpecBuilder = TypeSpec
                    .classBuilder(className + "_MvpCompiler")
                    .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
                    .addSuperinterface(CLASS_NAME_MVP_COMPILER)
                    .addProperty(initTitleProperty(title, className))
                    .addProperty(initStatusBarProperty(statusBar, className))
            val funSpec = initTitleMenuOnClick(it, title)
            funSpec?.let { fsc ->
                typeSpecBuilder.addFunction(fsc)
            }
            typeSpecBuilder.addFunction(injectFun(className,funSpec!=null))

            val typeSpec = typeSpecBuilder.build()
            FileSpec
                    .builder(fullPackageName, typeSpec.name!!)
                    .addType(typeSpec)
                    .addImport("com.catchpig.mvp", "R")
                    .build()
                    .writeTo(filer)
        }
        return true
    }

    /**
     * 初始化标题栏的点击事件
     */
    private fun initTitleMenuOnClick(element: TypeElement, title: Title?): FunSpec? {
        val elements = elementUtils.getAllMembers(element)
        /**
         * OnClickFirstText,OnClickFirstDrawable,OnClickSecondText,OnClickSecondDrawable是否有注解
         *
         * 只要其中有一个注解,则为true
         */
        var flag = false
        var builder = FunSpec
                .builder("initTitleMenuOnClick")
                .addParameter("activity", element.asType().asTypeName())
                .addModifiers(KModifier.PRIVATE)
        title?.let {

            //第一个文字按钮
            elements.find {
                return@find it.getAnnotation(OnClickFirstText::class.java) != null
            }?.let {
                it as ExecutableElement
            }?.run {
                val onClickFirstText = getAnnotation(OnClickFirstText::class.java)
                if (onClickFirstText != null) {
                    flag = true
                    builder = builder
                            .addStatement("//第一个文字按钮")
                            .addStatement("var rightFirstText = activity.findViewById<%T>(R.id.rightFirstText)", TYPE_TEXT_VIEW)
                            .addStatement("rightFirstText.setText(%L)", onClickFirstText.value)
                            .addStatement("rightFirstText.visibility = %T.VISIBLE", TYPE_VIEW)
                            .addStatement("rightFirstText.setOnClickListener {")
                    when (parameters.size) {
                        0 -> {
                            builder = builder.addStatement("  activity.${simpleName}()")
                        }
                        1 -> {
                            val paramType = parameters[0].asType().toString()
                            if (paramType == TYPE_VIEW.typeName) {
                                builder = builder.addStatement("  activity.${simpleName}(it)")
                            } else {
                                error("OnClickFirstText注解修饰的参数类型只能为View")
                            }
                        }
                        else -> {
                            error("OnClickFirstText注解修饰的参数个数只能<=1,且类型类型只能为View")
                        }
                    }
                    builder = builder.addStatement("}")
                }
            }
            //第一个图片按钮
            elements.find {
                return@find it.getAnnotation(OnClickFirstDrawable::class.java) != null
            }?.let {
                it as ExecutableElement
            }?.run {
                val onClickFirstDrawable = getAnnotation(OnClickFirstDrawable::class.java)
                if (onClickFirstDrawable != null) {
                    flag = true
                    builder = builder
                            .addStatement("//第一个图片按钮")
                            .addStatement("var rightFirstDrawable = activity.findViewById<%T>(R.id.rightFirstDrawable)", TYPE_IMAGE_VIEW)
                            .addStatement("rightFirstDrawable.setImageResource(%L)", onClickFirstDrawable.value)
                            .addStatement("rightFirstDrawable.visibility = %T.VISIBLE", TYPE_VIEW)
                            .addStatement("rightFirstDrawable.setOnClickListener {")
                    when (parameters.size) {
                        0 -> {
                            builder = builder.addStatement("  activity.${simpleName}()")
                        }
                        1 -> {
                            val paramType = parameters[0].asType().toString()
                            if (paramType == TYPE_VIEW.typeName) {
                                builder = builder.addStatement("  activity.${simpleName}(it)")
                            } else {
                                error("OnClickFirstDrawable注解修饰的参数类型只能为View")
                            }
                        }
                        else -> {
                            error("OnClickFirstDrawable注解修饰的参数个数只能<=1,且类型类型只能为View")
                        }
                    }
                    builder = builder.addStatement("}")
                }
            }
            //第二个文字按钮
            elements.find {
                return@find it.getAnnotation(OnClickSecondText::class.java) != null
            }?.let {
                it as ExecutableElement
            }?.run {
                val onClickSecondText = getAnnotation(OnClickSecondText::class.java)
                if (onClickSecondText != null) {
                    flag = true
                    builder = builder
                            .addStatement("//第二个文字按钮")
                            .addStatement("var rightSecondText = activity.findViewById<%T>(R.id.rightSecondText)", TYPE_TEXT_VIEW)
                            .addStatement("rightSecondText.setText(%L)", onClickSecondText.value)
                            .addStatement("rightSecondText.visibility = %T.VISIBLE", TYPE_VIEW)
                            .addStatement("rightSecondText.setOnClickListener {")
                    when (parameters.size) {
                        0 -> {
                            builder = builder.addStatement("  activity.${simpleName}()")
                        }
                        1 -> {
                            val paramType = parameters[0].asType().toString()
                            if (paramType == TYPE_VIEW.typeName) {
                                builder = builder.addStatement("  activity.${simpleName}(it)")
                            } else {
                                error("OnClickSecondText注解修饰的参数类型只能为View")
                            }
                        }
                        else -> {
                            error("OnClickSecondText注解修饰的参数个数只能<=1,且类型类型只能为View")
                        }
                    }
                    builder = builder.addStatement("}")
                }
            }
            //第二个图片按钮
            elements.find {
                return@find it.getAnnotation(OnClickSecondDrawable::class.java) != null
            }?.let {
                it as ExecutableElement
            }?.run {
                val onClickSecondDrawable = getAnnotation(OnClickSecondDrawable::class.java)
                if (onClickSecondDrawable != null) {
                    flag = true
                    builder = builder
                            .addStatement("//第二个图片按钮")
                            .addStatement("var rightSecondDrawable = activity.findViewById<%T>(R.id.rightSecondDrawable)", TYPE_IMAGE_VIEW)
                            .addStatement("rightSecondDrawable.setImageResource(%L)", onClickSecondDrawable.value)
                            .addStatement("rightSecondDrawable.visibility = %T.VISIBLE", TYPE_VIEW)
                            .addStatement("rightSecondDrawable.setOnClickListener {")
                    when (parameters.size) {
                        0 -> {
                            builder = builder.addStatement("  activity.${simpleName}()")
                        }
                        1 -> {
                            val paramType = parameters[0].asType().toString()
                            if (paramType == TYPE_VIEW.typeName) {
                                builder = builder.addStatement("  activity.${simpleName}(it)")
                            } else {
                                error("OnClickSecondDrawable注解修饰的参数类型只能为View")
                            }
                        }
                        else -> {
                            error("OnClickSecondDrawable注解修饰的参数个数只能<=1,且类型类型只能为View")
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

    private fun injectFun(className: String,isInitMenuFun:Boolean): FunSpec {
        var funSpecBuilder = FunSpec
                .builder("inject")
                .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
                .addParameter("activity", CLASS_NAME_BASE_ACTIVITY)
                .addStatement("//加载标题栏")
                .addStatement("title?.let{")
                .addStatement("  val titleBarViewStub = activity.findViewById<%T>(R.id.title_bar_view_stub)", TYPE_VIEW_STUB)
                .addStatement("  titleBarViewStub.setOnInflateListener { _, _ ->")
                .addStatement("    val titleBarController = %T(activity,it)", TYPE_TITLE_BAR_CONTROLLER)
                .addStatement("    titleBarController.initTitleBar()")
        if (isInitMenuFun) {
            funSpecBuilder.addStatement("    initTitleMenuOnClick(activity as $className)")
        }
        return funSpecBuilder
                .addStatement("  }")
                .addStatement("  titleBarViewStub.inflate()")
                .addStatement("}")
                .addStatement("//加载状态栏")
                .addStatement("val statusBarController = %T(activity,title,statusBar)", TYPE_STATUS_BAR_CONTROLLER)
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
            warning("$className:StatusBar注解没有使用")
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
            warning("$className:Title注解没有使用")
            builder
                    .initializer("null")
                    .build()
        } else {
            builder
                    .initializer("TitleParam(%L,%L,%L,%L)", title.value, title.backgroundColor, title.textColor, title.backIcon)
                    .build()
        }
    }

    /**
     * 判断父类是否是BaseActivity
     */
    private fun superClassIsBaseActivity(typeElement: TypeElement): Boolean {
        val className = typeElement.superclass.toString()
        if (className.contains("com.catchpig.mvp.base.activity")) {
            return true
        }
        return false
    }


}
