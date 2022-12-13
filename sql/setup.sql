DROP SCHEMA IF EXISTS @pingfedmysqlschema@;
DROP USER IF EXISTS @pingfedmysqluser@; 
CREATE schema @pingfedmysqlschema@;
CREATE USER '@pingfedmysqluser@'@'%' IDENTIFIED BY '@pingfedmysqluserpwd@';
GRANT ALL PRIVILEGES ON @pingfedmysqlschema@ . * TO '@pingfedmysqluser@'@'%';