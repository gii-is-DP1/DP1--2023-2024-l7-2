-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,password,authority) VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1);

-- Three clinic owners, with password "clinic_owner"
INSERT INTO authorities(id,authority) VALUES (2,'CLINIC_OWNER');
INSERT INTO appusers(id,username,password,authority) VALUES (2,'clinicOwner1','$2a$10$t.I/C4cjUdUWzqlFlSddLeh9SbZ6d8wR7mdbeIRghT355/KRKZPAi',2);
INSERT INTO appusers(id,username,password,authority) VALUES (3,'clinicOwner2','$2a$10$t.I/C4cjUdUWzqlFlSddLeh9SbZ6d8wR7mdbeIRghT355/KRKZPAi',2);

INSERT INTO clinic_owners(id,first_name,last_name,user_id) VALUES (1, 'John', 'Doe', 2);
INSERT INTO clinic_owners(id,first_name,last_name,user_id) VALUES (2, 'Jane', 'Doe', 3);

INSERT INTO clinics(id, name, address, telephone, plan, clinic_owner) VALUES (1, 'Clinic 1', 'Av. Palmera, 26', '955684230', 'PLATINUM', 1);
INSERT INTO clinics(id, name, address, telephone, plan, clinic_owner) VALUES (2, 'Clinic 2', 'Av. Torneo, 52', '955634232', 'GOLD', 2);
INSERT INTO clinics(id, name, address, telephone, plan, clinic_owner) VALUES (3, 'Clinic 3', 'Av. Reina Mercedes, 70', '955382238', 'BASIC', 2);

-- Ten owner user, named owner1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (3,'OWNER');
INSERT INTO appusers(id,username,password,authority) VALUES (4,'owner1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (5,'owner2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (6,'owner3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (7,'owner4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (8,'owner5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (9,'owner6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (10,'owner7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (11,'owner8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (12,'owner9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (13,'owner10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (20,'owner11','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (21,'owner12','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (22,'delsanrub','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);
INSERT INTO appusers(id,username,password,authority) VALUES (23,'davvicval','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',3);

-- One vet user, named vet1 with passwor v3t
/*INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (12,'vet1','veterinarian');*/
INSERT INTO authorities(id,authority) VALUES (4,'VET');
INSERT INTO appusers(id,username,password,authority) VALUES (14,'vet1','$2a$10$aeypcHWSf4YEkDAF0d.vjOLu94aS40MBUb4rOtDncFxZdo2wpkt8.',4);
INSERT INTO appusers(id,username,password,authority) VALUES (15,'vet2','$2a$10$aeypcHWSf4YEkDAF0d.vjOLu94aS40MBUb4rOtDncFxZdo2wpkt8.',4);
INSERT INTO appusers(id,username,password,authority) VALUES (16,'vet3','$2a$10$aeypcHWSf4YEkDAF0d.vjOLu94aS40MBUb4rOtDncFxZdo2wpkt8.',4);
INSERT INTO appusers(id,username,password,authority) VALUES (17,'vet4','$2a$10$aeypcHWSf4YEkDAF0d.vjOLu94aS40MBUb4rOtDncFxZdo2wpkt8.',4);
INSERT INTO appusers(id,username,password,authority) VALUES (18,'vet5','$2a$10$aeypcHWSf4YEkDAF0d.vjOLu94aS40MBUb4rOtDncFxZdo2wpkt8.',4);
INSERT INTO appusers(id,username,password,authority) VALUES (19,'vet6','$2a$10$aeypcHWSf4YEkDAF0d.vjOLu94aS40MBUb4rOtDncFxZdo2wpkt8.',4);

INSERT INTO vets(id, first_name,last_name,city, clinic, user_id) VALUES (1, 'James', 'Carter','Sevilla', 1, 14);
INSERT INTO vets(id, first_name,last_name,city, clinic, user_id) VALUES (2, 'Helen', 'Leary','Sevilla', 1, 15);
INSERT INTO vets(id, first_name,last_name,city, clinic, user_id) VALUES (3, 'Linda', 'Douglas','Sevilla', 2, 16);
INSERT INTO vets(id, first_name,last_name,city, clinic, user_id) VALUES (4, 'Rafael', 'Ortega','Badajoz', 2, 17);
INSERT INTO vets(id, first_name,last_name,city, clinic, user_id) VALUES (5, 'Henry', 'Stevens','Badajoz', 3, 18);
INSERT INTO vets(id, first_name,last_name,city, clinic, user_id) VALUES (6, 'Sharon', 'Jenkins','Cádiz', 3, 19);

INSERT INTO specialties(id,name) VALUES (1, 'radiology');
INSERT INTO specialties(id,name) VALUES (2, 'surgery');
INSERT INTO specialties(id,name) VALUES (3, 'dentistry');

INSERT INTO vet_specialties(vet_id,specialty_id) VALUES (2, 1);
INSERT INTO vet_specialties(vet_id,specialty_id) VALUES (3, 2);
INSERT INTO vet_specialties(vet_id,specialty_id) VALUES (3, 3);
INSERT INTO vet_specialties(vet_id,specialty_id) VALUES (4, 2);
INSERT INTO vet_specialties(vet_id,specialty_id) VALUES (5, 1);

INSERT INTO types(id,name)  VALUES (1, 'cat');
INSERT INTO types(id,name)  VALUES (2, 'dog');
INSERT INTO types(id,name)  VALUES (3, 'lizard');
INSERT INTO types(id,name)  VALUES (4, 'snake');
INSERT INTO types(id,name)  VALUES (5, 'bird');
INSERT INTO types(id,name)  VALUES (6, 'hamster');
INSERT INTO types(id,name)  VALUES (7, 'turtle');

INSERT INTO	owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Sevilla', '608555103', 4, 1);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sevilla', '608555174', 5, 1);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'Sevilla', '608558763', 6, 1);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Sevilla', '608555319', 7, 2);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Sevilla', '608555765', 8, 2);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Badajoz', '608555264', 9, 2);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Badajoz', '608555538', 10, 3);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Badajoz', '608557683', 11, 3);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail','Cádiz', '685559435', 12, 3);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Cádiz', '685555487', 13, 1);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (20, 'Mario', 'Zambrano', '2334 Reina Mercedes', 'Sevilla', '695555487', 14, 3);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (21, 'Francisco', 'De Dueñas', '10 Calle Bami', 'Sevilla', '633891195', 15, 3);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (22, 'Delfin', 'Santana', 'ETSII', 'Sevilla', '123123123', 22, 3);
INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (23, 'David', 'Vicente', 'Amor', 'Sevilla', '343123456', 23, 3);


INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (14, 'Pepe', '2013-06-08', 3, 20);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (15, 'Anacardo', '2015-04-12', 5, 21);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (16, 'José', '2015-02-12', 5, 23);


INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (1, 7, '2013-01-01 13:00', 'rabies shot', 4);
INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (2, 8, '2013-01-02 15:30', 'rabies shot', 5);
INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (3, 8, '2013-01-03 9:45', 'neutered', 5);
INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (4, 7, '2013-01-04 17:30', 'spayed', 4);
INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (5, 1, '2013-01-01 13:00', 'rabies shot', 1);
INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (6, 1, '2020-01-02 15:30', 'rabies shot', 1);
INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (7, 1, '2020-01-02 15:30', 'rabies shot', 1);
INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (8, 2, '2013-01-03 9:45', 'neutered', 2);
INSERT INTO visits(id,pet_id,visit_date_time,description,vet_id) VALUES (9, 3, '2013-01-04 17:30', 'spayed', 3);

INSERT INTO consultations(id,title, is_clinic_comment,status,owner_id,pet_id,creation_date) VALUES (1, 'Consultation about vaccines', 0, 'ANSWERED', 1, 1, '2023-01-04 17:30');
INSERT INTO consultations(id,title, is_clinic_comment,status,owner_id,pet_id,creation_date) VALUES (2, 'My dog gets really nervous', 0, 'PENDING', 1, 1, '2022-01-02 19:30');
INSERT INTO consultations(id,title, is_clinic_comment,status,owner_id,pet_id,creation_date) VALUES (3, 'My cat does not eat', 0, 'PENDING', 2, 2, '2023-04-11 11:20');
INSERT INTO consultations(id,title, is_clinic_comment,status,owner_id,pet_id,creation_date) VALUES (4, 'My lovebird does not sing', 0, 'CLOSED', 2, 2, '2023-02-24 10:30');
INSERT INTO consultations(id,title, is_clinic_comment,status,owner_id,pet_id,creation_date) VALUES (5, 'My snake has layed eggs', 0, 'PENDING', 10, 12, '2023-04-11 11:20');

INSERT INTO consultation_tickets(id,description,creation_date, user_id, consultation_id) VALUES (1, 'What vaccine should my dog receive?', '2023-01-04 17:32', 4, 1);
INSERT INTO consultation_tickets(id,description,creation_date, user_id, consultation_id) VALUES (2, 'Rabies'' one.', '2023-01-04 17:36', 14, 1);
INSERT INTO consultation_tickets(id,description,creation_date, user_id, consultation_id) VALUES (3, 'My dog gets really nervous during football matches. What should I do?', '2022-01-02 19:31', 4, 2);
INSERT INTO consultation_tickets(id,description,creation_date, user_id, consultation_id) VALUES (4, 'It also happens with tennis matches.', '2022-01-02 19:33', 4, 2);
INSERT INTO consultation_tickets(id,description,creation_date, user_id, consultation_id) VALUES (5, 'My cat han''t been eating his fodder.', '2023-04-11 11:30', 5, 3);
INSERT INTO consultation_tickets(id,description,creation_date, user_id, consultation_id) VALUES (6, 'Try to give him some tuna to check if he eats that.', '2023-04-11 15:20', 15, 3);
INSERT INTO consultation_tickets(id,description,creation_date, user_id, consultation_id) VALUES (7, 'My lovebird doesn''t sing as my neighbour''s one.', '2023-02-24 12:30', 5, 4);
INSERT INTO consultation_tickets(id,description,creation_date, user_id, consultation_id) VALUES (8, 'Lovebirds do not sing.', '2023-02-24 18:30', 16, 4);

INSERT INTO achievement(id,name,description,total_iron,total_gold,total_steal,total_objects,total_matches,total_victories) 
                        VALUES (1,'sample','sample achievement',10,9,8,7,6,5);

INSERT INTO achievement(id,name,description,total_iron,total_gold,total_steal,total_objects,total_matches,total_victories) 
                        VALUES (2,'well come','You won your first match',0,0,0,0,1,1);

INSERT INTO game(id,name,code,start,finish, winner_id, round) VALUES (1,null,null,null,null,null,null),
                                                    (2, 'game1','super-secret','2023-04-11 15:20',null, 2, 1),
                                                    (3,'super cool game', null,'2023-04-11 18:20','2023-04-11 19:20', 2, null); 

INSERT INTO game(id,name,code,start,finish, winner_id, round) VALUES (1,null,null,null,null,null,null),
                                                    (2, 'game1','super-secret','2023-04-11 15:20',null, 2, 1),
                                                    (3,'super cool game', null,'2023-04-11 18:20','2023-04-11 19:20', 2, null); 


INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (1,'Iron Seam','Take 3 iron from the supply',1,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (2,'Iron Seam','Take 3 iron from the supply',2,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (3,'Iron Seam','Take 3 iron from the supply',3,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (4,'Iron Seam','Take 3 iron from the supply',4,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (5,'Iron Seam','Take 3 iron from the supply',5,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (6,'Iron Seam','Take 3 iron from the supply',6,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (7,'Gold Seam','Take 1 gold from the supply',7,False,0,1,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (8,'Iron Seam','Take 3 iron from the supply',8,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (9,'Gold Seam','Take 1 gold from the supply',9,False,0,1,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (10,'Alloy Steel','Return 3 to the iron supply then take 2 steel',1,False,-3,0,2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (11,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',2,False,0,0,0,1,False);
/*OBJETO*/
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (12,'Forge Sword','Return 3 steel to the supply then take 1 item.',3,False,0,0,-3,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (13,'Great Dragon','If undefended at the end of the round, each player must return all gold they possess to the supply.',4,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (14,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',5,False,0,0,0,1,False);                        
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (15,'Alloy Steel','Return 3 to the iron supply then take 2 steel',6,False,-3,0,2,0,False);
/*OBJETO*/
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (16,'Forge Mace','Return 2 steel and 1 gold to the supply then take 1 item.',7,False,0,-1,-2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (17,'Gold Seam','Take 1 gold from the supply',8,False,0,1,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (18,'Iron Seam','Take 3 iron from the supply',9,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (19,'Alloy Steel','Return 3 to the iron supply then take 2 steel',1,False,-3,0,2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (20,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',2,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (21,'Alloy Steel','Return 3 to the iron supply then take 2 steel',3,False,-3,0,2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (22,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',4,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (23,'Alloy Steel','Return 3 to the iron supply then take 2 steel',5,False,-3,0,2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (24,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',6,False,0,0,0,0,True);
/*OBJETO*/
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (25,'Forge Diadem','Return 1 iron, 1 steel and 1 gold to the supply then take 1 item.',7,False,-1,-1,-1,0,False);
/*OBJETO*/
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (26,'Forge Helm','Return 1 steel and 2 gold to the supply then take 1 item.',8,False,0,-2,-1,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (27,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',9,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (28,'Gold Seam','Take 1 gold from the supply',1,False,0,1,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (29,'Alloy Steel','Return 3 to the iron supply then take 2 steel',2,False,-3,0,2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (30,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',3,False,0,0,0,1,False);
/*OBJETO*/
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (31,'Forge Axe','Return 2 steel to the supply then take 1 item.',4,False,0,0,-2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (32,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',5,False,0,0,0,0,True);
/*OBJETO*/
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (33,'Forge Crown','Return 3 gold to the supply then take 1 item.',6,False,0,-3,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (34,'Iron Seam','Take 3 iron from the supply',7,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (35,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',8,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (36,'Iron Seam','Take 3 iron from the supply',9,False,3,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (37,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',1,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (38,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',2,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (39,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',3,False,0,0,0,0,True);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (40,'Gold Seam','Take 1 gold from the supply',4,False,0,1,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (41,'Gold Seam','Take 1 gold from the supply',5,False,0,1,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (42,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',6,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (43,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',7,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (44,'Alloy Steel','Return 3 to the iron supply then take 2 steel',8,False,-3,0,2,0,False);
/*OBJETO*/
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (45,'Forge Dagger','Return 1 iron and 2 steel to the supply then take 1 item.',9,False,-1,0,-2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (46,'Gold Seam','Take 1 gold from the supply',1,False,0,1,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (47,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',2,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (48,'Great Dragon','If undefended at the end of the round, each player must return all gold they possess to the supply.',3,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (49,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',4,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (50,'Gold Seam','Take 1 gold from the supply',5,False,0,1,0,0,False);
/*OBJETO*/
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (51,'Forge Armour','Return 2 steel and 1 gold to the supply then take 1 item.',6,False,0,-1,-2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (52,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',7,False,0,0,0,0,True);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (53,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',8,False,0,0,0,1,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (54,'Alloy Steel','Return 3 to the iron supply then take 2 steel',9,False,-3,0,2,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (55,'Muster an army','When resolving actions this round, treat all defend locations as if they are occupied.',10,True,0,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (56,'Hold a council','Remove the top card from each location (leaving at least one card) and shuffle them back into The Mountain.',11,True,0,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (57,'Sell an intem','Exchange 1 of your items for 5 resources of your choice.',12,True,0,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (58,'Past Glories','Name a card previously on top in any location and return it to the top of that location.',10,True,0,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (59,'Special order','Return 5 resources of your choice, including at least 1 of each type, to the supply and take 1 item.',11,True,0,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (60,'Turn back','Remove the top card from 1 location and shuffle it back into the mountain. Immediately place 1 on that location.',12,True,0,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (61,'Apprentice','Place one worker on a location occupied by the other player.',10,True,0,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (62,'Collapse the Shafts ','Remove the top card from each location and place it on the bottom of that stack.',11,True,0,0,0,0,False);
INSERT INTO card(id, name, description, position, specialCard, total_iron,total_gold,total_steal, total_medals, helpCard) 
                        VALUES (63,'Run Amok','Collect all of the cards from each location, in turn, shuffle them and return them to that location.',12,True,0,0,0,0,False);                                                