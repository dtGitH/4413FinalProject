# 4413FinalProject
**README for GameStopper Project**



**Project Overview:**

GameStopper is an e-commerce web application for buying and selling gaming-related products. Built using JSP, Servlets, and MySQL, the application allows users to browse, purchase, and list gaming products. This guide provides instructions to set up, configure, and deploy the project remotely using a WAR file.

**Prerequisites:**

Before running the project, ensure the following software and tools are installed and configured on your machine or server:

1. JDK 8+ for Java development.
2. Apache Tomcat 9+ to host the WAR file.
3. MySQL 8+ to store data for users, products, and transactions.
4. MySQL Workbench (optional) for GUI-based database management.

Install Required Software: Install JDK, MySQL, and Apache Tomcat.

**Database Setup:** 

Import the SQL File
To set up the database schema and seed initial data, you need to import the gamestopper.sql file. If you are using MySQL Workbench, follow these steps:
    - Open MySQL Workbench.
    - Go to Server > Data Import.
    - Select Import from Self-Contained File and choose the gamestopper.sql file.
    - Select gamestopper_db as the Default Target Schema.
    - Click Start Import to begin the process.

      User: "root"
      Password:"EECS4413" 

**Tomcat Deployment:**

- Install Tomcat: Download and extract Tomcat from Tomcat Website.
- Open Eclipse: Launch Eclipse and access the Servers tab (Window > Show View > Servers).
- Add a New Server: Click Create a new server and select Apache Tomcat v9.0.
- Set Tomcat Directory: Select the directory where Tomcat is installed and click Next.
- Add Project to Server: Right-click on Tomcat in the Servers tab, select Add and Remove..., choose GameStopper, and click Add.
- Start Tomcat: Right-click the Tomcat server and select Start.


**Import WAR File into Eclipse:**

If you have a GameStopper.war file and want to import it into Eclipse, follow these steps:

- Open Eclipse: Launch Eclipse on your system.
- Create a New Dynamic Web Project:
   - Go to File > New > Dynamic Web Project.
   - Enter the project name (e.g., GameStopper).
   - Set the target runtime to Apache Tomcat v9.0.
   - Click Finish to create the project.

- Import the WAR File:
   - Right-click on the project name in the Project Explorer.
   - Select Import.
   - Choose Web > WAR file.
   - Click Next and browse to select the GameStopper.war file.
   - Select the target location for the extracted files (e.g., the existing GameStopper project).
   - Click Finish to complete the import process.

- Verify the Import:
   - Check if all source files (JSP, HTML, Servlets, and Java files) are imported under src/main/java and src/main/webapp.
   - If any files are missing, re-import the WAR file.

- Configure the Tomcat Server:
   - Open the Servers tab at the bottom of Eclipse (if not visible, go to Window > Show View > Servers).
   - Right-click on the Tomcat v9.0 Server and select Add and Remove....
   - Add the GameStopper project to the list of configured resources.
   - Click Finish.
   - Run the Application
 
Please note, on the ecommerce web application it self, you can use this login for admin that has a preset role, and can access the admin dashboard:
 - Email: a@gmail.com
 - Password: a

Being admin is the only way you can access the admin dashboard. If you want to, you could create your own account and preset the value through sql manually. 


