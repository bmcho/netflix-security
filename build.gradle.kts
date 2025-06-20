import com.linecorp.support.project.multi.recipe.configureByLabels

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("io.freefair.lombok") version "8.4" apply false
    id("com.epages.restdocs-api-spec") version "0.19.4" apply false
    id("com.coditory.integration-test") version "1.4.0" apply false
    id("com.linecorp.build-recipe-plugin") version "1.1.1"
}

allprojects {
    group = "com.bmcho"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://maven.restlet.com") }
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    apply(plugin = "idea")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


configureByLabels("java") {
    apply(plugin = "org.gradle.java")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.coditory.integration-test")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("com.google.guava:guava-bom:${Versions.guava}")
            mavenBom("com.epages:restdocs-api-spec-bom:${Versions.restdocsApiSpec}")
            mavenBom("io.jsonwebtoken:jjwt-bom:${Versions.jwt}")
        }

        dependencies {
            dependency("org.apache.commons:commons-lang3:${Versions.apacheCommonsLang}")
            dependency("org.apache.commons:commons-collections4:${Versions.apacheCommonsCollections}")
            dependency("com.navercorp.fixturemonkey:fixture-monkey-starter:${Versions.fixtureMonkey}")
            dependency("org.mapstruct:mapstruct:${Versions.mapstruct}")
            dependency("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")
            dependency("com.fasterxml.jackson.core:jackson-databind:${Versions.jacksonCore}")

            dependency("org.junit.jupiter:junit-jupiter-api:${Versions.junit}")
            dependency("org.junit.jupiter:junit-jupiter-params:${Versions.junit}")
            dependency("org.junit.jupiter:junit-jupiter-engine:${Versions.junit}")
            dependency("org.assertj:assertj-core:${Versions.assertjCore}")
            dependency("org.mockito:mockito-junit-jupiter:${Versions.mockitoCore}")

            dependencySet("io.jsonwebtoken:${Versions.jwt}") {
                entry("jjwt-api")
                entry("jjwt-impl")
                entry("jjwt-jackson")
            }
        }
    }

    dependencies {

        val integrationImplementation by configurations
        val integrationRuntimeOnly by configurations

        implementation("com.google.guava:guava")

        implementation("org.apache.commons:commons-lang3")
        implementation("org.apache.commons:commons-collections4")
        implementation("org.mapstruct:mapstruct")

        annotationProcessor("org.mapstruct:mapstruct-processor")

        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.assertj:assertj-core")
        testImplementation("org.junit.jupiter:junit-jupiter-params")
        testImplementation("org.mockito:mockito-core")
        testImplementation("org.mockito:mockito-junit-jupiter")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

        integrationImplementation("org.junit.jupiter:junit-jupiter-api")
        integrationImplementation("org.junit.jupiter:junit-jupiter-params")
        integrationImplementation("org.assertj:assertj-core")
        integrationRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }


    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

configureByLabels("boot") {
    apply(plugin = "org.springframework.boot")

    tasks.getByName<Jar>("jar") {
        enabled = false
    }

    tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = true
        archiveClassifier.set("boot")
    }
}

configureByLabels("querydsl") {
    apply(plugin = "io.spring.dependency-management")

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("com.querydsl:querydsl-bom:${Versions.querydsl}")
        }
    }

    dependencies {

        implementation("com.querydsl:querydsl-jpa:jakarta")
        implementation("com.querydsl:querydsl-core")

        annotationProcessor("com.querydsl:querydsl-apt:jakarta")
        annotationProcessor("jakarta.persistence:jakarta.persistence-api")
        annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    }
}