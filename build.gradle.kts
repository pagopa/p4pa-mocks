import java.util.Objects

plugins {
  java
  id("org.springframework.boot") version "3.5.3"
  id("io.spring.dependency-management") version "1.1.7"
  jacoco
  id("org.sonarqube") version "6.2.0.5505"
  id("com.github.ben-manes.versions") version "0.52.0"
  id("org.openapi.generator") version "7.13.0"
  id("org.ajoberstar.grgit") version "5.3.2"
  id("com.gorylenko.gradle-git-properties") version "2.5.0"
}

group = "it.gov.pagopa.payhub"
version = "0.0.1"
description = "p4pa-mocks"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

val springDocOpenApiVersion = "2.8.9"
val openApiToolsVersion = "0.2.6"
val javaJwtVersion = "4.5.0"
val jwksRsaVersion = "0.22.2"
val nimbusJoseJwtVersion = "10.3"
val jjwtVersion = "0.12.6"
val javaFakerVersion = "1.0.2"
val httpClientVersion = "5.5"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenApiVersion")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
  implementation("com.github.javafaker:javafaker:$javaFakerVersion") {
    exclude(group = "org.yaml", module = "snakeyaml")
  }
  implementation ("org.apache.httpcomponents.client5:httpclient5:${httpClientVersion}")

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  // validation token jwt
  implementation("com.auth0:java-jwt:$javaJwtVersion")
  implementation("com.auth0:jwks-rsa:$jwksRsaVersion")
  implementation("com.nimbusds:nimbus-jose-jwt:$nimbusJoseJwtVersion")
  implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")

  //	Testing
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.mockito:mockito-core")
  testImplementation("org.projectlombok:lombok")
}

tasks.withType<Test> {
  useJUnitPlatform()
  finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.required = true
  }
}

val projectInfo = mapOf(
  "artifactId" to project.name,
  "version" to project.version
)

tasks {
  val processResources by getting(ProcessResources::class) {
    filesMatching("**/application.yml") {
      expand(projectInfo)
    }
  }
}

configurations {
  compileClasspath {
    resolutionStrategy.activateDependencyLocking()
  }
}

tasks.compileJava {
  dependsOn("dependenciesBuild")
}

tasks.register("dependenciesBuild") {
  group = "AutomaticallyGeneratedCode"
  description = "grouping all together automatically generate code tasks"

  dependsOn(
    "openApiGenerateAnprApiC030",
    "openApiGenerateAnprApiC003",
    "openApiGenerateSilLegacyActualization",
    "openApiGenerateSilLegacyNotification",
    "openApiGenerateP4PAAUTH"
  )
}

configure<SourceSetContainer> {
  named("main") {
    java.srcDir("$projectDir/build/generated/src/main/java")
  }
}

springBoot {
  buildInfo()
  mainClass.value("it.gov.pagopa.payhub.mocks.MocksApplication")
}

var targetEnv = when (Objects.requireNonNullElse(System.getProperty("targetBranch"), grgit.branch.current().name)) {
  "uat" -> "uat"
  "main" -> "main"
  else -> "develop"
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateP4PAAUTH") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-auth/refs/heads/$targetEnv/openapi/p4pa-auth.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.auth.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.auth.dto.generated")
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "enumPropertyNaming" to "original",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  library.set("resttemplate")
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateAnprApiC030") {
  group = "openapi"
  description = "description"

  generatorName.set("spring")
  inputSpec.set("$rootDir/openapi/anpr/anprApiC030.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.payhub.anpr.C030.controller.generated")
  modelPackage.set("it.gov.pagopa.payhub.anpr.C030.model.generated")
  configOptions.set(mapOf(
    "dateLibrary" to "java8",
    "requestMappingMode" to "api_interface",
    "useSpringBoot3" to "true",
    "interfaceOnly" to "true",
    "useTags" to "true",
    "generateConstructorWithAllArgs" to "false",
    "generatedConstructorWithRequiredArgs" to "false",
    "additionalModelTypeAnnotations" to "@lombok.Data @lombok.Builder @lombok.AllArgsConstructor"
  ))
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateAnprApiC003") {
  group = "openapi"
  description = "description"

  generatorName.set("spring")
  inputSpec.set("$rootDir/openapi/anpr/anprApiC003.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.payhub.anpr.C003.controller.generated")
  modelPackage.set("it.gov.pagopa.payhub.anpr.C003.model.generated")
  configOptions.set(mapOf(
    "dateLibrary" to "java8",
    "requestMappingMode" to "api_interface",
    "useSpringBoot3" to "true",
    "interfaceOnly" to "true",
    "useTags" to "true",
    "generateConstructorWithAllArgs" to "false",
    "generatedConstructorWithRequiredArgs" to "false",
    "additionalModelTypeAnnotations" to "@lombok.Data @lombok.Builder @lombok.AllArgsConstructor"
  ))
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateSilLegacyActualization") {
  group = "openapi"
  description = "description"

  generatorName.set("spring")
  inputSpec.set("$rootDir/openapi/sil/amount-updates-legacy.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.payhub.sil.actualization.controller.legacy.generated")
  modelPackage.set("it.gov.pagopa.payhub.sil.actualization.model.legacy.generated")
  configOptions.set(mapOf(
    "dateLibrary" to "java8",
    "requestMappingMode" to "api_interface",
    "useSpringBoot3" to "true",
    "interfaceOnly" to "true",
    "useTags" to "true",
    "generateConstructorWithAllArgs" to "false",
    "generatedConstructorWithRequiredArgs" to "false",
    "additionalModelTypeAnnotations" to "@lombok.Data @lombok.Builder @lombok.AllArgsConstructor"
  ))
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateSilLegacyNotification") {
  group = "openapi"
  description = "description"

  generatorName.set("spring")
  inputSpec.set("$rootDir/openapi/sil/payment-notification-legacy.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.payhub.sil.notification.controller.legacy.generated")
  modelPackage.set("it.gov.pagopa.payhub.sil.notification.model.legacy.generated")
  configOptions.set(mapOf(
    "dateLibrary" to "java8",
    "requestMappingMode" to "api_interface",
    "useSpringBoot3" to "true",
    "interfaceOnly" to "true",
    "useTags" to "true",
    "generateConstructorWithAllArgs" to "false",
    "generatedConstructorWithRequiredArgs" to "false",
    "additionalModelTypeAnnotations" to "@lombok.Data @lombok.Builder @lombok.AllArgsConstructor"
  ))
}
