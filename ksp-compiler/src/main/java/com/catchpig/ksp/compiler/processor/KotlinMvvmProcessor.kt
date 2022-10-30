package com.catchpig.ksp.compiler.processor

import com.catchpig.ksp.compiler.generator.*
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class KotlinMvvmProcessor(
    codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    companion object {
        private const val TAG = "KotlinMvvmProcessor"

    }

    private val serviceApiGenerator = ServiceApiGenerator(codeGenerator, logger)
    private val prefsGenerator = PrefsGenerator(codeGenerator, logger)

        private val recyclerAdapterGenerator = RecyclerAdapterGenerator(codeGenerator, logger)
    private val kotlinMvvmGenerator = KotlinMvvmGenerator(codeGenerator, logger)
    private val activityGenerator = ActivityGenerator(codeGenerator, logger)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("$TAG")
        val annotateds = mutableListOf<KSAnnotated>()
        annotateds.addAll(serviceApiGenerator.process(resolver))
        annotateds.addAll(prefsGenerator.process(resolver))
        annotateds.addAll(kotlinMvvmGenerator.process(resolver))
        annotateds.addAll(activityGenerator.process(resolver))
        annotateds.addAll(recyclerAdapterGenerator.process(resolver))
        return annotateds
    }


}