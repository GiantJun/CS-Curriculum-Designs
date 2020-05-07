use ZhiYuanLuQuXiTong
go

create proc searchsubjectbyname
@subjectname char(20)
as
begin
select * from subjectview
where 专业编号=
(select sno from enrollment
where sname=@subjectname)
end;
/*按专业名查询单独专业的录取情况*/
go

create proc searchsubjectbyno
@subjectnum char(3)
as
begin
select * from subjectview
where 专业编号=@subjectnum
end;
/*按专业号查询单独专业的录取情况的存储过程*/
go
 
create proc searchsubject
@subjectnum char(20)
as
begin

select distinct  student.sname,score,ranking,province,student.subjects,enrollment.sno,snum,enrollment.sname,ageLimit
from student join enrollresult on student.sname=EnrollResult.sname
join enrollment on enrollment.sno=enrollresult.sno
where student.sname in (select sname from EnrollResult
where sno in (select sno from enrollment)) AND enrollment.sName=@subjectnum
order by sno ;

end;
/*查询每个专业录取到的学生的详细信息*/
go

create proc searchzyenroll

/*查询第几志愿录取的存储过程*/

@zy char(10)
as
begin
if(@zy='zy1')
begin 
select * from student 
where zy1 in 
(select sNo from EnrollResult
where sname=student.sname);
end
if(@zy='zy2')
begin 
select * from student 
where zy2 in 
(select sNo from EnrollResult
where sname=student.sname);
end
if(@zy='zy3')
begin 
select * from student 
where zy3 in 
(select sNo from EnrollResult
where sname=student.sname);
end
if(@zy='zy4')
begin 
select * from student 
where zy4 in 
(select sNo from EnrollResult
where sname=student.sname);
end
if(@zy='zy5')
begin 
select * from student 
where zy5 in 
(select sNo from EnrollResult
where sname=student.sname);
end
if(@zy='zy6')
begin 
select * from student 
where zy6 in 
(select sNo from EnrollResult
where sname=student.sname);
end
end;

go

create proc percentstudentnum
@percentnum int 
as
begin
select count(*) from enrollresult as 人数
where sname in 
(select sname from student 
where ranking<(770000*@percentnum/100))
end;
/* 查询全省考生前%的人数*/
go

create proc percentstudentsubject
@percentnum int 
as
select *  
from collagetaken
where 全省排位<(770000*@percentnum/100);
/* 查询全省考生前%的考生情况包括专业*/
go

create proc returnstudent
as
select distinct TOP (100) PERCENT *
from student
where sname in 
(select sname from unEnrolledResult);
/*输出被退档学生的信息*/
go

create proc enrolldirect
as
select * from student
where sname not in (select sname from relieveTable);
/* 输出不经过调剂直接被录取的学生信息*/
go

create proc returndirect
as
select * from
student
where sname in
(select sname from unEnrolledResult
where sname not in 
(select sname from relieveTable));
/*输出直接被退档学生信息*/
go

create proc returnbyrelieve
as
select * from
student
where sname in
(select sname from unEnrolledResult
where sname in 
(select sname from relieveTable));
--/输出调剂之后仍然被退档学生信息
go

create proc enrollbyrelieve
as
select * from
student
where sname in
(select sname from EnrollResult
where sname in 
(select sname from relieveTable));
--输出调剂之后被录取的学生信息
go


create proc selectcollagetaken
as
begin
select * from collagetaken
end;
--输出collagetaken视图
go

create proc selecttotaltaken
as
begin
select * from totaltaken
end;
--输出totaltaken视图
go

create proc selectsubjectview
as
begin
select * from subjectview
end;
--输出subjectview视图
go

create proc selectsubjectviewdetail
as
begin
select * from subjectviewdetail
end;
--输出subjectviewdetail视图
go

create proc selectstudentscore

@lowscore	int,
@highscore	int

as
begin
select * from collagetaken
where 分数 between @lowscore and @highscore
end;
--查询分数在某一范围内的学生信息
go

create proc selectstudentranking

@lowranking	int,
@highranking	int

as
begin
select * from collagetaken
where 全省排位 between @lowranking and @highranking
end;
--查询排名在某一范围内的学生信息
go


create proc getHighestRankBySname
@subjectName char(20)
as
begin
select 最高排位 from subjectview
where 专业编号=
(select sno from enrollment
where sname=@subjectName)
end;
/*按专业名称查询单独专业的最高排名*/

go
create proc getLowestRankBySname
@subjectName char(20)
as
begin
select 最低排位 from subjectview
where 专业编号=
(select sno from enrollment
where sname=@subjectName)
end;
/*按专业名称查询单独专业的最低排名*/

go
create proc getHighestScoreBySname
@subjectName char(20)
as
begin
select 最高分 from subjectview
where 专业编号=
(select sno from enrollment
where sname=@subjectName)
end;
/*按专业名称查询单独专业的最高分数*/

go
create proc getLowestScoreBySname
@subjectName char(20)
as
begin
select 最低分 from subjectview
where 专业编号=
(select sno from enrollment
where sname=@subjectName)
end;
/*按专业名称查询单独专业的最低分数*/

go
create proc getAverageScoreBySname
@subjectName char(20)
as
begin
select 平均分 from subjectview
where 专业编号=
(select sno from enrollment
where sname=@subjectName)
end;
/*按专业名称查询单独专业的平均分*/

go
create proc getAllAverageScore
as
begin
select 专业名称,平均分 from subjectview,collagetaken
where subjectview.专业编号=collagetaken.专业号
end;
/*查询所有专业的平均分*/

go
create proc getAllHightestScore
as
begin
select 专业名称,最高分 from subjectview,collagetaken
where subjectview.专业编号=collagetaken.专业号
end;
/*查询所有专业的最高分*/

go
create proc getAllLowestScore
as
begin
select 专业名称,最低分 from subjectview,collagetaken
where subjectview.专业编号=collagetaken.专业号
end;
/*查询所有专业的最低分*/

go
create proc getAllHighestRank
as
begin
select 专业名称,最高排位 from subjectview,collagetaken
where subjectview.专业编号=collagetaken.专业号
end;
/*查询所有专业的最高排位*/

go
create proc getAllLowestRank
as
begin
select 专业名称,最低排位 from subjectview,collagetaken
where subjectview.专业编号=collagetaken.专业号
end;
/*查询所有专业的最低排位*/

go

create proc getSchoolHighestRank
as
begin
select 最高排位 from totaltaken
end;
/*查询全校的最高省排名*/

go
create proc getSchoolLowestRank
as
begin
select 最低排位 from totaltaken
end;
/*查询全校的最低省排名*/

go
create proc getSchoolLowestScore
as
begin
select 最低分 from totaltaken
end;
/*查询全校的最低分*/

go
create proc getSchoolHighestScore
as
begin
select 最高分 from totaltaken
end;
/*查询全校的最高分*/

go
create proc getSchoolAverageScore
as
begin
select 平均分 from totaltaken
end;
/*查询全校的平均分*/

go

create proc blselect2   /*查询指定分数区间的学生信息*/

@scorel	int,
@scoreh	int

as
begin
select * from student
where score between @scorel and @scoreh
end;
go

create proc blselect   /*查询指定排名区间的学生信息*/

@rankl	int,
@rankh	int

as
begin
select * from student
where ranking between @rankl and @rankh
end;
go