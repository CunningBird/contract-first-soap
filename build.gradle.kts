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

val wsdlPath = "$projectDir/src/main/resources/wsdl/"

extra["cxfVersion"] = "3.3.2"
extra["cxfPluginVersion"] = "3.2.2"
wsdl2java {
    wsdlDir = file(wsdlPath)
    wsdlsToGenerate = listOf(
        listOf("-p", "com.cunningbird.contractfirst.wsdl.contract", "-autoNameResolution", "$wsdlPath/PetstoreService.wsdl")
    )
}

sourceSets {
    main {
        resources
    }
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
            url = uri("https://maven.pkg.github.com/cunningbird/contract-first-wsdl")
            credentials {
                username = System.getProperty("publishRegistryUsername")
                password = System.getProperty("publishRegistryPassword")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.cunningbird.contractfirst.wsdl"
            artifactId = "contract"
            version = "1.0.0"
            from(components["java"])
        }
    }
}