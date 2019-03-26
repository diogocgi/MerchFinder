/**
 * Author:  diogo
 * Created: Mar 10, 2019
 */

SHOW databases;

CREATE DATABASE merchfinderdb;

USE merchfinderdb;

SHOW tables;

CREATE USER 'im_user'@'localhost' IDENTIFIED BY 'pass';

GRANT ALL PRIVILEGES ON *.* TO 'im_user'@'%' IDENTIFIED BY 'pass' WITH GRANT OPTION;

CREATE TABLE Products_searches (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    search_text VARCHAR(200) NOT NULL,
    pname VARCHAR(80) NOT NULL,
    plink VARCHAR(200) NOT NULL,
    pimage_link VARCHAR(200) NOT NULL,
    price DECIMAL(10, 3) NOT NULL,
    currency VARCHAR(5) NOT NULL
);

SHOW tables;

SELECT pname FROM Products_searches;

SELECT pname,search_text,plink,pimage_link,price,currency FROM Products_searches;

SELECT pname,search_text,plink,pimage_link,price,currency 
	FROM Products_searches
	WHERE id=1;
	
SELECT ps.pname,ps.plink,ps.pimage_link,ps.price,ps.currency FROM Products_searches AS ps WHERE search_text='yuy';


















