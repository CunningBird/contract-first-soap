plugins {
    id("java")
    id("maven-publish")
    id("com.yupzip.wsdl2java") version "2.3.1"
}

repositories {
    mavenCentral()
}

dependencies {
    // Test context
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
}

extra["cxfVersion"] = "3.3.2"
extra["cxfPluginVersion"] = "3.2.2"
wsdl2java {
    wsdlDir = file("$projectDir/src/main/wsdl")
    wsdlsToGenerate = listOf(
        listOf("-p", "com.cunningbird.templates.contractfirstsoap", "-autoNameResolution", "$projectDir/src/main/wsdl/HelloService.wsdl")
    )
}

sourceSets {
    test {
        java {
            srcDir("${buildDir}/generated/wsdl")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/cunningbird-lab/contract-frist-soap")
            credentials {
                username = System.getProperty("publishRegistryUsername")
                password = System.getProperty("publishRegistryPassword")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.cunningbird.templates"
            artifactId = "contract-first-soap"
            version = "1.0.0"
            from(components["java"])
        }
    }
}