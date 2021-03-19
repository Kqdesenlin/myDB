sql的解析顺序遵循
1.select
2.distinct
3.from
4.join
5.on
6.where
7.group by
8.with
9.having
10.order by

当前完成状态
1.判断是哪一类dml语句(select,insert,delete,update)
1.1.如果是select,则判断from - where之间是哪一种
1.2.判断是table还是子查询