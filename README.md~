
# Healthcare Management System

Description

This is a simple command-line application for managing healthcare data using Java and JDBC. The system allows users to perform basic operations such as adding, viewing, and deleting patient records. The goal of this project is to provide a foundational understanding of how to interact with a relational database using JDBC in Java.

Features

*Add Patient*: Allows users to add new patient records to the database.\
*View All Patients*: Displays a list of all patients stored in the database.\
*Delete Patient*: Enables the deletion of patient records by their ID.\
*Exit*: Exits the application.

Requirements\
Java Development Kit (JDK) 8 or higher\
MySQL Database\
MySQL JDBC Driver

Setup Instructions
1. Clone the Repository
```
 git clone https://github.com/your-username/healthcare-management-system.git
 cd healthcare-management-system
```
2. Set Up the Database
- Create a new MySQL database named **`healthcare`**.
- Create the **`patients`** table with the following schema:
```
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL
);
```
3. Update Database Configuration
- Update the database URL, username, and password in the DatabaseConnection.java file to match your MySQL configuration.
4. Compile and Run the Application
```
javac -cp .:mysql-connector-java-8.0.26.jar Main.java
java -cp .:mysql-connector-java-8.0.26.jar Main
```
**Usage**
1. Run the application.
2. Follow the on-screen instructions to add, view, or delete patient records.

**Future Enhancements**
- Add user authentication.
- Implement appointment scheduling and management.
- Include medical records management.
- Develop a user-friendly GUI.


