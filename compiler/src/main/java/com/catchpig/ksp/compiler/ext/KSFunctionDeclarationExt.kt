package com.catchpig.ksp.compiler.ext

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> KSFunctionDeclaration.getAnnotations(): List<T> {
    return getAnnotationsByType(T::class).toList()
}

inline fun <reified T : Annotation> KSFunctionDeclaration.getAnnotation(): T? {
    val annotations = getAnnotations<T>()
    if (annotations.isEmpty()) {
        return null
    }
    return annotations.first()
}