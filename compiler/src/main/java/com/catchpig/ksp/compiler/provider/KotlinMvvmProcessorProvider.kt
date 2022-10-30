package com.catchpig.ksp.compiler.provider

import com.catchpig.ksp.compiler.processor.KotlinMvvmProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KotlinMvvmProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return KotlinMvvmProcessor(environment.codeGenerator, environment.logger)
    }
}