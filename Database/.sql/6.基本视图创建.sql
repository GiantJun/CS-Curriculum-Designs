 use ZhiYuanLuQuXiTong;
 go

create view collagetaken(学生姓名,分数,全省排位,生源地址,科目类型,专业号,专业代码,专业名称,学制) 
as
select distinct TOP (100) PERCENT student.sname,score,ranking,province,student.subjects,enrollment.sno,snum,enrollment.sname,ageLimit
from student join enrollresult on student.sname=EnrollResult.sname
join enrollment on enrollment.sno=enrollresult.sno
where student.sname in (select sname from EnrollResult
where sno in (select sno from enrollment))
order by sno ;
/*创建被录取考生所有信息视图*/

go

create view totaltaken(最高排位,最高分,最低排位,最低分,平均分)
as
(select min(ranking),max(score),max(ranking),min(score),avg(score)
from student
where sname in 
(select sname from enrollresult));
/*创建全校总体录取情况的视图*/
go

create view subjectview(最高排位,最高分,最低排位,最低分,平均分,专业编号)
as
select  min(全省排位) ,max(分数),max(全省排位),min(分数) ,avg(分数),专业号
from collagetaken
group by 专业号;
/*创建视图来获取按专业排序的录取情况过度视图*/

go

create view subjectviewdetail(专业编号,专业代码,专业名称,最高排位,最高分,最低排位,最低分,平均分)
as
select  专业编号,snum,sname,最高排位,最高分,最低排位,最低分,平均分
from subjectview join enrollment
on subjectview.专业编号=enrollment.sNo;
--创建视图来获取按专业排序的录取情况
go


create view returnstudentview
as
select distinct TOP (100) PERCENT *
from student
where sname in 
(select sname from unEnrolledResult);
/*输出被退档学生的信息*/

go

create view enrolldirectview
as
select * from student
where sname not in (select sname from relieveTable);
/* 输出不经过调剂直接被录取的学生信息*/
go

create view returndirectview
as
select * from
student
where sname in
(select sname from unEnrolledResult
where sname not in 
(select sname from relieveTable));
/*输出直接被退档学生信息*/
go

create view returnbyrelieveview
as
select * from
student
where sname in
(select sname from unEnrolledResult
where sname in 
(select sname from relieveTable));
--/输出调剂之后仍然被退档学生信息
go

create view enrollbyrelieveview
as
select * from
student
where sname in
(select sname from EnrollResult
where sname in 
(select sname from relieveTable));
--输出调剂之后被录取的学生信息
go
