--------------------------------------------------------
--  DDL for Sequence LAB3_GROUPS_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "LAB3_GROUPS_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE
/
--------------------------------------------------------
--  DDL for Sequence LAB3_MARKS_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "LAB3_MARKS_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 101 CACHE 20 NOORDER  NOCYCLE
/
--------------------------------------------------------
--  DDL for Sequence LAB3_REG_USERS_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "LAB3_REG_USERS_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 81 CACHE 20 NOORDER  NOCYCLE
/
--------------------------------------------------------
--  DDL for Sequence LAB3_ROLES_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "LAB3_ROLES_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE
/
--------------------------------------------------------
--  DDL for Sequence LAB3_SUBJECTS_ON_GROUP_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "LAB3_SUBJECTS_ON_GROUP_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 81 CACHE 20 NOORDER  NOCYCLE
/
--------------------------------------------------------
--  DDL for Sequence LAB3_SUBJECTS_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "LAB3_SUBJECTS_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 81 CACHE 20 NOORDER  NOCYCLE
/
--------------------------------------------------------
--  DDL for Sequence LAB3_TOPICS_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "LAB3_TOPICS_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 161 CACHE 20 NOORDER  NOCYCLE
/
--------------------------------------------------------
--  DDL for Table LAB3_GROUPS
--------------------------------------------------------

CREATE TABLE "LAB3_GROUPS"
(	"GROUPID" NUMBER(*,0) NOT NULL ENABLE,
     "GROUPNAME" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     CONSTRAINT "LAB3_GROUPS_PK" PRIMARY KEY ("GROUPID")
)
/
--------------------------------------------------------
--  DDL for Table LAB3_MARKS
--------------------------------------------------------

CREATE TABLE "LAB3_MARKS"
(	"MARKID" NUMBER(*,0) NOT NULL ENABLE,
     "TOPICID" NUMBER(*,0) NOT NULL ENABLE,
     "STUDENTID" NUMBER(*,0) NOT NULL ENABLE,
     "MARK" NUMBER(*,0) NOT NULL ENABLE,
     CONSTRAINT "LAB3_MARKS_PK" PRIMARY KEY ("MARKID")
)
/
--------------------------------------------------------
--  DDL for Table LAB3_REG_USERS
--------------------------------------------------------

CREATE TABLE "LAB3_REG_USERS"
(	"USERID" NUMBER NOT NULL ENABLE,
     "EMAIL" VARCHAR2(30 BYTE) NOT NULL ENABLE,
     "PASSWORD" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     "ROLEID" NUMBER(*,0),
     CONSTRAINT "LAB3_REG_USERS_PK" PRIMARY KEY ("USERID")
)
/
--------------------------------------------------------
--  DDL for Table LAB3_ROLES
--------------------------------------------------------

CREATE TABLE "LAB3_ROLES"
(	"ROLEID" NUMBER(*,0) NOT NULL ENABLE,
     "ROLENAME" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     CONSTRAINT "LAB3_ROLES_PK" PRIMARY KEY ("ROLEID")
)
/
--------------------------------------------------------
--  DDL for Table LAB3_STUDENTS
--------------------------------------------------------

CREATE TABLE "LAB3_STUDENTS"
(	"USERID" NUMBER(*,0) NOT NULL ENABLE,
     "GROUPID" NUMBER(*,0) NOT NULL ENABLE,
     "FIRSTNAME" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     "LASTNAME" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     CONSTRAINT "LAB3_STUDENTS_PK" PRIMARY KEY ("USERID")
)
/
--------------------------------------------------------
--  DDL for Table LAB3_SUBJECTS
--------------------------------------------------------

CREATE TABLE "LAB3_SUBJECTS"
(	"SUBJECTID" NUMBER(*,0) NOT NULL ENABLE,
     "TEACHERID" NUMBER(*,0) NOT NULL ENABLE,
     "SUBJECTNAME" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     CONSTRAINT "LAB3_SUBJECTS_PK" PRIMARY KEY ("SUBJECTID")
)
/
--------------------------------------------------------
--  DDL for Table LAB3_SUBJECTS_ON_GROUP
--------------------------------------------------------

CREATE TABLE "LAB3_SUBJECTS_ON_GROUP"
(	"SUBJECTS_ON_GROPU_ID" NUMBER(*,0) NOT NULL ENABLE,
     "GROUP_ID" NUMBER(*,0) NOT NULL ENABLE,
     "SUBJECT_ID" NUMBER(*,0) NOT NULL ENABLE,
     CONSTRAINT "LAB3_SUBJECTS_ON_GROUP_PK" PRIMARY KEY ("SUBJECTS_ON_GROPU_ID")
)
/
--------------------------------------------------------
--  DDL for Table LAB3_TEACHERS
--------------------------------------------------------

CREATE TABLE "LAB3_TEACHERS"
(	"USERID" NUMBER(*,0) NOT NULL ENABLE,
     "FIRSTNAME" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     "LASTNAME" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     CONSTRAINT "LAB3_TEACHERS_PK" PRIMARY KEY ("USERID")
)
/
--------------------------------------------------------
--  DDL for Table LAB3_TOPICS
--------------------------------------------------------

CREATE TABLE "LAB3_TOPICS"
(	"TOPICID" NUMBER(*,0) NOT NULL ENABLE,
     "SUBJECTID" NUMBER(*,0) NOT NULL ENABLE,
     "INDEXNUMBER" NUMBER(*,0) NOT NULL ENABLE,
     "TOPICNAME" VARCHAR2(20 BYTE) NOT NULL ENABLE,
     CONSTRAINT "LAB3_TOPICS_PK" PRIMARY KEY ("TOPICID")
)
/
--------------------------------------------------------
--  DDL for View LAB3_USERS_INFO
--------------------------------------------------------

CREATE OR REPLACE FORCE EDITIONABLE VIEW "LAB3_USERS_INFO" ("ID", "EMAIL", "PASSWORD", "ROLENAME", "FIRSTNAME", "LASTNAME", "GROUPNAME") AS
select
    users.userid id,
    users.email email,
    users.password,
    roles.rolename,
    students.firstname || teachers.firstname firstname,
    students.lastname || teachers.lastname lastname,
    groups.groupname
from lab3_reg_users users
         inner join lab3_roles roles on users.roleid = roles.roleid
         full outer join lab3_students students on users.userid = students.userid
         full outer join lab3_teachers teachers on users.userid = teachers.userid
         full outer join lab3_groups groups on groups.groupid = students.groupid
/
--------------------------------------------------------
--  DDL for Index LAB3_GROUPS_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_GROUPS_PK" ON "LAB3_GROUPS" ("GROUPID")
/
--------------------------------------------------------
--  DDL for Index LAB3_MARKS_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_MARKS_PK" ON "LAB3_MARKS" ("MARKID")
/
--------------------------------------------------------
--  DDL for Index LAB3_REG_USERS_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_REG_USERS_PK" ON "LAB3_REG_USERS" ("USERID")
/
--------------------------------------------------------
--  DDL for Index LAB3_ROLES_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_ROLES_PK" ON "LAB3_ROLES" ("ROLEID")
/
--------------------------------------------------------
--  DDL for Index LAB3_STUDENTS_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_STUDENTS_PK" ON "LAB3_STUDENTS" ("USERID")
/
--------------------------------------------------------
--  DDL for Index LAB3_SUBJECTS_ON_GROUP_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_SUBJECTS_ON_GROUP_PK" ON "LAB3_SUBJECTS_ON_GROUP" ("SUBJECTS_ON_GROPU_ID")
/
--------------------------------------------------------
--  DDL for Index LAB3_SUBJECTS_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_SUBJECTS_PK" ON "LAB3_SUBJECTS" ("SUBJECTID")
/
--------------------------------------------------------
--  DDL for Index LAB3_TEACHERS_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_TEACHERS_PK" ON "LAB3_TEACHERS" ("USERID")
/
--------------------------------------------------------
--  DDL for Index LAB3_TOPICS_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "LAB3_TOPICS_PK" ON "LAB3_TOPICS" ("TOPICID")
/
CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_GROUPS_TRG"
    BEFORE INSERT ON "LAB3_GROUPS"
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.GROUPID IS NULL THEN
        SELECT LAB3_GROUPS_SEQ.NEXTVAL INTO :NEW.GROUPID FROM SYS.DUAL;
    END IF;
END;
/
ALTER TRIGGER "LAB3_GROUPS_TRG" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_MARKS_TRG
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_MARKS_TRG"
    BEFORE INSERT ON LAB3_MARKS
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.MARKID IS NULL THEN
        SELECT LAB3_MARKS_SEQ.NEXTVAL INTO :NEW.MARKID FROM SYS.DUAL;
    END IF;
END;
/
ALTER TRIGGER "LAB3_MARKS_TRG" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_REG_USERS_TRG
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_REG_USERS_TRG"
    BEFORE INSERT ON LAB3_REG_USERS
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.USERID IS NULL THEN
        SELECT LAB3_REG_USERS_SEQ.NEXTVAL INTO :NEW.USERID FROM SYS.DUAL;
    END IF;
END;
/
ALTER TRIGGER "LAB3_REG_USERS_TRG" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_ROLES_TRG
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_ROLES_TRG"
    BEFORE INSERT ON LAB3_ROLES
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.ROLEID IS NULL THEN
        SELECT LAB3_ROLES_SEQ.NEXTVAL INTO :NEW.ROLEID FROM SYS.DUAL;
    END IF;
END;
/
ALTER TRIGGER "LAB3_ROLES_TRG" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_STUDENT_TRG
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_STUDENT_TRG"
    BEFORE INSERT ON LAB3_STUDENTS
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.GROUPID IS NULL THEN
        SELECT LAB3_GROUPS_SEQ.NEXTVAL INTO :NEW.GROUPID FROM SYS.DUAL;
    END IF;
END;
/
ALTER TRIGGER "LAB3_STUDENT_TRG" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_SUBJECTS_ON_GROUP_TRG
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_SUBJECTS_ON_GROUP_TRG"
    BEFORE INSERT ON LAB3_SUBJECTS_ON_GROUP
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.SUBJECTS_ON_GROPU_ID IS NULL THEN
        SELECT LAB3_SUBJECTS_ON_GROUP_SEQ.NEXTVAL INTO :NEW.SUBJECTS_ON_GROPU_ID FROM SYS.DUAL;
    END IF;
END;
/
ALTER TRIGGER "LAB3_SUBJECTS_ON_GROUP_TRG" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_SUBJECTS_TRG
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_SUBJECTS_TRG"
    BEFORE INSERT ON "LAB3_SUBJECTS"
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.SUBJECTID IS NULL THEN
        SELECT LAB3_SUBJECTS_SEQ.NEXTVAL INTO :NEW.SUBJECTID FROM SYS.DUAL;
    END IF;
END;
/
ALTER TRIGGER "LAB3_SUBJECTS_TRG" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_TOPICS_TRG
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_TOPICS_TRG"
    BEFORE INSERT ON "LAB3_TOPICS"
    FOR EACH ROW
BEGIN
    IF INSERTING AND :NEW.TOPICID IS NULL THEN
        SELECT LAB3_TOPICS_SEQ.NEXTVAL INTO :NEW.TOPICID FROM SYS.DUAL;
    END IF;
END;
/
ALTER TRIGGER "LAB3_TOPICS_TRG" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_USERS_INFO_DELETE
--------------------------------------------------------

create or replace TRIGGER "LAB3_USERS_INFO_DELETE"
    INSTEAD OF DELETE ON LAB3_USERS_INFO
BEGIN
    DELETE FROM LAB3_REG_USERS WHERE userid = :OLD.ID;
END;
/
ALTER TRIGGER "LAB3_USERS_INFO_DELETE" ENABLE
/
--------------------------------------------------------
--  DDL for Trigger LAB3_USERS_INFO_INSERT
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TRIGGER "LAB3_USERS_INFO_INSERT"
    INSTEAD OF INSERT ON LAB3_USERS_INFO
DECLARE
    ROLEID INT;
    INSERTED_GROUPID INT;
    INSERTED_USERID INT;
BEGIN
    SELECT LAB3_ROLES.ROLEID INTO ROLEID FROM LAB3_ROLES WHERE  lab3_roles.rolename = :NEW.rolename;
    INSERT INTO LAB3_REG_USERS (EMAIL, PASSWORD, ROLEID) VALUES (:NEW.EMAIL, :NEW.PASSWORD, ROLEID);
    SELECT MAX(LAB3_REG_USERS.USERID) INTO INSERTED_USERID FROM LAB3_REG_USERS;
    IF :NEW.ROLENAME = 'STUDENT' THEN
        SELECT lab3_groups.groupid INTO INSERTED_GROUPID FROM LAB3_GROUPS WHERE LAB3_GROUPS.GROUPNAME = :NEW.GROUPNAME;
        INSERT INTO LAB3_STUDENTS (USERID, GROUPID, FIRSTNAME, LASTNAME) VALUES (INSERTED_USERID, INSERTED_GROUPID, :NEW.FIRSTNAME, :NEW.LASTNAME);
    ELSIF :NEW.ROLENAME = 'TEACHER' THEN
        INSERT INTO LAB3_TEACHERS (USERID, FIRSTNAME, LASTNAME) VALUES (INSERTED_USERID, :NEW.FIRSTNAME, :NEW.LASTNAME);
    END IF;
END;
/
ALTER TRIGGER "LAB3_USERS_INFO_INSERT" ENABLE

--------------------------------------------------------
--  Ref Constraints for Table LAB3_MARKS
--------------------------------------------------------

/
ALTER TABLE "LAB3_MARKS" ADD CONSTRAINT "LAB3_MARK_TOPIC_FK" FOREIGN KEY ("TOPICID")
    REFERENCES "LAB3_TOPICS" ("TOPICID") ON DELETE CASCADE ENABLE
/
ALTER TABLE "LAB3_MARKS" ADD CONSTRAINT "LAB3_MARK_STUDENT_FK" FOREIGN KEY ("STUDENTID")
    REFERENCES "LAB3_STUDENTS" ("USERID") ON DELETE CASCADE ENABLE
--------------------------------------------------------
--  Ref Constraints for Table LAB3_REG_USERS
--------------------------------------------------------

/
ALTER TABLE "LAB3_REG_USERS" ADD CONSTRAINT "LAB3_REG_USERS_FK1" FOREIGN KEY ("ROLEID")
    REFERENCES "LAB3_ROLES" ("ROLEID") ON DELETE SET NULL ENABLE
--------------------------------------------------------
--  Ref Constraints for Table LAB3_STUDENTS
--------------------------------------------------------

/
ALTER TABLE "LAB3_STUDENTS" ADD CONSTRAINT "LAB3_STUDENT_GROUP_FK" FOREIGN KEY ("GROUPID")
    REFERENCES "LAB3_GROUPS" ("GROUPID") ON DELETE SET NULL ENABLE
/
ALTER TABLE "LAB3_STUDENTS" ADD CONSTRAINT "LAB3_STUDENTS_USER_FK" FOREIGN KEY ("USERID")
    REFERENCES "LAB3_REG_USERS" ("USERID") ON DELETE CASCADE ENABLE
--------------------------------------------------------
--  Ref Constraints for Table LAB3_SUBJECTS
--------------------------------------------------------

/
ALTER TABLE "LAB3_SUBJECTS" ADD CONSTRAINT "LAB3_SUBJECT_TEACHER_FK" FOREIGN KEY ("TEACHERID")
    REFERENCES "LAB3_TEACHERS" ("USERID") ON DELETE CASCADE ENABLE
--------------------------------------------------------
--  Ref Constraints for Table LAB3_SUBJECTS_ON_GROUP
--------------------------------------------------------

/
ALTER TABLE "LAB3_SUBJECTS_ON_GROUP" ADD CONSTRAINT "LAB3_SUBJECTS_ON_GROUP_FK1" FOREIGN KEY ("GROUP_ID")
    REFERENCES "LAB3_GROUPS" ("GROUPID") ON DELETE CASCADE ENABLE
/
ALTER TABLE "LAB3_SUBJECTS_ON_GROUP" ADD CONSTRAINT "LAB3_SUBJECTS_ON_GROUP_FK2" FOREIGN KEY ("SUBJECT_ID")
    REFERENCES "LAB3_SUBJECTS" ("SUBJECTID") ON DELETE CASCADE ENABLE
--------------------------------------------------------
--  Ref Constraints for Table LAB3_TEACHERS
--------------------------------------------------------

/
ALTER TABLE "LAB3_TEACHERS" ADD CONSTRAINT "LAB3_TEACHERS_USER_FK" FOREIGN KEY ("USERID")
    REFERENCES "LAB3_REG_USERS" ("USERID") ON DELETE CASCADE ENABLE
--------------------------------------------------------
--  Ref Constraints for Table LAB3_TOPICS
--------------------------------------------------------

/
ALTER TABLE "LAB3_TOPICS" ADD CONSTRAINT "LAB3_TOPIC_SUBJECT_FK" FOREIGN KEY ("SUBJECTID")
    REFERENCES "LAB3_SUBJECTS" ("SUBJECTID") ON DELETE CASCADE ENABLE
/