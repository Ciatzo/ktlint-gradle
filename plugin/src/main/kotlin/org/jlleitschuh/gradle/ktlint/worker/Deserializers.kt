package org.jlleitschuh.gradle.ktlint.worker

import com.pinterest.ktlint.core.ReporterProvider
import org.apache.commons.io.serialization.ValidatingObjectInputStream
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

internal fun loadErrors(
    serializedErrors: File
): List<LintErrorResult> = ValidatingObjectInputStream(
    BufferedInputStream(
        FileInputStream(serializedErrors)
    )
)
    .use {
        it.accept(
            ArrayList::class.java,
            LintErrorResult::class.java,
            File::class.java,
            Pair::class.java,
            SerializableLintError::class.java,
            java.lang.Boolean::class.java
        )
        it.accept("kotlin.Pair")
        @Suppress("UNCHECKED_CAST")
        it.readObject() as List<LintErrorResult>
    }

internal fun loadReporterProviders(
    serializedReporterProviders: File
): List<ReporterProvider> = ValidatingObjectInputStream(
    BufferedInputStream(
        FileInputStream(serializedReporterProviders)
    )
)
    .use {
        it.accept(
            ArrayList::class.java,
            SerializableReporterProvider::class.java
        )
        @Suppress("UNCHECKED_CAST")
        it.readObject() as List<SerializableReporterProvider>
    }
    .map { it.reporterProvider }
