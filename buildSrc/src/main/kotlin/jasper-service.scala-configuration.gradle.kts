plugins {
    scala
}

val scalaCompilerVersion: String by project

tasks.withType<ScalaCompile>().configureEach {
    scalaCompileOptions.apply {
        targetCompatibility = "8"
    }
}

tasks.test {
    useJUnitPlatform {
        includeEngines("scalatest")
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

dependencies {
    implementation(group = "org.scala-lang", name = "scala-library", version = scalaCompilerVersion)
}
