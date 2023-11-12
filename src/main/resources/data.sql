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

INSERT INTO owners(id, first_name, last_name, address, city, telephone, user_id, clinic) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Sevilla', '608555103', 4, 1);
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

INSERT INTO achievement(id,name,description,total_iron,total_gold,total_steal,total_objects,total_matches,total_victories) 
                        VALUES (1,'sample','sample achievement',10,9,8,7,6,5);

INSERT INTO achievement(id,name,description,total_iron,total_gold,total_steal,total_objects,total_matches,total_victories) 
                        VALUES (2,'well come','You won your first match',0,0,0,0,1,1);

/*CARD TYPE*/
INSERT INTO card_type(id, name) VALUES (1, 'HelpCard'),
        (2, 'OrcCard'),
        (3, 'ObjectCard'),
        (4, 'Other');

/*CARD*/
INSERT INTO card(id, name, description, position, card_type_id, total_iron, total_gold, total_steal, total_medals) 
                        VALUES (1,'Iron Seam','Take 3 iron from the supply',1, 4,3,0,0,0),
                        (2,'Iron Seam','Take 3 iron from the supply',2, 4,3,0,0,0),
                        (3,'Iron Seam','Take 3 iron from the supply',3, 4,3,0,0,0),
                        (4,'Iron Seam','Take 3 iron from the supply',4, 4,3,0,0,0),
                        (5,'Iron Seam','Take 3 iron from the supply',5, 4,3,0,0,0),
                        (6,'Iron Seam','Take 3 iron from the supply',6, 4,3,0,0,0),
                        (7,'Gold Seam','Take 1 gold from the supply',7, 4,0,1,0,0),
                        (8,'Iron Seam','Take 3 iron from the supply',8, 4,3,0,0,0),
                        (9,'Gold Seam','Take 1 gold from the supply',9, 4,0,1,0,0),
                        (10,'Alloy Steel','Return 3 to the iron supply then take 2 steel',1, 4,-3,0,2,0),
                        (11,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',2, 2,0,0,0,1),
                        (12,'Forge Sword','Return 3 steel to the supply then take 1 item.',3, 3,0,0,-3,0),
                        (13,'Great Dragon','If undefended at the end of the round, each player must return all gold they possess to the supply.',4, 2,0,0,0,1),
                        (14,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',5, 2, 0,0,0,1),                       
                        (15,'Alloy Steel','Return 3 to the iron supply then take 2 steel',6, 4,-3,0,2,0),
                        (16,'Forge Mace','Return 2 steel and 1 gold to the supply then take 1 item.',7, 3,0,-1,-2,0),
                        (17,'Gold Seam','Take 1 gold from the supply',8, 4,0,1,0,0),
                        (18,'Iron Seam','Take 3 iron from the supply',9, 4,3,0,0,0),
                        (19,'Alloy Steel','Return 3 to the iron supply then take 2 steel',1, 4,-3,0,2,0),
                        (20,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',2, 2,0,0,0,1),
                        (21,'Alloy Steel','Return 3 to the iron supply then take 2 steel',3, 4,-3,0,2,0),
                        (22,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',4, 2,0,0,0,1),
                        (23,'Alloy Steel','Return 3 to the iron supply then take 2 steel',5, 4,-3,0,2,0),
                        (24,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',6, 1,0,0,0,0),
                        (25,'Forge Diadem','Return 1 iron, 1 steel and 1 gold to the supply then take 1 item.',7, 3,-1,-1,-1,0),
                        (26,'Forge Helm','Return 1 steel and 2 gold to the supply then take 1 item.',8, 3,0,-2,-1,0),
                        (27,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',9, 2,0,0,0,1),
                        (28,'Gold Seam','Take 1 gold from the supply',1, 4,0,1,0,0),
                        (29,'Alloy Steel','Return 3 to the iron supply then take 2 steel',2, 4,-3,0,2,0),
                        (30,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',3, 2,0,0,0,1),
                        (31,'Forge Axe','Return 2 steel to the supply then take 1 item.',4, 3,0,0,-2,0),
                        (32,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',5, 1,0,0,0,0),
                        (33,'Forge Crown','Return 3 gold to the supply then take 1 item.',6, 3,0,-3,0,0),
                        (34,'Iron Seam','Take 3 iron from the supply',7, 4,3,0,0,0),
                        (35,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',8, 2,0,0,0,1),
                        (36,'Iron Seam','Take 3 iron from the supply',9, 4,3,0,0,0),
                        (37,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',1, 2,0,0,0,1),
                        (38,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',2, 2,0,0,0,1),
                        (39,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',3, 1,0,0,0,0),
                        (40,'Gold Seam','Take 1 gold from the supply',4, 4,0,1,0,0),
                        (41,'Gold Seam','Take 1 gold from the supply',5, 4,0,1,0,0),
                        (42,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',6, 2,0,0,0,1),
                        (43,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',7, 2,0,0,0,1),
                        (44,'Alloy Steel','Return 3 to the iron supply then take 2 steel',8, 4,-3,0,2,0),
                        (45,'Forge Dagger','Return 1 iron and 2 steel to the supply then take 1 item.',9, 3,-1,0,-2,0),
                        (46,'Gold Seam','Take 1 gold from the supply',1, 4,0,1,0,0),
                        (47,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',2, 2,0,0,0,1),
                        (48,'Great Dragon','If undefended at the end of the round, each player must return all gold they possess to the supply.',3, 2,0,0,0,1),
                        (49,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',4, 2,0,0,0,1),
                        (50,'Gold Seam','Take 1 gold from the supply',5, 4,0,1,0,0),
                        (51,'Forge Armour','Return 2 steel and 1 gold to the supply then take 1 item.',6, 3,0,-1,-2,0),
                        (52,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',7, 1,0,0,0,0),
                        (53,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',8, 2,0,0,0,1),
                        (54,'Alloy Steel','Return 3 to the iron supply then take 2 steel',9, 4,-3,0,2,0);


INSERT INTO special_card(id, name, description) 
                        VALUES (1,'Muster an army','When resolving actions this round, treat all defend locations as if they are occupied.');
INSERT INTO special_card(id, name, description) 
                        VALUES (2,'Hold a council','Remove the top card from each location (leaving at least one card) and shuffle them back into The Mountain.');
INSERT INTO special_card(id, name, description) 
                        VALUES (3,'Sell an intem','Exchange 1 of your items for 5 resources of your choice.');
INSERT INTO special_card(id, name, description) 
                        VALUES (4,'Past Glories','Name a card previously on top in any location and return it to the top of that location.');
INSERT INTO special_card(id, name, description) 
                        VALUES (5,'Special order','Return 5 resources of your choice, including at least 1 of each type, to the supply and take 1 item.');
INSERT INTO special_card(id, name, description) 
                        VALUES (6,'Turn back','Remove the top card from 1 location and shuffle it back into the mountain. Immediately place 1 on that location.');
INSERT INTO special_card(id, name, description) 
                        VALUES (7,'Apprentice','Place one worker on a location occupied by the other player.');
INSERT INTO special_card(id, name, description) 
                        VALUES (8,'Collapse the Shafts ','Remove the top card from each location and place it on the bottom of that stack.');
INSERT INTO special_card(id, name, description) 
                        VALUES (9,'Run Amok','Collect all of the cards from each location, in turn, shuffle them and return them to that location.');                                                


INSERT INTO game(id,name,code,start,finish, winner_id, round, players) VALUES (1,null,null,null,null,null,null, null),
        (2, 'game1','super-secret','2023-04-11 15:20',null, 2, 1, null),
        (3,'super cool game', null,'2023-04-11 18:20','2023-04-11 19:20', 2, null, null); 

INSERT INTO card_deck(id, card_id) VALUES (1,1);

INSERT INTO card_deck_cards(card_deck_id,cards_id) VALUES (1,1),(1,22),(1,33),(1,14),(1,25),(1,36),(1,37),(1,38),(1,9),(1,10),
                                                        (1,11),(1,12),(1,13),(1,24),(1,15),(1,26),(1,27),(1,28),(1,19);


INSERT INTO special_card_deck(id,card_id) VALUES (1,1),(2,4),(3,7);

INSERT INTO special_card_deck_special_cards(special_card_deck_id, special_cards_id) VALUES 
                (1,1),(1,2),(1,3),(2,4),(2,5),(2,6),(3,7),(3,8),(3,9);


INSERT INTO main_board(id, card_deck_id) VALUES (1, 1);

INSERT INTO main_board_special_card_decks(main_board_id, special_card_decks_id) VALUES (1,1),(1,3),(1,2);

INSERT INTO game(id,name,code,start,finish, winner_id, round, main_board_id, players) VALUES (4,'game-test','test-code','2023-04-11 10:20',null,null,1,1, null);




