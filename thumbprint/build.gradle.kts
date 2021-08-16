object Versions {
    const val jacoco = "0.8.6"
    const val junit = "5.7.1"
    const val junitVintagePlatform = "1.7.0"
    const val material = "1.2.0"
    const val picasso = "2.8"
    const val robolectric = "4.4"
    const val thumbprintTokens = "v10.0.0"
    const val truth = "1.1.3"

    object AndroidX {
        const val constraintLayout = "2.0.0"
        const val core = "1.3.2"
        const val recyclerView = "1.1.0"
        const val testCore = "1.0.0"
    }
}

group = "com.github.thumbtack"
version = "1.1.0"

plugins {
    id("com.android.library")

    id("kotlin-android")
    id("kotlin-android-extensions")

    id("jacoco")

    id("maven-publish")

    id("io.gitlab.arturbosch.detekt")
}

repositories {
    google()
    mavenCentral()

    // jitpack required for thumbprint-tokens
    maven { setUrl("https://jitpack.io") }
}

configurations.all {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode(2)
        versionName(project.version.toString())

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.viewBinding = true

    lintOptions {
        // Allow the continuous build to go on executing after lint errors.
        isAbortOnError = false

        // Ensure we use methods from AppCompat when available
        error("AppCompatMethod")

        // We force the use of res/values instead of hardcoded strings in layouts
        error("HardcodedText")

        // We want Manifests to declare that they support RTL
        error("RtlEnabled")

        // We want to be consistent on using start/end instead of right/left
        error("RtlHardcoded")

        // We want to be consistent on symmetry
        error("RtlSymmetry")

        // We're on API 21+ anyway, and this is only relevant for older versions
        error("VectorRaster")
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

androidExtensions {
    isExperimental = true
}

extensions.getByType(JacocoPluginExtension::class).toolVersion = Versions.jacoco

tasks.withType(Test::class) {
    useJUnitPlatform()
}

dependencies {
    implementation("androidx.core:core:${Versions.AndroidX.core}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintLayout}")
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("androidx.recyclerview:recyclerview:${Versions.AndroidX.recyclerView}")
    implementation("com.squareup.picasso:picasso:${Versions.picasso}")

    api("com.github.thumbtack:thumbprint-tokens:${Versions.thumbprintTokens}")

    testImplementation("com.google.truth:truth:${Versions.truth}")

    // JUnit 5 for unit tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junit}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.junit}")

    // JUnit 4 for robolectric tests
    testImplementation("org.junit.platform:junit-platform-runner:${Versions.junitVintagePlatform}")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:${Versions.junit}")
    testImplementation("androidx.test:core:${Versions.AndroidX.testCore}")
    testImplementation("org.robolectric:robolectric:${Versions.robolectric}")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = project.group.toString()
                artifactId = "thumbprint-android"
                version = project.version.toString()

                from(components["release"])

                pom {
                    name.set("Thumbprint")
                    description.set("Assets for building high-quality, consistent user experiences at Thumbtack.")
                    url.set("https://thumbprint.design/")

                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://github.com/thumbtack/thumbprint-android/blob/main/LICENSE")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com:thumbtack/thumbprint-android.git")
                        developerConnection.set("scm:git:ssh://github.com:thumbtack/thumbprint-android.git")
                        url.set("https://github.com/thumbtack/thumbprint-android")
                    }
                }
            }
        }
    }
}
