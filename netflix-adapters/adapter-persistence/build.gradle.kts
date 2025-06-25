dependencies {
    implementation(project(":netflix-core:core-port"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // spring-context, spring-tx 포함

//    implementation("org.flywaydb:flyway-core")
//    implementation("org.flywaydb:flyway-mysql")

    runtimeOnly("com.mysql:mysql-connector-j")
}