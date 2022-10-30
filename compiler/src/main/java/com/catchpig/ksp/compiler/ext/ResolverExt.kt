package com.catchpig.ksp.compiler.ext

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

inline fun <reified T> Resolver.getKSClassDeclarations(): List<KSClassDeclaration> {
    return getSymbolsWithAnnotation(T::class.qualifiedName!!)
        .filterIsInstance<KSClassDeclaration>().toList()
}

inline fun <reified T> Resolver.getKSClassDeclaration(): KSClassDeclaration {
    return getKSClassDeclarations<T>().first()
}