#src 表示当前目录下的所有.c文件
src = $(wildcard ./*.c)
#obj 表示当前目标文件名与c文件名对应
obj = $(src:%.c=%.o)
#可执行文件名
target = main 
#编译器
CC = gcc

#用所有目标文件编译生成可执行文件
$(target) : $(obj)
	$(CC) $(obj) -o $(target)

# $<：第一个依赖文件；
# $@：目标；
# $^：所有不重复的依赖文件，以空格分开​

#生成所有的.o文件都由对应的.c文件
%.o: %.c
	$(CC) -Wall $< -c -o $@
#gcc -Wall 第一依赖文件 -c -o 目标文件.o

.PHONY:clean  
clean:  
	-rm -rf $(obj) $(target)  
#rm命令前面加了一个小减号的意思就是，也许某些文件出现问题，但不要管，继续做后面的事。