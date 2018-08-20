DROP TABLE IF EXISTS Supplier;
DROP TABLE IF EXISTS ProductCategory;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS ShoppingCart;

 CREATE TABLE Supplier (
   id        SERIAL  PRIMARY KEY NOT NULL,
   name      VARCHAR(255)        NOT NULL,
   description   VARCHAR(255)    NOT NULL
 );

 CREATE TABLE ProductCategory (
   id        SERIAL  PRIMARY KEY NOT NULL,
   name      VARCHAR(255)        NOT NULL,
   description   VARCHAR(255)    NOT NULL,
   department    VARCHAR(255)    NOT NULL
 );

 CREATE TABLE Product (
   id        SERIAL  PRIMARY KEY   NOT NULL,
   name      VARCHAR(255)          NOT NULL,
   description   VARCHAR(255)      NOT NULL,
   defaultPrice  DOUBLE PRECISION  NOT NULL,
   currencyString VARCHAR(255)     NOT NULL,
   supplier_id    INTEGER          NOT NULL,
   product_category_id INTEGER     NOT NULL
 );

 CREATE TABLE Users (
   id        SERIAL  PRIMARY KEY NOT NULL,
   user_name      VARCHAR(255)   NOT NULL,
   password   varchar(255)       NOT NULL,
   email   VARCHAR(255)          NOT NULL,
   billing    VARCHAR(255)       NOT NULL,
   shipping   VARCHAR(255)       NOT NULL
 );

 CREATE TABLE ShoppingCart (
   id        SERIAL  PRIMARY KEY NOT NULL,
   name      VARCHAR(255)        NOT NULL,
   description   varchar(255)    NOT NULL
 );
