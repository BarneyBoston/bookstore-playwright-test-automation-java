# How to install bookstore app on your own

Follow this link for step by step instruction: [https://testapka.testelka.pl/apka?recordId=recUKkx3sDu1sE3Bd](https://github.com/Testelka/test-bookstore)

# Playwright API and UI bookstore test automation

This is a Java, Playwright (both UI and API) complete testing framework with dynamic data insertion from application's database.\
It is a TDD project - with TestNG implemented for tests management and clarity.\
Its purpose is to validate correctness of implemented communication between backend & frontend of application as well as user interface.

## Installation

To set up the Maven project, follow the steps below:

### Prerequisites

Ensure you have the following installed on your machine:

- **Java Development Kit (JDK)**: Version 11 or higher.
- **Apache Maven**: Version 3.6.0 or higher.

### Steps

### 1. Clone the Repository

Clone the repository to your local machine using the following command:
```bash
git clone https://github.com/BarneyBoston/bookstore-playwright-test-automation-java.git
```
Navigate to the project directory:

```bash
cd your-repository
```
### 2. Build the Project
Use Maven to build the project. Run the following command in the project root directory:

```bash
mvn clean install
```

This command will download all necessary dependencies, compile the source code, run the tests, and package the project.

### 3. Verifying the Installation
To verify that the project is set up correctly, you can run the following command to check if all tests pass:

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml
```

## Usage

If the project will be developed further, specify which test classes should be run within:

/src/test/resources/testng.xml class


## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.
