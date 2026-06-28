package org.ivcode.core.gradle.dokkapages

import java.nio.file.Path

data class Module (
    val name: String,
    val links: List<Path>
)