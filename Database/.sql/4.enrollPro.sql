use ZhiYuanLuQuXiTong
go
create procedure enroll_pro1 
as
	declare @student_name char(10),
	@student_zy1 char(10),
	@student_zy2 char(10),
	@student_zy3 char(10),
	@student_zy4 char(10),
	@student_zy5 char(10),
	@student_zy6 char(10),
	@student_ranking int,
	@student_tj char(2),
	@enrolledNum int,
	@LowestRanking int=0,
	@planNum int
	declare CustCursor cursor
	for
	select sname,zy1,zy2,zy3,zy4,zy5,zy6,tj,ranking
	from student
	order by ranking 

	open CustCursor
	fetch next from CustCursor
	into @student_name,@student_zy1,@student_zy2,
	@student_zy3,@student_zy4,@student_zy5,@student_zy6,
	@student_tj,@student_ranking
	while @@FETCH_STATUS = 0
	begin
		--对志愿1
		--获取志愿一对应的专业招生人数、已录取人数
		select @planNum=planNum,@enrolledNum=enrolledNum,@LowestRanking=LowestRanking
		from enrollNum
		where sNo=@student_zy1;
		if @enrolledNum<@planNum
		begin
			insert into EnrollResult(sname,sNo)
			values(@student_name,@student_zy1);
			update enrollNum
			set enrolledNum=@enrolledNum+1
			where sNo=@student_zy1;
		
			if @student_ranking>@LowestRanking
			  begin
			    update enrollNum
				set LowestRanking=@student_ranking
				where sNo=@student_zy1;
			  end

			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		--对志愿2
		--判断志愿2是否为空
		if @student_zy2 is NULL
		begin 
			--为空则以下的志愿都不需要检索
			if @student_tj='1'
			begin 
				--如果是服从调剂的，就进入调剂队列
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--如果不是，则录取失败
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		select @planNum=planNum,@enrolledNum=enrolledNum,@LowestRanking=LowestRanking
		from enrollNum
		where sNo=@student_zy2;
		if @enrolledNum<@planNum
		begin
			insert into EnrollResult(sname,sNo)
			values(@student_name,@student_zy2);
			update enrollNum
			set enrolledNum=@enrolledNum+1
			where sNo=@student_zy2;

			if @student_ranking>@LowestRanking
			  begin
			    update enrollNum
				set LowestRanking=@student_ranking
				where sNo=@student_zy2;
			  end


			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		--对志愿3
		--判断志愿3是否为空
		if @student_zy3 is NULL
		begin 
			--为空则以下的志愿都不需要检索
			if @student_tj='1'
			begin 
				--如果是服从调剂的，就进入调剂队列
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--如果不是，则录取失败
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		select @planNum=planNum,@enrolledNum=enrolledNum,@LowestRanking=LowestRanking
		from enrollNum
		where sNo=@student_zy3;
		if @enrolledNum<@planNum
		begin
			insert into EnrollResult(sname,sNo)
			values(@student_name,@student_zy3);
			update enrollNum
			set enrolledNum=@enrolledNum+1
			where sNo=@student_zy3;

	 	if @student_ranking>@LowestRanking
			  begin
			    update enrollNum
				set LowestRanking=@student_ranking
				where sNo=@student_zy3;
			  end

			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end

		--对志愿4
		--判断志愿4是否为空
		if @student_zy4 is NULL
		begin 
			--为空则以下的志愿都不需要检索
			if @student_tj='1'
			begin 
				--如果是服从调剂的，就进入调剂队列
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--如果不是，则录取失败
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		select @planNum=planNum,@enrolledNum=enrolledNum,@LowestRanking=LowestRanking
		from enrollNum
		where sNo=@student_zy4;
		if @enrolledNum<@planNum
		begin
			insert into EnrollResult(sname,sNo)
			values(@student_name,@student_zy4);
			update enrollNum
			set enrolledNum=@enrolledNum+1
			where sNo=@student_zy4;

			if @student_ranking>@LowestRanking
			  begin
			    update enrollNum
				set LowestRanking=@student_ranking
				where sNo=@student_zy4;
			  end

			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		
		--对志愿5
		--判断志愿5是否为空
		if @student_zy5 is NULL
		begin 
			--为空则以下的志愿都不需要检索
			if @student_tj='1'
			begin 
				--如果是服从调剂的，就进入调剂队列
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--如果不是，则录取失败
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		select @planNum=planNum,@enrolledNum=enrolledNum,@LowestRanking=LowestRanking
		from enrollNum
		where sNo=@student_zy5;
		if @enrolledNum<@planNum
		begin
			insert into EnrollResult(sname,sNo)
			values(@student_name,@student_zy5);
			update enrollNum
			set enrolledNum=@enrolledNum+1
			where sNo=@student_zy5;

		 if @student_ranking>@LowestRanking
			  begin
			    update enrollNum
				set LowestRanking=@student_ranking
				where sNo=@student_zy5;
			  end

			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end

		--对志愿6
		--判断志愿6是否为空
		if @student_zy6 is NULL
		begin 
			--为空则以下的志愿都不需要检索
			if @student_tj='1'
			begin 
				--如果是服从调剂的，就进入调剂队列
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--如果不是，则录取失败
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		select @planNum=planNum,@enrolledNum=enrolledNum,@LowestRanking=LowestRanking
		from enrollNum
		where sNo=@student_zy6;
		if @enrolledNum<@planNum
		begin
			insert into EnrollResult(sname,sNo)
			values(@student_name,@student_zy6);
			update enrollNum
			set enrolledNum=@enrolledNum+1
			where sNo=@student_zy6;

		  if @student_ranking>@LowestRanking
			  begin
			    update enrollNum
				set LowestRanking=@student_ranking
				where sNo=@student_zy6;
			  end

			--获取下一条记录
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		
		--所有志愿都遍历完，查看是否服从调剂
		if @student_tj='1'
		begin 
			--如果是服从调剂的，就进入调剂队列
			insert into relieveTable(sname,ranking)
			values(@student_name,@student_ranking);
		end
		else
		begin
			--如果不是，则录取失败
			insert into unEnrolledResult(sname)
			values(@student_name);
		end
		--获取下一条记录
		fetch next from CustCursor
		into @student_name,@student_zy1,@student_zy2,
		@student_zy3,@student_zy4,@student_zy5,@student_zy6,
		@student_tj,@student_ranking;
		continue;
	end
	close CustCursor
	deallocate CustCursor
go
exec enroll_pro1