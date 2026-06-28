package org.ivcode.core.gradle.dokkapages

import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.nio.file.Path

class DockkaPagesPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.applyPlugin()
    }

    fun Project.applyPlugin() {
        val kdocSubprojects: MutableList<Project> = mutableListOf()
        val javadocSubprojects: MutableList<Project> = mutableListOf()

        subprojects {
            val subproject = this

            pluginManager.withPlugin("org.jetbrains.dokka") {
                kdocSubprojects += subproject
            }
            pluginManager.withPlugin("org.jetbrains.dokka-javadoc") {
                javadocSubprojects += subproject
            }
        }

        tasks.register("dokkaPages") {
            group = "documentation"
            description = "Generates an index page for all Dokka documentation."

            // Depend on all Dokka tasks
            dependsOn(
                kdocSubprojects.map { it.path + ":dokkaGenerateHtml" } +
                        javadocSubprojects.map { it.path + ":dokkaGenerateJavadoc" }
            )

            doLast {
                val outputDir = layout.buildDirectory.dir("dokka-pages").get().asFile
                outputDir.deleteRecursively()
                outputDir.mkdirs()

                val modules = mutableMapOf<String, MutableList<String>>()

                kdocSubprojects.forEach { subproject ->
                    val moduleDir = outputDir.resolve(subproject.name)
                    modules.getOrPut(subproject.name) { mutableListOf() } += "html"

                    copy {
                        from(subproject.layout.buildDirectory.dir("dokka/html"))
                        into(moduleDir.resolve("html"))
                        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    }
                }

                javadocSubprojects.forEach { subproject ->
                    val moduleDir = outputDir.resolve(subproject.name)
                    modules.getOrPut(subproject.name) { mutableListOf() } += "javadoc"

                    copy {
                        from(subproject.layout.buildDirectory.dir("dokka/javadoc"))
                        into(moduleDir.resolve("javadoc"))
                        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    }
                }

                IndexGenerator(
                    modules = modules.map { (name, links) ->
                        Module(name, links.map { Path.of(name, it) })
                    },
                    outputDir = outputDir.absolutePath
                ).generate()
            }
        }
    }
}