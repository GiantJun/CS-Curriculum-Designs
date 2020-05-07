create login JavaManager with password='sqlstudy',default_database=ZhiYuanLuQuXiTong
go
use ZhiYuanLuQuXiTong
create user JavaManager for login JavaManager with default_schema=dbo
go
exec sp_addrolemember 'db_owner','JavaManager'