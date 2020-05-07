use ZhiYuanLuQuXiTong
go
create procedure dispensing_pro1 
as
declare @student_name char(10),
        @student_ranking int,
        @planNum int,
		@enrolledNum int,
		@LowestRanking int,
		@sNo char(10),
		@flg int;
  declare Dispensing_Major cursor
  for
  select sNo,planNum,enrolledNum,LowestRanking
  from enrollNum
  where enrollNum.planNum>enrollNum.enrolledNum
  order by LowestRanking ASC;

  declare Dispensing_Student cursor
  for
  select sname,ranking
  from relieveTable
  order by ranking ASC;

  open Dispensing_Major
  fetch next from Dispensing_Major
  into @sNo,@planNum,@enrolledNum,@LowestRanking;

  open Dispensing_Student
  fetch next from Dispensing_student
  into @student_name,@student_ranking;

  while @@FETCH_STATUS=0
  begin
     if @planNum>@enrolledNum
	    begin 
	      insert into EnrollResult(sname,sNo)
		  values(@student_name,@sNo);

		  set @enrolledNum=@enrolledNum+1;
		  update enrollNum
		  set enrolledNum=@enrolledNum
		  where sNo=@sNo;
		  if @student_ranking>@LowestRanking
		     begin
			    update enrollNum
				set LowestRanking=@student_ranking
				where sNo=@sNo;
			  end

		  --获得下一个学生信息
		  fetch next from Dispensing_Student
		  into @student_name,@student_ranking;
		  set @flg=1;
		  continue;
		end
     else   --该专业录满,检查下一个专业
	    begin
		--获得下一个专业剩余的名额
        fetch next from Dispensing_Major
        into @sNo,@planNum,@enrolledNum,@LowestRanking;
		set @flg=2;
		continue;
		end
  end


  if @flg=2
  begin
    insert into unEnrolledResult(sname)
	values(@student_name);
	fetch next from Dispensing_Student
    into @student_name,@student_ranking;
    while @@FETCH_STATUS=0
	  begin
	    insert into unEnrolledResult(sname)
	     values(@student_name);
	    fetch next from Dispensing_Student
		  into @student_name,@student_ranking;
	  end
  end

  close Dispensing_Major
  deallocate Dispensing_Major
  close Dispensing_Student
  deallocate Dispensing_Student
go
exec dispensing_pro1