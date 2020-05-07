 use ZhiYuanLuQuXiTong;
 go

create view collagetaken(ѧ������,����,ȫʡ��λ,��Դ��ַ,��Ŀ����,רҵ��,רҵ����,רҵ����,ѧ��) 
as
select distinct TOP (100) PERCENT student.sname,score,ranking,province,student.subjects,enrollment.sno,snum,enrollment.sname,ageLimit
from student join enrollresult on student.sname=EnrollResult.sname
join enrollment on enrollment.sno=enrollresult.sno
where student.sname in (select sname from EnrollResult
where sno in (select sno from enrollment))
order by sno ;
/*������¼ȡ����������Ϣ��ͼ*/

go

create view totaltaken(�����λ,��߷�,�����λ,��ͷ�,ƽ����)
as
(select min(ranking),max(score),max(ranking),min(score),avg(score)
from student
where sname in 
(select sname from enrollresult));
/*����ȫУ����¼ȡ�������ͼ*/
go

create view subjectview(�����λ,��߷�,�����λ,��ͷ�,ƽ����,רҵ���)
as
select  min(ȫʡ��λ) ,max(����),max(ȫʡ��λ),min(����) ,avg(����),רҵ��
from collagetaken
group by רҵ��;
/*������ͼ����ȡ��רҵ�����¼ȡ���������ͼ*/

go

create view subjectviewdetail(רҵ���,רҵ����,רҵ����,�����λ,��߷�,�����λ,��ͷ�,ƽ����)
as
select  רҵ���,snum,sname,�����λ,��߷�,�����λ,��ͷ�,ƽ����
from subjectview join enrollment
on subjectview.רҵ���=enrollment.sNo;
--������ͼ����ȡ��רҵ�����¼ȡ���
go


create view returnstudentview
as
select distinct TOP (100) PERCENT *
from student
where sname in 
(select sname from unEnrolledResult);
/*������˵�ѧ������Ϣ*/

go

create view enrolldirectview
as
select * from student
where sname not in (select sname from relieveTable);
/* �������������ֱ�ӱ�¼ȡ��ѧ����Ϣ*/
go

create view returndirectview
as
select * from
student
where sname in
(select sname from unEnrolledResult
where sname not in 
(select sname from relieveTable));
/*���ֱ�ӱ��˵�ѧ����Ϣ*/
go

create view returnbyrelieveview
as
select * from
student
where sname in
(select sname from unEnrolledResult
where sname in 
(select sname from relieveTable));
--/�������֮����Ȼ���˵�ѧ����Ϣ
go

create view enrollbyrelieveview
as
select * from
student
where sname in
(select sname from EnrollResult
where sname in 
(select sname from relieveTable));
--�������֮��¼ȡ��ѧ����Ϣ
go
