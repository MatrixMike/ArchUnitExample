package com.isaacudy.archunit.example.predicates

import com.tngtech.archunit.base.DescribedPredicate.describe
import com.tngtech.archunit.core.domain.JavaClass

val isRealKotlinClass = describe<JavaClass>("is real kotlin class") {
    val kotlinClass = it.reflect().kotlin

    // Some classes generated by Kotlin, such as the "FileNameKt" classes that contain top-level functions are not
    // accessible through Kotlin reflection, and will throw if you attempt to access any properties, so we'll attempt to
    // access the visibility modifier here, and return false from the predicate if the class isn't real
    val isAccessibleThroughKotlinReflection = runCatching { kotlinClass.visibility }
        .map { true }
        .getOrElse { false }
    if (!isAccessibleThroughKotlinReflection) return@describe false
    if (it.name.contains("\$\$inlined")) return@describe false
    if (it.name.contains("LiveLiterals")) return@describe false
    return@describe true
}