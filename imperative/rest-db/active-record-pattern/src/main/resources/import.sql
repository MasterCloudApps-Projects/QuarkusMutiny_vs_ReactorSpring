
INSERT INTO player(name, goals) VALUES ('Marcelo', 2);
INSERT INTO player(name, goals) VALUES ('Kross', 2);
INSERT INTO player(name, goals) VALUES ('Benzema', 10);
INSERT INTO player(name, goals) VALUES ('Modric', 4);

INSERT INTO player(name, goals) VALUES ('Pique', 1);
INSERT INTO player(name, goals) VALUES ('Alba', 0);
INSERT INTO player(name, goals) VALUES ('Umtiti', 4);
INSERT INTO player(name, goals) VALUES ('Fati', 4);

INSERT INTO player(name, goals) VALUES ('Suarez', 3);
INSERT INTO player(name, goals) VALUES ('Koke', 2);
INSERT INTO player(name, goals) VALUES ('Llorente', 0);
INSERT INTO player(name, goals) VALUES ('Griezmann', 6);

INSERT INTO team(name, ranking) VALUES ('Real Madrid FC', 0);
INSERT INTO team(name, ranking) VALUES ('Barcelona FC', 0);
INSERT INTO team(name, ranking) VALUES ('Club Atlético de Madrid', 0);
INSERT INTO team(name, ranking) VALUES ('Seleccion de España', 0);
INSERT INTO team(name, ranking) VALUES ('Seleccion de Francia', 0);

INSERT INTO player_team(player_id, team_id) VALUES (1, 1);
INSERT INTO player_team(player_id, team_id) VALUES (2, 1);
INSERT INTO player_team(player_id, team_id) VALUES (3, 1);
INSERT INTO player_team(player_id, team_id) VALUES (4, 1);

INSERT INTO player_team(player_id, team_id) VALUES (5, 2);
INSERT INTO player_team(player_id, team_id) VALUES (6, 2);
INSERT INTO player_team(player_id, team_id) VALUES (7, 2);
INSERT INTO player_team(player_id, team_id) VALUES (8, 2);

INSERT INTO player_team(player_id, team_id) VALUES (9, 3);
INSERT INTO player_team(player_id, team_id) VALUES (10, 3);
INSERT INTO player_team(player_id, team_id) VALUES (11, 3);
INSERT INTO player_team(player_id, team_id) VALUES (12, 3);

INSERT INTO player_team(player_id, team_id) VALUES (5, 4);
INSERT INTO player_team(player_id, team_id) VALUES (6, 4);
INSERT INTO player_team(player_id, team_id) VALUES (7, 4);

INSERT INTO player_team(player_id, team_id) VALUES (3, 5);
INSERT INTO player_team(player_id, team_id) VALUES (12, 5);

INSERT INTO stadium(name, capacity, team_id) VALUES ('Santiago Bernabéu', 10000, 1);
INSERT INTO stadium(name, capacity, team_id) VALUES ('Camp Nou', 10500, 2);
INSERT INTO stadium(name, capacity, team_id) VALUES ('Wanda Metropolitano', 11000, 3);

INSERT INTO car(licence_plate, brand, model, price, player_id) VALUES ('LSK6613', 'BMW', 'X5', 100000, 1);
INSERT INTO car(licence_plate, brand, model, price, player_id) VALUES ('7ARP967', 'Ford', 'F150', 80000, 1);
INSERT INTO car(licence_plate, brand, model, price, player_id) VALUES ('DAH4372', 'Jeep', 'Liberty Sport', 55000, 3);
INSERT INTO car(licence_plate, brand, model, price, player_id) VALUES ('RHW7961', 'Honda', 'Accord', 25000, 4);
INSERT INTO car(licence_plate, brand, model, price, player_id) VALUES ('4ENX781', 'Lexus', 'Lx 470', 88000, 5);
INSERT INTO car(licence_plate, brand, model, price, player_id) VALUES ('15953S1', 'Mercedes', 'C/K1500', 150000, 5);
