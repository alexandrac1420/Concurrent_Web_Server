# Concurrent Web Server

This project implements a concurrent HTTP server in Java that serves static files from a specified directory. The server handles multiple client requests concurrently using a fixed-size thread pool.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to install the following tools and configure their dependencies:

1. **Java** (versions 7 or 8)
    ```sh
    java -version
    ```
    Should return something like:
    ```sh
    java version "1.8.0"
    Java(TM) SE Runtime Environment (build 1.8.0-b132)
    Java HotSpot(TM) 64-Bit Server VM (build 25.0-b70, mixed mode)
    ```

2. **Maven**
    - Download Maven from [here](http://maven.apache.org/download.html)
    - Follow the installation instructions [here](http://maven.apache.org/download.html#Installation)

    Verify the installation:
    ```sh
    mvn -version
    ```
    Should return something like:
    ```sh
    Apache Maven 3.2.5 (12a6b3acb947671f09b81f49094c53f426d8cea1; 2014-12-14T12:29:23-05:00)
    Maven home: /Users/dnielben/Applications/apache-maven-3.2.5
    Java version: 1.8.0, vendor: Oracle Corporation
    Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/jre
    Default locale: es_ES, platform encoding: UTF-8
    OS name: "mac os x", version: "10.10.1", arch: "x86_64", family: "mac"
    ```

3. **Git**
    - Install Git by following the instructions [here](http://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

    Verify the installation:
    ```sh
    git --version
    ```
    Should return something like:
    ```sh
    git version 2.2.1
    ```

### Installing

1. Clone the repository and navigate into the project directory:
    ```sh
    git clone https://github.com/alexandrac1420/Concurrent_Web_Server.git

    cd Concurrencia
    ```

2. Build the project:
    ```sh
    mvn package
    ```

    Should display output similar to:
    ```sh
    [INFO] --- jar:3.2.0:jar (default-jar) @ Concurrencia ---
    [INFO] Building jar: C:\Users\alexa\Downloads\Ejemplo\MeanStdDevCalculator\target\Concurrencia-1.0-SNAPSHOT.jar
    [INFO] BUILD SUCCESS
    ```

3. Run the application:
    ```sh
    java -cp target/Concurrencia-1.0-SNAPSHOT.jar edu.escuelaing.arsw.HttpServer_Exercise6Concurrent
    ```
 
## Test Report - MeanStdDevCalculator

### Author
Name: Alexandra Cortes Tovar

### Date
Date: 22/06/2024

### Summary
This report outlines the unit tests conducted for the Concurrent Web Server project. Each test aimed to validate specific functionalities and behaviors of the server under various conditions.

### Tests Conducted

1. **Test `testConcurrentRequests`**
   - **Description**: Validates the server's ability to handle concurrent requests from multiple clients.
   - **Objective**: Ensure the server manages simultaneous connections effectively using a fixed thread pool.
   - **Testing Scenario**: Clients simulate HTTP GET requests for index.html.
   - **Expected Behavior**: The server should respond with HTTP/1.1 200 OK and appropriate Content-Type headers for index.html.
   - **Verification**: Confirms that all client threads receive the correct response with the expected content.


## Design 

### Title
MeanStdDevCalculator

### Author
Alexandra Cortes Tovar

### Date
22/06/2024

### Class Diagram


### Description of Class Diagram

#### Class `MeanStdDevCalculator`
- **Description**: This class handles reading from a file and calculating the mean and standard deviation for multiple tables of numeric data.
- **Operations**:
  - `public static void main(String[] args)`: Main method that initiates the program. It reads the file, processes each table of data, and calculates the mean and standard deviation.
  - `private static void processTable(LinkedList<Double> numbers, int tableNumber)`: Private method that processes each table of data, calculating and printing the mean and standard deviation.


#### Relationships Between Classes
- `LinkedList<T>` has an aggregation relationship with `Node<T>` because it contains instances of `Node<T>`. `MeanCalculator` and `StdDevCalculator` are interfaces that define methods for calculating the mean and standard deviation respectively. These interfaces are implemented by concrete classes that provide the implementation of the calculate methods. `MeanStdDevCalculator` uses instances of `MeanCalculator` and `StdDevCalculator` to perform calculations. Additionally, `MeanStdDevCalculator` uses `LinkedList<Double>` to store and process numbers.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Git](http://git-scm.com/) - Version Control System

## Versioning

I use [GitHub](https://github.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/alexandrac1420/Concurrent_Web_Server.git).

## Authors

* **Alexandra Cortes Tovar** - [alexandrac1420](https://github.com/alexandrac1420)

## License

This project is licensed under the GNU
