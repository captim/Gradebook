--------------------------------------------------------
--  Inserting
--------------------------------------------------------
/
SET DEFINE OFF
/
Insert into LAB3_GROUPS (GROUPID,GROUPNAME) values ('1','IT-81')
/
Insert into LAB3_ROLES (ROLEID,ROLENAME) values ('1','ADMIN')
/
Insert into LAB3_ROLES (ROLEID,ROLENAME) values ('2','TEACHER')
/
Insert into LAB3_ROLES (ROLEID,ROLENAME) values ('3','STUDENT')
/
Insert into LAB3_REG_USERS (USERID,EMAIL,PASSWORD,ROLEID) values ('3','admin','admin','1')
/
Insert into LAB3_REG_USERS (USERID,EMAIL,PASSWORD,ROLEID) values ('68','alexander@gmail.com','123','21')
/
Insert into LAB3_REG_USERS (USERID,EMAIL,PASSWORD,ROLEID) values ('1','john2233@gmail.com','parolyaNet0','2')
/
Insert into LAB3_REG_USERS (USERID,EMAIL,PASSWORD,ROLEID) values ('2','timur@gmail.com','parolyaNet0','3')
/
Insert into LAB3_REG_USERS (USERID,EMAIL,PASSWORD,ROLEID) values ('41','bekker.d@gmail.com','123','3')
/
Insert into LAB3_REG_USERS (USERID,EMAIL,PASSWORD,ROLEID) values ('42','alex7743@gmail.com','123','2')
/
Insert into LAB3_STUDENTS (USERID,GROUPID,FIRSTNAME,LASTNAME) values ('68','1','Alexander','Skubko')
/
Insert into LAB3_STUDENTS (USERID,GROUPID,FIRSTNAME,LASTNAME) values ('41','1','Dima','Bekker')
/
Insert into LAB3_STUDENTS (USERID,GROUPID,FIRSTNAME,LASTNAME) values ('2','1','Timur','Dumanskiy')
/
Insert into LAB3_TEACHERS (USERID,FIRSTNAME,LASTNAME) values ('42','Stive','Jobs')
/
Insert into LAB3_TEACHERS (USERID,FIRSTNAME,LASTNAME) values ('1','John','Anderson')
/