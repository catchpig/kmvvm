package com.catchpig.ksp.compiler

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