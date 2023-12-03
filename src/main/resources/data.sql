-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,password,authority) VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1);


-- Ten owner user, named owner1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'USER');
INSERT INTO appusers(id,username,password,authority) VALUES (4,'owner1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (5,'owner2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (6,'owner3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (7,'owner4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (8,'owner5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (9,'owner6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (10,'owner7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (11,'owner8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (12,'owner9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (13,'owner10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (20,'owner11','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (21,'owner12','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (22,'delsanrub','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (23,'davvicval','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);



INSERT INTO achievement(id,name,description,total_iron,total_gold,total_steal,total_objects,total_matches,total_victories) 
                        VALUES (1,'sample','sample achievement',10,9,8,7,6,5);

INSERT INTO achievement(id,name,description,total_iron,total_gold,total_steal,total_objects,total_matches,total_victories) 
                        VALUES (2,'well come','You won your first match',0,0,0,0,1,1);

/*CARD TYPE*/
INSERT INTO card_type(id, name) VALUES (1, 'HelpCard'),
        (2, 'OrcCard'),
        (3, 'ObjectCard'),
        (4, 'Other');

/*OBJECTS*/
INSERT INTO object(id, name) VALUES (1, 'maze'),
                                        (2, 'sword'),
                                        (3, 'axe'),
                                        (4, 'dagger'),
                                        (5, 'armour'),
                                        (6, 'helm'),
                                        (7, 'crown'),
                                        (8, 'diadem');

/*CARD*/
INSERT INTO card(id, name, description, position, card_type_id, total_iron, total_gold, total_steal, total_medals, object_id) 
                        VALUES (1,'Iron Seam','Take 3 iron from the supply',1, 4,3,0,0,0, null),
                        (2,'Iron Seam','Take 3 iron from the supply',2, 4,3,0,0,0, null),
                        (3,'Iron Seam','Take 3 iron from the supply',3, 4,3,0,0,0, null),
                        (4,'Iron Seam','Take 3 iron from the supply',4, 4,3,0,0,0, null),
                        (5,'Iron Seam','Take 3 iron from the supply',5, 4,3,0,0,0, null),
                        (6,'Iron Seam','Take 3 iron from the supply',6, 4,3,0,0,0, null),
                        (7,'Gold Seam','Take 1 gold from the supply',7, 4,0,1,0,0, null),
                        (8,'Iron Seam','Take 3 iron from the supply',8, 4,3,0,0,0, null),
                        (9,'Gold Seam','Take 1 gold from the supply',9, 4,0,1,0,0, null),
                        (10,'Alloy Steel','Return 3 to the iron supply then take 2 steel',1, 4,-3,0,2,0, null),
                        (11,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',2, 2,0,0,0,1, null),
                        (12,'Forge Sword','Return 3 steel to the supply then take 1 item.',3, 3,0,0,-3,0, 2),
                        (13,'Great Dragon','If undefended at the end of the round, each player must return all gold they possess to the supply.',4, 2,0,0,0,1, null),
                        (14,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',5, 2, 0,0,0,1, null),                       
                        (15,'Alloy Steel','Return 3 to the iron supply then take 2 steel',6, 4,-3,0,2,0, null),
                        (16,'Forge Mace','Return 2 steel and 1 gold to the supply then take 1 item.',7, 3,0,-1,-2,0, 1),
                        (17,'Gold Seam','Take 1 gold from the supply',8, 4,0,1,0,0, null),
                        (18,'Iron Seam','Take 3 iron from the supply',9, 4,3,0,0,0, null),
                        (19,'Alloy Steel','Return 3 to the iron supply then take 2 steel',1, 4,-3,0,2,0, null),
                        (20,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',2, 2,0,0,0,1, null),
                        (21,'Alloy Steel','Return 3 to the iron supply then take 2 steel',3, 4,-3,0,2,0, null),
                        (22,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',4, 2,0,0,0,1, null),
                        (23,'Alloy Steel','Return 3 to the iron supply then take 2 steel',5, 4,-3,0,2,0, null),
                        (24,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',6, 1,0,0,0,0, null),
                        (25,'Forge Diadem','Return 1 iron, 1 steel and 1 gold to the supply then take 1 item.',7, 3,-1,-1,-1,0, 8),
                        (26,'Forge Helm','Return 1 steel and 2 gold to the supply then take 1 item.',8, 3,0,-2,-1,0, 6),
                        (27,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',9, 2,0,0,0,1, null),
                        (28,'Gold Seam','Take 1 gold from the supply',1, 4,0,1,0,0, null),
                        (29,'Alloy Steel','Return 3 to the iron supply then take 2 steel',2, 4,-3,0,2,0, null),
                        (30,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',3, 2,0,0,0,1, null),
                        (31,'Forge Axe','Return 2 steel to the supply then take 1 item.',4, 3,0,0,-2,0, 3),
                        (32,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',5, 1,0,0,0,0, null),
                        (33,'Forge Crown','Return 3 gold to the supply then take 1 item.',6, 3,0,-3,0,0, 7),
                        (34,'Iron Seam','Take 3 iron from the supply',7, 4,3,0,0,0, null),
                        (35,'Dragon','If undefended at the end of the round, each player must return 1 gold to the supply.',8, 2,0,0,0,1, null),
                        (36,'Iron Seam','Take 3 iron from the supply',9, 4,3,0,0,0, null),
                        (37,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',1, 2,0,0,0,1, null),
                        (38,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',2, 2,0,0,0,1, null),
                        (39,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',3, 1,0,0,0,0, null),
                        (40,'Gold Seam','Take 1 gold from the supply',4, 4,0,1,0,0, null),
                        (41,'Gold Seam','Take 1 gold from the supply',5, 4,0,1,0,0, null),
                        (42,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',6, 2,0,0,0,1, null),
                        (43,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',7, 2,0,0,0,1, null),
                        (44,'Alloy Steel','Return 3 to the iron supply then take 2 steel',8, 4,-3,0,2,0, null),
                        (45,'Forge Dagger','Return 1 iron and 2 steel to the supply then take 1 item.',9, 3,-1,0,-2,0, 4),
                        (46,'Gold Seam','Take 1 gold from the supply',1, 4,0,1,0,0, null),
                        (47,'Sidhe','If undefended at the end of the round, each player must replace two gold they possess with two iron.',2, 2,0,0,0,1, null),
                        (48,'Great Dragon','If undefended at the end of the round, each player must return all gold they possess to the supply.',3, 2,0,0,0,1, null),
                        (49,'Orc Raiders','If undefended at the end of the round, player cannot take any recolection actions',4, 2,0,0,0,1, null),
                        (50,'Gold Seam','Take 1 gold from the supply',5, 4,0,1,0,0, null),
                        (51,'Forge Armour','Return 2 steel and 1 gold to the supply then take 1 item.',6, 3,0,-1,-2,0, 5),
                        (52,'Get Help','You may place 2 additional workers this round. If you are 1st player, pass the 1st player marker to your left.',7, 1,0,0,0,0, null),
                        (53,'Knockers','If undefended at the end of the round, each player must return 1 iron to the supply.',8, 2,0,0,0,1, null),
                        (54,'Alloy Steel','Return 3 to the iron supply then take 2 steel',9, 4,-3,0,2,0, null);

/* Friend Request status */
INSERT INTO friend_request_status(id,name) VALUES (1, 'Sent'), (2,'Accepted'), (3, 'Denied'), (4, 'Blocked');

/* Friendes mocks */
INSERT INTO friend_request(id, receiver_id, sender_id, status_id) VALUES 
                (1,4,5,1), (2,6,7,1);


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


INSERT INTO game(id,name,code,start,finish, winner_id, round) VALUES (1,null,null,null,null,null,null),
        (2, 'game1','super-secret','2023-04-11 15:20',null, 2, 1),
        (3,'super cool game', null,'2023-04-11 18:20','2023-04-11 19:20', 2, null); 

INSERT INTO card_deck(id, card_id) VALUES (1,1);

INSERT INTO card_deck_cards(card_deck_id,cards_id) VALUES (1,1),(1,22),(1,33),(1,14),(1,25),(1,36),(1,37),(1,38),(1,9),(1,10),
                                                        (1,11),(1,12),(1,13),(1,24),(1,15),(1,26),(1,27),(1,28),(1,19);


INSERT INTO special_card_deck(id,special_card_id) VALUES (1,1),(2,4),(3,7);

INSERT INTO special_card_deck_special_cards(special_card_deck_id, special_cards_id) VALUES 
                (1,1),(1,2),(1,3),(2,4),(2,5),(2,6),(3,7),(3,8),(3,9);


INSERT INTO main_board(id, card_deck_id, special_card_deck_id) VALUES (1, 1, 1);

INSERT INTO game(id,name,code,start,finish, winner_id, round, main_board_id) VALUES (4,'game-test','test-code','2023-04-11 10:20',null,null,1,1);




