# --- !Ups

CREATE TABLE "university"("id" SERIAL PRIMARY KEY ,"name" varchar(200) , "location" varchar(200));
INSERT INTO "university" values (1,'HCU', 'Hyderabad');
INSERT INTO "university" values (2,'BHU', 'Banaras');
INSERT INTO "university" values (3,'PUNE University', 'Pune');

CREATE TABLE "student"("id" SERIAL PRIMARY KEY ,"name" varchar(200) , "email" varchar(200)  ,"university_id" int );
INSERT INTO "student" values (1,'Bob', 'bob@xyz.com',3);
INSERT INTO "student" values (2,'Rob', 'rob@abc.com',2);
INSERT INTO "student" values (3,'Joe', 'joe@xyz.com',1);

CREATE TABLE "user"("id" SERIAL PRIMARY KEY ,"fname" varchar(200),"lname" varchar(200) ,"username" varchar(200), "email" varchar(200),password varchar(200)  ,"created_date" DATE );
INSERT INTO "user" values (1,'John','Snow','jSnow11','john@xyz.com','pass','1996-06-05');
INSERT INTO "user" values (2,'Jammie','Lanister','jlanister','jimmy@xyz.com','pass','1997-06-05');
INSERT INTO "user" values (3,'Jorah','Mormant','mormantJorah','mormant@xyz.com','pass','1998-06-05');

# --- !Downs

DROP TABLE "student";
DROP TABLE "university";
DROP TABLE "user";
