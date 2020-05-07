--初始化存储过程：
use ZhiYuanLuQuXiTong;
go
create procedure init_pro
as


	create table enrollNum(
		sNo char(10),
		planNum int,
		enrolledNum int,
		LowestRanking int
	);
	insert into enrollNum(sNo,planNum,enrolledNum,LowestRanking)
		select  enrollment.sNo,enrollment.PlanNum,0,0
		from enrollment;

	create table EnrollResult(
	sname char(10) not null ,
	sNo char(10)
	);
	create table relieveTable(
	sname char(10) not null primary key,
	ranking int
	)
	create table unEnrolledResult(
	sname char(10) not null primary key
	)
go
exec init_pro

