# Mock Services for P4PA

This repository contains the mock implementations of the services required for the P4PA project. The mock services simulate the behavior of the actual system components and external integrations, providing a controlled environment for development, testing, and debugging without relying on the production infrastructure.

## Mocks

### ANPR

* [anprApiC003.openapi.yaml](openapi/anpr/anprApiC003.openapi.yaml)
* [anprApiC030.openapi.yaml](openapi/anpr/anprApiC030.openapi.yaml)

### SIL
* [payment-notification-legacy.yaml](openapi/sil/payment-notification-legacy.yaml)
   * `payment-notification`
      * It will require a Bearer token:
        * Signed using the key configured through the [application.yml](src/main/resources/application.yml) property `sil.notification.legacy.secret`;
        * Having header `kid` as configured through the [application.yml](src/main/resources/application.yml) property `sil.notification.legacy.kid`;
        * Having subject as configured through the [application.yml](src/main/resources/application.yml) property `sil.notification.legacy.subject`;
        * Having issuer as configured through the [application.yml](src/main/resources/application.yml) property `sil.notification.legacy.issuer`.
      * Randomly, with a probability of 16% (1/6) it will return 500 error status.
* [amount-updates-legacy.yaml](openapi/sil/amount-updates-legacy.yaml)
  * `login`
    * The password to provide is given concatenating the suffix `_PSW` to the username;
    * It will expire after the minutes configured through the [application.yml](src/main/resources/application.yml) property `sil.actualization.legacy.auth.expire-minutes`.
  * `attualizzazione`
    * It will require a bearer token obtained through the `login` API;
    * It will always return a response except when the NAV contains:
      * `SILPAID`: it will return error 004 (already paid)
      * `SILNODT`: it will return error 003 (notification date not available)
    * Pseudo-randomly, it will return also a (single) balance

### CIE
* [cie-online.openapi.json](openapi/cie/cie-online.openapi.json)
  * `getIssuerFC`
    * Returns a list of available issuers, each including its name, province code, and Fiscal Code.
    * The list contains a set of **static pre-defined issuers**:
      * Ente P4PA intermediato 2 (BG)
      * Comune di Brescia (BS)
      * Comune di Milano (MI)
    * It **dynamically includes** the "Comune di Test" (TS - `11111111111`) based on the configuration set via the `includeCieTestOrganization` API.

### Mock Configuration APIs
* [mock-configuration.openapi.yaml](openapi/mock-configuration/mock-configuration.openapi.yaml)
  * `includeCieTestOrganization`
    * This API allows for dynamic configuration of the CIE mock behavior at runtime.
   
## 📂 Repository Structure

Here is a quick overview of the files and directories included in this repository:

```plaintext
.
├── .github/            # GitHub configuration files
├── gradle/             # Gradle wrapper files
├── helm/               # Helm charts for Kubernetes deployments
├── openapi/            # OpenAPI specification files
├── src/                # Source code for the Java application
│   ├── main/
│   └── test/
├── build.gradle.kts    # Gradle build file
├── Dockerfile          # Docker build file
├── README.md           # Project documentation
├── settings.gradle.kts # Gradle settings file
└── .gitignore          # Git ignore rules
```
