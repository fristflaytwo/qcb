

SELECT
	a.*,c.newPrice,b.week20
FROM
	t_stock_change a,
	t_stock_ma b,
	t_stock c
WHERE
	a.`code` = b.`code`
AND a.`code` = c.`code`
AND a.create_date = '20161206'
AND b.createDate = '20161206'
AND c.createDate = '20161206'
AND c.newPrice > b.week20
ORDER BY
	a.change_type ASC;