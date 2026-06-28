package org.ivcode.core.gradle.dokkapages

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import java.io.File
import java.io.FileWriter

class IndexGenerator(
    private val modules: List<Module>,
    private val outputDir: String,
    private val template: String = "index.html.mustache",
    private val mustacheFactory: MustacheFactory = DefaultMustacheFactory()
) {
    fun generate() {
        val mustache = mustacheFactory.compile(template)

        FileWriter(File("$outputDir/index.html")).use {
            mustache.execute(it, mapOf("modules" to modules)).flush()
        }
    }
}