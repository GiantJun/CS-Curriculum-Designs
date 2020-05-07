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
		--��־Ը1
		--��ȡ־Ըһ��Ӧ��רҵ������������¼ȡ����
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

			--��ȡ��һ����¼
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		--��־Ը2
		--�ж�־Ը2�Ƿ�Ϊ��
		if @student_zy2 is NULL
		begin 
			--Ϊ�������µ�־Ը������Ҫ����
			if @student_tj='1'
			begin 
				--����Ƿ��ӵ����ģ��ͽ����������
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--������ǣ���¼ȡʧ��
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--��ȡ��һ����¼
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


			--��ȡ��һ����¼
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		--��־Ը3
		--�ж�־Ը3�Ƿ�Ϊ��
		if @student_zy3 is NULL
		begin 
			--Ϊ�������µ�־Ը������Ҫ����
			if @student_tj='1'
			begin 
				--����Ƿ��ӵ����ģ��ͽ����������
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--������ǣ���¼ȡʧ��
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--��ȡ��һ����¼
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

			--��ȡ��һ����¼
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end

		--��־Ը4
		--�ж�־Ը4�Ƿ�Ϊ��
		if @student_zy4 is NULL
		begin 
			--Ϊ�������µ�־Ը������Ҫ����
			if @student_tj='1'
			begin 
				--����Ƿ��ӵ����ģ��ͽ����������
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--������ǣ���¼ȡʧ��
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--��ȡ��һ����¼
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

			--��ȡ��һ����¼
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		
		--��־Ը5
		--�ж�־Ը5�Ƿ�Ϊ��
		if @student_zy5 is NULL
		begin 
			--Ϊ�������µ�־Ը������Ҫ����
			if @student_tj='1'
			begin 
				--����Ƿ��ӵ����ģ��ͽ����������
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--������ǣ���¼ȡʧ��
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--��ȡ��һ����¼
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

			--��ȡ��һ����¼
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end

		--��־Ը6
		--�ж�־Ը6�Ƿ�Ϊ��
		if @student_zy6 is NULL
		begin 
			--Ϊ�������µ�־Ը������Ҫ����
			if @student_tj='1'
			begin 
				--����Ƿ��ӵ����ģ��ͽ����������
				insert into relieveTable(sname,ranking)
				values(@student_name,@student_ranking);
			end
			else
			begin
				--������ǣ���¼ȡʧ��
				insert into unEnrolledResult(sname)
				values(@student_name);
			end
			--��ȡ��һ����¼
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

			--��ȡ��һ����¼
			fetch next from CustCursor
			into @student_name,@student_zy1,@student_zy2,
			@student_zy3,@student_zy4,@student_zy5,@student_zy6,
			@student_tj,@student_ranking;
			continue;
		end
		
		--����־Ը�������꣬�鿴�Ƿ���ӵ���
		if @student_tj='1'
		begin 
			--����Ƿ��ӵ����ģ��ͽ����������
			insert into relieveTable(sname,ranking)
			values(@student_name,@student_ranking);
		end
		else
		begin
			--������ǣ���¼ȡʧ��
			insert into unEnrolledResult(sname)
			values(@student_name);
		end
		--��ȡ��һ����¼
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