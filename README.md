# StarkTech-Blog-Platform-API-s
Starktech is a Platform for Techies. I have tried building a tech forum that can help them to share the knowledge. 

In order to generate the boilerplate for the project visit: 
https://start.spring.io/ 
Just select the dependencies of Spring Data JPA, PostgreSQL Driver and Spring Web.

For the server: 
In your linux systems:
Open the Terminal --> $ sudo -u postgres psql --> postgres-# \connect your_user_name (If already created a user).

If you want to create your role:  postgres=# CREATE ROLE role_name WITH LOGIN;
Use: \password role_name  (To set the password for the role)

So you have logged in as the user starktech. In order ro check your connection details, run command: \conninfo

DB_USER: starktech
DB_PASSWORD: starktech
DATABASE: informationcenter
Connect to postgres with user: \connect starktech;
Connect to database with db: \connect informationcenter;
