plugins {
    java
    `maven-publish`
}

val groupId: String by project
val title: String by project
val vendor: String by project

group = groupId

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

tasks.withType<Jar> {
    manifest {
        attributes(
            "Built-By" to System.getProperty("user.name"),
            "Specification-Title" to title,
            "Specification-Vendor" to vendor,
            "Specification-Version" to project.version,
            "Implementation-Title" to title,
            "Implementation-Vendor" to vendor,
            "Implementation-Version" to project.version
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("jasperService") {
            from(components["java"])
            pom {
                name.set("Riege Software International Jasper Service")
                url.set(System.getenv("PROJECT_URL"))
            }
        }
    }
    repositories {
        maven {
            url = uri(System.getenv("MAVEN_PUBLISH_URL") ?: "")
            credentials {
                username = System.getenv("MAVEN_PUBLISH_USERNAME")
                password = System.getenv("MAVEN_PUBLISH_PASSWORD")
            }
        }
    }
}

repositories {
    maven {
        url = uri("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts/")
    }
    mavenCentral()
}


dependencies {
    implementation(group = "net.sf.jasperreports", name = "jasperreports", version = "6.14.0")
}
