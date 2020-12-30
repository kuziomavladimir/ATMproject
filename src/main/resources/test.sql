select * from users;
select * from cards order by user_id;

SELECT * FROM cards WHERE number = '4279600099999999'

#insert into users (user_name, surname, birthday, email) values('Georgiy', 'Tuntsov', '2001-11-05', 'qwerasdzx@mail.ru');

#insert into cards (user_id, number, pin_code, currency, balance, tryes_enter_pin) values(33, '4817760070001111', '2222', 'RUR', 80000, 3);

#update users set user_name = 'Petr', surname = 'Ivanov', birthday = '1995-05-12', email = 'azxcvvbb@yandex.ru' where id = 3;

#update cards set balance = 7000000000000 where user_id = 9;

#delete from users where user_name = 'Petr';