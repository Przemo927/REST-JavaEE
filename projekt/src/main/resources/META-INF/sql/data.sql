INSERT INTO role VALUES ('admin','admin');
INSERT INTO role VALUES('user','user');
INSERT INTO user(active,email,password,username) VALUES (1,	'przemyslaw.swietoslawski@gmail.com',	'21232f297a57a5a743894a0e4a801fc3',	'Admin');
INSERT INTO user_role VALUES ('admin','Admin');
INSERT INTO discovery(description,downVote,name,timestamp,upVote,url,user_id) VALUES  ('Silownia', 0, 'Silownia', '2017-11-29 21:06:24', 0, 'http://www.silownia.pl', 1);
