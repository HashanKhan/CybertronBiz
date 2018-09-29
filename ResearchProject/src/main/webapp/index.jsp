<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
 
<html>
   <head>
      <title>SELECT Operation</title>
   </head>

   <body>
      <sql:setDataSource var = "snapshot" driver = "com.mysql.jdbc.Driver"
         url = "jdbc:mysql://localhost:3306/sliitattendance"
         user = "root"  password = ""/>
 
      <sql:query dataSource = "${snapshot}" var = "result">
         SELECT * from attendance;
      </sql:query>
 
      <table border = "1" width = "50%">
         <tr>
            <th>ID</th>
            <th>Subject</th>
            <th>Hall</th>
            <th>IT Number</th>
            <th>Fingerprint</th>
         </tr>
         
         <c:forEach var = "row" items = "${result.rows}">
            <tr>
               <td><c:out value = "${row.id}"/></td>
               <td><c:out value = "${row.subject}"/></td>
               <td><c:out value = "${row.hall}"/></td>
               <td><c:out value = "${row.itnumber}"/></td>
               <td><c:out value = "${row.fingerprint}"/></td>
            </tr>
         </c:forEach>
      </table>
 
   </body>
</html>