USE tecwebprojeto1;

INSERT INTO User (login_name, display_name, pass_hash)
	values(
		'teste',
		'teste',
		'46070D4BF934FB0D4B06D9E2C46E346944E322444900A435D7D9A95E6D7435F5');

INSERT INTO Note (message_body, creation_date, priority_level, is_active, id_owner)
	values(
		'Primeira Note teste',
		'2001-01-01',
        0,
        true,
		1);
        
INSERT INTO Note (message_body, creation_date, expiry_date, priority_level, is_active, id_owner)
	values(
		'Segunda Note teste',
		'2002-02-02',
		'2018-11-01',
        1,
        true,
		1);