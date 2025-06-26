dependencies {
    implementation(project(":netflix-core:core-usecase"))
    implementation(project(":netflix-core:core-service"))
    implementation(project(":netflix-commons"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.data:spring-data-commons")

}