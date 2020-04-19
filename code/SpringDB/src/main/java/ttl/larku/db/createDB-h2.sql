DROP TABLE STUDENT_SCHEDULEDCLASS IF EXISTS;

DROP TABLE SCHEDULEDCLASS IF EXISTS;

DROP TABLE COURSE IF EXISTS;

DROP TABLE STUDENT IF EXISTS;

CREATE USER IF NOT EXISTS LARKU SALT 'f2d97d5e5c194fe4' HASH 'bf9ac7082b79123183a1a58f3f23b3822cbedc5c1161394f43bd4d0d03237c59' ADMIN;         
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_7E7F4695_DE0A_4895_8F94_2783554FACC9 START WITH 1 BELONGS_TO_TABLE;     
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_48555558_4CCC_47C3_B0E5_463F705BAAE1 START WITH 1 BELONGS_TO_TABLE;     
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_1E1E9410_2EB3_4490_B509_D78C2389694F START WITH 1 BELONGS_TO_TABLE;     
CREATE MEMORY TABLE PUBLIC.COURSE(
    ID INTEGER DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_1E1E9410_2EB3_4490_B509_D78C2389694F) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_1E1E9410_2EB3_4490_B509_D78C2389694F,
    CODE VARCHAR(255),
    CREDITS FLOAT NOT NULL,
    TITLE VARCHAR(255)
);             
ALTER TABLE PUBLIC.COURSE ADD CONSTRAINT PUBLIC.CONSTRAINT_7 PRIMARY KEY(ID);  
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.COURSE;   
CREATE MEMORY TABLE PUBLIC.SCHEDULEDCLASS(
    ID INTEGER DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_7E7F4695_DE0A_4895_8F94_2783554FACC9) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_7E7F4695_DE0A_4895_8F94_2783554FACC9,
    ENDDATE VARCHAR(255),
    STARTDATE VARCHAR(255),
    COURSE_ID INTEGER
);   
ALTER TABLE PUBLIC.SCHEDULEDCLASS ADD CONSTRAINT PUBLIC.CONSTRAINT_A PRIMARY KEY(ID);          
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.SCHEDULEDCLASS;           
CREATE MEMORY TABLE PUBLIC.STUDENT(
    ID INTEGER DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_48555558_4CCC_47C3_B0E5_463F705BAAE1) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_48555558_4CCC_47C3_B0E5_463F705BAAE1,
    NAME VARCHAR(255),
    PHONENUMBER VARCHAR(255),
    STATUS VARCHAR(255)
);         
ALTER TABLE PUBLIC.STUDENT ADD CONSTRAINT PUBLIC.CONSTRAINT_B PRIMARY KEY(ID); 
-- 4 +/- SELECT COUNT(*) FROM PUBLIC.STUDENT;  
CREATE MEMORY TABLE PUBLIC.STUDENT_SCHEDULEDCLASS(
    STUDENTS_ID INTEGER NOT NULL,
    CLASSES_ID INTEGER NOT NULL
);        
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.STUDENT_SCHEDULEDCLASS;   
ALTER TABLE PUBLIC.STUDENT_SCHEDULEDCLASS ADD CONSTRAINT PUBLIC.UKRRVJ0XAVODFIFGWFQB7SA402W UNIQUE(STUDENTS_ID, CLASSES_ID);   
ALTER TABLE PUBLIC.SCHEDULEDCLASS ADD CONSTRAINT PUBLIC.FKCG4P6V03E9CL2JVRB8DEXTVD5 FOREIGN KEY(COURSE_ID) REFERENCES PUBLIC.COURSE(ID) NOCHECK;               
ALTER TABLE PUBLIC.STUDENT_SCHEDULEDCLASS ADD CONSTRAINT PUBLIC.FKIGKWH9YGFXS5SSFT48ECED7L9 FOREIGN KEY(STUDENTS_ID) REFERENCES PUBLIC.STUDENT(ID) NOCHECK;    
ALTER TABLE PUBLIC.STUDENT_SCHEDULEDCLASS ADD CONSTRAINT PUBLIC.FKQ4ABX6JNMBO10I6X1YFCAB5WJ FOREIGN KEY(CLASSES_ID) REFERENCES PUBLIC.SCHEDULEDCLASS(ID) NOCHECK;              


	