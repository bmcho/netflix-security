dependencies {
    implementation(project(":netflix-core:core-usecase"))
    implementation(project(":netflix-core:core-port"))
    implementation(project(":netflix-adapters:adapter-http"))
    implementation(project(":netflix-adapters:adapter-persistence"))
    implementation(project(":netflix-adapters:adapter-redis"))
    implementation(project(":netflix-commons"))

    implementation("org.springframework:spring-context")

    implementation("io.jsonwebtoken:jjwt-api")
    implementation("io.jsonwebtoken:jjwt-impl")
    implementation("io.jsonwebtoken:jjwt-jackson")

}