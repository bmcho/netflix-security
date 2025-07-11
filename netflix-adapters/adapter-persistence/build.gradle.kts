dependencies {
    implementation(project(":netflix-core:core-port"))
    implementation(project(":netflix-core:core-domain"))
    implementation(project(":netflix-commons"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // spring-context, spring-tx 포함
    
    //DB 버전 관리
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    runtimeOnly("com.mysql:mysql-connector-j")
}