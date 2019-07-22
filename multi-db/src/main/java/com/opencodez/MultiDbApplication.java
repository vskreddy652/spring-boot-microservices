package com.opencodez;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiDbApplication.class, args);
	}
	
}

/*
 * mysql> create database localdb1;
Query OK, 1 row affected (0.03 sec)

mysql> create database localdb2;
Query OK, 1 row affected (0.00 sec)

mysql> use localdb2;
Database changed
mysql> create table tbl_mysql(MESSAGE_ID int, MESSAGE varchar(100), CREATED_DATE date);
Query OK, 0 rows affected (0.17 sec)

mysql> insert into tbl_mysql values(1,"aaaaaaaaaaaa", now());
Query OK, 1 row affected, 1 warning (0.06 sec)

mysql> insert into tbl_mysql values(2,"bbbbbbbbbbbbb", now());
Query OK, 1 row affected, 1 warning (0.07 sec)

mysql> create table tbl_orcl(MESSAGE_ID int, MESSAGE varchar(100), CREATED_DATE date);
Query OK, 0 rows affected (0.08 sec)

mysql> insert into tbl_orcl values(1,"aaaaaaaaaaaaorcl", now());
Query OK, 1 row affected, 1 warning (0.07 sec)

mysql> insert into tbl_orcl values(2,"bbbbbbbbbbbbbborcl", now());
Query OK, 1 row affected, 1 warning (0.07 sec)
*/