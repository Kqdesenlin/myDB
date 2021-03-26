本内容完全基于JsqlParser实现

##create table语句
由statement转换成的CreateTable对象
由三部分组成
###第一部分 Table类
包括表名，别名，表提示(hint)等信息  
不存在内部递归情况
直接解析为TableInfoEntity的tableName  
###第二部分 ColumnDefinitions
包括多列ColumnDefinition  
每一列包括  
列名(ColumnName)，数据类型(ColDataType),
数据类型参数(columnArguments),
特殊参数要求(columnSpecs)  
其中，列名，数据类型，数据类型参数无递归，且可以直接获得
因此直接添加到entity中  
而columnSpecs是一个List<String>  
针对list<String> 通过特殊参数枚举类(ColumnSpecsEnums)进行匹配，
最终以boolean属性的格式，装载入entity对象中
至此，第二部分解析完毕
###第三部分 config
包括create，table，index等特殊属性，
不存在递归，可以直接装载，但是尚未添加

##insert语句
有statement对象转换成的insert对象由三部分组成

###第一部分 Table
和createTable一样，table包括表名等信息，直接添加到entity
的tableName中

###第二部分 Columns
例子
insert into city(c1,c2,c3) values(1,2,3);
这里的(c1,c2,c3)就是columns  
columns以arraylist的形式存储，同时不具备递归的可能
因此直接添加到entity

###第三部分 Expression
expression指插入的值
而具体的表现形式   
第一种 最平常的形式 insert into city values(1,2,3);  
在解析中，表现为itemsList  
需要通过itemsList继承expression  
因此，需要用visitor来访问  
itemsList本身有4种表达形式  
1.expressionList  
绝大部分的values() 都是该类型，无论内部是一个还是多个值或者表达式  
2.select  
例子
insert into world.city(name,countrycode,district,population)
select `name`,`Code`,region,population from world.country where code = 'ABW';  
通过 select出来的结果，作为插入的值  
3.subselect  
未知  
4.namedExpressionList  
未知  
5.multiexpressionlist  
未知  
解析方案  
这里的expression，需要最终转化为值，value  
以value的形式插入到table中去，  
因此在解析时，我们需要嵌套，按顺序来获取  
如 values(1,2,(select max(t.tv) from T t where t.tv in (select 1 from temp)));  
在这个语句中，当查询当column3的时候，发现是一个子查询  
而在执行子查询的时候，发现又一个子查询  
因此需要先执行最内侧的查询语句select 1  
当获取到select 1 的时候  把select 1 的结果  
返回到t.tv in中,然后执行select max(t.tv)  
在得到结果之后，在插入  
而递归查找需要通过visitor的格式去获取  
整体如下  
查询{  
accpet(Visitor){  
  
}   
}  
VIsitor {  
  
visit(子查询){  
子查询.accpet(this);  
}  
visit(非子查询){    
如果左条件时子查询，那么先左条件查询  
left==subSelect?(left=visit(子查询)):值;  
right==subSelect?(right=visit(子查询)):值;  
返回 left条件right;  
}   
}  
核心问题在于如何在visit(子查询)之后，获取到值，然后把值赋值给left  
这个问题的难点在于两个，第一获取值，第二赋值  
由于visit返回值为void，因此无法直接返回  
考虑方法1 
void visit(查询){  
 rtnVisit(查询);  
}  
object rtnVisit(查询){
  
}  
方法2  
void visit(查询){  
rtnVIsit(查询,object)  
}  
方法3  
Map<Integer, Object> map;  

除了获取值，如何赋值的解决方法  
方法  
clu锁  
  
