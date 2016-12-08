SELECT
	a.*
FROM
	t_stock_change a,
	t_stock_ma b,
	t_stock c
WHERE
	a.`code` = b.`code`
AND a.`code` = c.`code`
AND a.create_date = '20161207'
AND b.createDate = '20161207'
AND c.createDate = '20161207'
AND c.newPrice > b.day20
ORDER BY
	a.change_type ASC;



SELECT
	a.*
FROM
	t_stock_recover a,
	t_stock_ma b,
	t_stock c
WHERE
	a.`code` = b.`code`
AND a.`code` = c.`code`
AND a.create_date = '20161207'
AND b.createDate = '20161207'
AND c.createDate = '20161207'
AND c.newPrice > b.day20
ORDER BY
	a.recover_type ASC;
