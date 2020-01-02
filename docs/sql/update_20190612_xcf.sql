//设置数据库模式(在严格模式下,不要让GROUP BY部分中的查询指向未选择的列,否则报错;所以将严格模式中group by相关限制取消)
SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';