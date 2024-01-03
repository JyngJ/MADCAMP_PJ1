plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.tab_pj"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.tab_pj"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    val splashScreenVersion = "1.0.0"
    // Use: def instead of val if you are not using Kotlin Gradle(.kts)

    implementation("androidx.core:core-splashscreen:$splashScreenVersion")
    implementation ("com.google.code.gson:gson:2.8.6'")

    implementation ("com.github.bumptech.glide:glide:4.12.0") // Glide 라이브러리 버전은 최신 버전으로 업데이트 가능
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0") // Glide 어노테이션 프로세서 // ViewModel과 LiveData에 대한 의존성 추가
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1") // ViewModel을 사용하기 위한 액티비티 의존성 추가
    implementation ("androidx.activity:activity-ktx:1.2.3")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}