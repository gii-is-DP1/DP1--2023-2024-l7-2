-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,password,authority) VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1);


-- Ten owner user, named owner1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'USER');
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (4,'owner1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (5,'owner2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (6,'owner3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (7,'owner4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (8,'owner5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (9,'owner6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (10,'owner7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (11,'owner8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (12,'owner9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (13,'owner10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (20,'owner11','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (21,'owner12','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (22,'delsanrub','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);
INSERT INTO appusers(id,username,password,authority, is_logged_in) VALUES (23,'davvicval','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2, False);



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

/* Friendes mocks */
INSERT INTO friend_request(id, receiver_id, sender_id, status) VALUES 
                (1,4,5, 2), (2,6,7,2), (3,4,8, 0);


INSERT INTO special_card(id, turned_side_id, name, description) VALUES 
                        (1, 1, 'Muster an army','When resolving actions this round, treat all defend locations as if they are occupied.'),
                        (2, 2, 'Hold a council','Remove the top card from each location (leaving at least one card) and shuffle them back into The Mountain.'),
                        (3, 3, 'Sell an item','Exchange 1 of your items for 5 resources of your choice.'),
                        (4, 4, 'Past Glories','Name a card previously on top in any location and return it to the top of that location.'),
                        (5, 5, 'Special order','Return 5 resources of your choice, including at least 1 of each type, to the supply and take 1 item.'),
                        (6, 6, 'Turn back','Remove the top card from 1 location and shuffle it back into the mountain. Immediately place 1 on that location.'),
                        (7, 7, 'Apprentice','Place one worker on a location occupied by the other player.'),
                        (8, 8, 'Collapse the Shafts','Remove the top card from each location and place it on the bottom of that stack.'),
                        (9, 9, 'Run Amok','Collect all of the cards from each location, in turn, shuffle them and return them to that location.');                                                



INSERT INTO game(id,name,code,start,finish, user_winner_id, round, is_public) VALUES (1,'test1','test1',null,null,null,null, True),
        (2, 'game1','super-secret','2023-04-11 15:20',null, 4, 1, True),
        (3,'super cool game', '222','2023-04-11 18:20','2023-04-11 19:20', 4, null, False);


INSERT INTO card_deck(id) VALUES (1),(2),(3);

INSERT INTO card_deck_cards(card_deck_id,cards_id) VALUES (1,1),(1,22),(1,33),(1,14),(1,25),(1,36),(1,37),(1,38),(1,9),(1,10),
                                                        (1,11),(1,12),(1,13),(1,24),(1,15),(1,26),(1,27),(1,28),(1,19);

INSERT INTO card_deck_cards(card_deck_id,cards_id) VALUES (2,1),(2,19),(2,33),(2,14), (3,1),(3,19),(3,46),(3,14);


INSERT INTO main_board(id, card_deck_id) VALUES (1, 1);

INSERT INTO main_board_s_cards(main_board_id,s_cards_id) VALUES (1,2),(1,4),(1,1),(1,7),(1,9),(1,8);

INSERT INTO location(id,position) VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9);

INSERT INTO location_cards(location_id,cards_id) VALUES (1,1),(1,22),(1,33),(2,14),(2,25),(3,36),(3,37),(4,38),(4,9),(5,10),
                                                        (5,11),(6,12),(6,13),(7,24),(7,15),(8,26),(8,27),(9,28),(9,19);

INSERT INTO main_board_locations(main_board_id, locations_id) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9);

INSERT INTO player(id,user_id,name,iron,gold,steal,medal,color) VALUES 
        (1,4,'owner1',0,0,0,0,'red'),
        (2,5,'owner2',6,7,8,10,'blue'),
        (3,6,'owner3',10,20,50,30,'yellow');


INSERT INTO game(id,name,player_creator_id,player_start_id,code,start,finish, user_winner_id, round, main_board_id, is_public) VALUES 
        (4,'game-test',1,2,'test-code','2023-04-11 10:20',null,5,1,1, False);


INSERT INTO player_objects(player_id, objects_id) VALUES (2,1), (3,1),(3,2),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8);

INSERT INTO game_players(game_id, players_id) VALUES (4,1),(4,2),(4,3);

INSERT INTO dwarf(id, card_id, player_id, round) VALUES 
        (1, 1, 1, 1),(2, 22, 2, 1),(3, 33, 3, 1),(4, 14, 1, 1),(5, 25, 2, 1),--(6, 36, 3, 1),
        (7, 1, 1, 2),(8, 24, 2, 2),(9, 33, 3, 2),(10, 14, 1, 2),(11, 25, 2, 2),(12, 36, 3, 2),
        (13, 1, 1, 3),(14, null, 2, 3),(15, null, 3, 3),(16, 14, 1, 3),(17, 25, 2, 3),(18, 36, 3, 3),
        (19, 1, 1, 4),(20, null, 2, 4),(21, null, 2, 4),(22, 1, 2, 4),(23, 25, 3, 4),(24, 2, 1, 4),(25, 33, 3, 4),
        (26, null, 1, 5),(27, 1, 2, 5),(28, 25, 3, 5),(29, 2, 2, 5),(30, 33, 3, 5); 

INSERT INTO game_dwarves(dwarves_id, game_id) VALUES 
        (1,4),(2,4),(3,4),(4,4),(5,4),--(6,4),
        (7,4),(8,4),(9,4),(10,4),(11,4),(12,4),
        (13,4),(14,4),(15,4),(16,4),(17,4),(18,4),
        (19,4),(20,4),(21,4),(22,4),(23,4),(24,4),(25,4),
        (26,4),(27,4),(28,4),(29,4),(30,4);