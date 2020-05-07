
if  exists(select 1 From master.dbo.sysdatabases where name=N'ZhiYuanLuQuXiTong') 
 drop database ZhiYuanLuQuXiTong
 go

 create database ZhiYuanLuQuXiTong
on primary
(
name = 'ZhiYuanLuQuXiTong',
filename = 'E:\ZhiYuanLuQuXiTong.mdf',
size = 30MB,
filegrowth = 20%
)
Log on
(
name = 'ZhiYuanLuQuXiTong_log',
filename = 'E:\ZhiYuanLuQuXiTong_log.ldf',
size = 3MB,
filegrowth = 1MB
)