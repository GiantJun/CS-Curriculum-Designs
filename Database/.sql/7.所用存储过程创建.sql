use ZhiYuanLuQuXiTong
go

create proc searchsubjectbyname
@subjectname char(20)
as
begin
select * from subjectview
where רҵ���=
(select sno from enrollment
where sname=@subjectname)
end;
/*��רҵ����ѯ����רҵ��¼ȡ���*/
go

create proc searchsubjectbyno
@subjectnum char(3)
as
begin
select * from subjectview
where רҵ���=@subjectnum
end;
/*��רҵ�Ų�ѯ����רҵ��¼ȡ����Ĵ洢����*/
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
/*��ѯÿ��רҵ¼ȡ����ѧ������ϸ��Ϣ*/
go

create proc searchzyenroll

/*��ѯ�ڼ�־Ը¼ȡ�Ĵ洢����*/

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
select count(*) from enrollresult as ����
where sname in 
(select sname from student 
where ranking<(770000*@percentnum/100))
end;
/* ��ѯȫʡ����ǰ%������*/
go

create proc percentstudentsubject
@percentnum int 
as
select *  
from collagetaken
where ȫʡ��λ<(770000*@percentnum/100);
/* ��ѯȫʡ����ǰ%�Ŀ����������רҵ*/
go

create proc returnstudent
as
select distinct TOP (100) PERCENT *
from student
where sname in 
(select sname from unEnrolledResult);
/*������˵�ѧ������Ϣ*/
go

create proc enrolldirect
as
select * from student
where sname not in (select sname from relieveTable);
/* �������������ֱ�ӱ�¼ȡ��ѧ����Ϣ*/
go

create proc returndirect
as
select * from
student
where sname in
(select sname from unEnrolledResult
where sname not in 
(select sname from relieveTable));
/*���ֱ�ӱ��˵�ѧ����Ϣ*/
go

create proc returnbyrelieve
as
select * from
student
where sname in
(select sname from unEnrolledResult
where sname in 
(select sname from relieveTable));
--/�������֮����Ȼ���˵�ѧ����Ϣ
go

create proc enrollbyrelieve
as
select * from
student
where sname in
(select sname from EnrollResult
where sname in 
(select sname from relieveTable));
--�������֮��¼ȡ��ѧ����Ϣ
go


create proc selectcollagetaken
as
begin
select * from collagetaken
end;
--���collagetaken��ͼ
go

create proc selecttotaltaken
as
begin
select * from totaltaken
end;
--���totaltaken��ͼ
go

create proc selectsubjectview
as
begin
select * from subjectview
end;
--���subjectview��ͼ
go

create proc selectsubjectviewdetail
as
begin
select * from subjectviewdetail
end;
--���subjectviewdetail��ͼ
go

create proc selectstudentscore

@lowscore	int,
@highscore	int

as
begin
select * from collagetaken
where ���� between @lowscore and @highscore
end;
--��ѯ������ĳһ��Χ�ڵ�ѧ����Ϣ
go

create proc selectstudentranking

@lowranking	int,
@highranking	int

as
begin
select * from collagetaken
where ȫʡ��λ between @lowranking and @highranking
end;
--��ѯ������ĳһ��Χ�ڵ�ѧ����Ϣ
go


create proc getHighestRankBySname
@subjectName char(20)
as
begin
select �����λ from subjectview
where רҵ���=
(select sno from enrollment
where sname=@subjectName)
end;
/*��רҵ���Ʋ�ѯ����רҵ���������*/

go
create proc getLowestRankBySname
@subjectName char(20)
as
begin
select �����λ from subjectview
where רҵ���=
(select sno from enrollment
where sname=@subjectName)
end;
/*��רҵ���Ʋ�ѯ����רҵ���������*/

go
create proc getHighestScoreBySname
@subjectName char(20)
as
begin
select ��߷� from subjectview
where רҵ���=
(select sno from enrollment
where sname=@subjectName)
end;
/*��רҵ���Ʋ�ѯ����רҵ����߷���*/

go
create proc getLowestScoreBySname
@subjectName char(20)
as
begin
select ��ͷ� from subjectview
where רҵ���=
(select sno from enrollment
where sname=@subjectName)
end;
/*��רҵ���Ʋ�ѯ����רҵ����ͷ���*/

go
create proc getAverageScoreBySname
@subjectName char(20)
as
begin
select ƽ���� from subjectview
where רҵ���=
(select sno from enrollment
where sname=@subjectName)
end;
/*��רҵ���Ʋ�ѯ����רҵ��ƽ����*/

go
create proc getAllAverageScore
as
begin
select רҵ����,ƽ���� from subjectview,collagetaken
where subjectview.רҵ���=collagetaken.רҵ��
end;
/*��ѯ����רҵ��ƽ����*/

go
create proc getAllHightestScore
as
begin
select רҵ����,��߷� from subjectview,collagetaken
where subjectview.רҵ���=collagetaken.רҵ��
end;
/*��ѯ����רҵ����߷�*/

go
create proc getAllLowestScore
as
begin
select רҵ����,��ͷ� from subjectview,collagetaken
where subjectview.רҵ���=collagetaken.רҵ��
end;
/*��ѯ����רҵ����ͷ�*/

go
create proc getAllHighestRank
as
begin
select רҵ����,�����λ from subjectview,collagetaken
where subjectview.רҵ���=collagetaken.רҵ��
end;
/*��ѯ����רҵ�������λ*/

go
create proc getAllLowestRank
as
begin
select רҵ����,�����λ from subjectview,collagetaken
where subjectview.רҵ���=collagetaken.רҵ��
end;
/*��ѯ����רҵ�������λ*/

go

create proc getSchoolHighestRank
as
begin
select �����λ from totaltaken
end;
/*��ѯȫУ�����ʡ����*/

go
create proc getSchoolLowestRank
as
begin
select �����λ from totaltaken
end;
/*��ѯȫУ�����ʡ����*/

go
create proc getSchoolLowestScore
as
begin
select ��ͷ� from totaltaken
end;
/*��ѯȫУ����ͷ�*/

go
create proc getSchoolHighestScore
as
begin
select ��߷� from totaltaken
end;
/*��ѯȫУ����߷�*/

go
create proc getSchoolAverageScore
as
begin
select ƽ���� from totaltaken
end;
/*��ѯȫУ��ƽ����*/

go

create proc blselect2   /*��ѯָ�����������ѧ����Ϣ*/

@scorel	int,
@scoreh	int

as
begin
select * from student
where score between @scorel and @scoreh
end;
go

create proc blselect   /*��ѯָ�����������ѧ����Ϣ*/

@rankl	int,
@rankh	int

as
begin
select * from student
where ranking between @rankl and @rankh
end;
go