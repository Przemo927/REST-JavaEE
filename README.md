# RESTful api
Aplication which allow to create own account, login, write own articles and voting to articles other users. Aplication wrote by use JPA, EJB, JMS, REST, Maven, CDI, Angular6, Bootstrap and MySQL.

To build project:
1. Fill EmailSender.properties (email, username, passowrd). It is necessary to use jms, because during registration emails are sending.
2. Set up jndi for connection to datasource on the server side and add it to the persistence.xml file.
3. Application is compatible with wildfly 10, run server on full profile.
4. For the first time build with profile devWithInstallNode. It install node.js additionally.
