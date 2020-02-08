# Page
`Page` 是 `SqlFly` 执行分页查询时的辅助类

--- 

## 所有字段

| 参数名称	| 类型		| 说明													|
| :--------	| :--------	| :--------												|
| pageNo	| int		| 当前页													|
| pageSize	| int		|  页大小												|
| start		| int		| 起始位置												|
| count		| int		|  总数据数												|
| pageCount	| int		| 可以分的总页数											|
| is_count	| Boolean	| 是否加载总数, 不加载总数的情况下可以减少一次数据库请求	|

说明：采用 `getter` & `setter` 的形式即可对字段进行赋值
