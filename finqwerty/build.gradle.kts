plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

val generatedLayoutsResDir = file("${layout.buildDirectory}/generated/layouts")
val generatedLayoutsRawDir = file("$generatedLayoutsResDir/raw")

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "de.fjdrjr.titanqwerty"
    compileSdk = libs.versions.compileSdk.get()
        .toInt()

    defaultConfig {
        applicationId = "de.fjdrjr.titanqwerty"
        minSdk = libs.versions.minSdk.get()
            .toInt()
        targetSdk = libs.versions.targetSdk.get()
            .toInt()
        versionCode = libs.versions.appVersionCode.get()
            .toInt()
        versionName = "1.0.1"
    }

    sourceSets.getByName("main") { // sourceSets.main 대신 getByName("main") 사용 권장
        res.srcDirs(generatedLayoutsResDir) // '+=' 대신 srcDirs() 메서드 사용
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// 커스텀 작업 정의
tasks.register<Exec>("generateLayouts") { // tasks.register<Type>("taskName") 사용
    commandLine("python3", "$projectDir/../generate_layouts.py", generatedLayoutsRawDir.absolutePath) // 문자열 보간 및 File 객체 사용
}

// preBuild 작업에 의존성 추가
tasks.named("preBuild") { // tasks.named("taskName") 사용
    dependsOn(tasks.named("generateLayouts"))
}
