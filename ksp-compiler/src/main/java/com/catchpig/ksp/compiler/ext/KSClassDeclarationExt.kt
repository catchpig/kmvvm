package com.catchpig.ksp.compiler

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName

@OptIn(KotlinPoetKspPreview::class)
fun KSClassDeclaration.className(): String {
    return this.toClassName().simpleName
}

@OptIn(KotlinPoetKspPreview::class)
fun KSClassDeclaration.packageName(): String {
    return this.toClassName().packageName
}

@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> KSClassDeclaration.getAnnotation(): T? {
    val list = getAnnotationsByType(T::class).toList()
    return if (list.isEmpty()) {
        null
    } else {
        list.first()
    }
}