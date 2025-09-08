androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.activity:activity-ktx:1.9.2")
        implementation("androidx.fragment:fragment-ktx:1.8.3")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
        implementation("junit:junit:4.13.2")
    }
}
