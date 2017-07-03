/* ------------------------------------
  INSERT - USERS
  ------------------------------------ */

-- FIRST USER
INSERT INTO sppoti.user (id, uuid, version, gender, confirmation_code, account_creation_date, account_max_activation_date, confirmed, date_born, deleted, description, email, first_name, job, last_name, password, telephone, username, language)
VALUES
  (11, 12345678, 1, 'MALE', 'code', curdate(), curdate(), TRUE, curdate(), FALSE, NULL, 'wail.djenane@gmail.com', 'wail', NULL,
   'djenane',
   'piratusse1',
   NULL, 'piratusse1', 'fr');

INSERT INTO sppoti.user_roles (users_id, roles_id) VALUES (11, 1);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (11, 2);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (11, 3);

-- SECOND USER
INSERT INTO sppoti.user (id, uuid, version, gender, confirmation_code, account_creation_date, account_max_activation_date, confirmed, date_born, deleted, description, email, first_name, job, last_name, password, telephone, username, language)
VALUES
  (12, 12377778, 2, 'MALE', 'code1', curdate(), curdate(), TRUE, curdate(), FALSE, NULL, 'wail.djenane@gmail.co', 'bachir', NULL,
   'bouacheria',
   'bachir31', NULL, 'bachir31');

INSERT INTO sppoti.user_roles (users_id, roles_id) VALUES (12, 1);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (12, 2);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (12, 3);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (12, 4);

-- THIRD USER
INSERT INTO sppoti.user (id, uuid, version, gender, confirmation_code, account_creation_date, account_max_activation_date, confirmed, date_born, deleted, description, email, first_name, job, last_name, password, telephone, username, language)
VALUES
  (13, 12999678, 3, 'MALE', 'code2', curdate(), curdate(), TRUE, curdate(), FALSE, NULL, 'aymen.zaghwali@test.fr', 'Aymen', NULL,
   'Zaghwali', 'aymen', NULL, 'aymen31', 'fr');

INSERT INTO sppoti.user_roles (users_id, roles_id) VALUES (13, 1);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (13, 4);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (13, 3);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (13, 5);

-- FOURTH USER
INSERT INTO sppoti.user (id, uuid, version, gender, confirmation_code, account_creation_date, account_max_activation_date, confirmed, date_born, deleted, description, email, first_name, job, last_name, password, telephone, username, language)
VALUES
  (14, 10459678, 0, 'MALE', 'code4', curdate(), curdate(), TRUE , curdate(), FALSE, NULL, 'gg.gg@gg.gg', 'koko', NULL, 'MUkA',
   'KAKA31', NULL, 'kaka31', 'fr');

INSERT INTO sppoti.user_roles (users_id, roles_id) VALUES (14, 1);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (14, 4);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (14, 5);

-- FIFTH USER
INSERT INTO sppoti.user (id, uuid, version, gender, confirmation_code, account_creation_date, account_max_activation_date, confirmed, date_born, deleted, description, email, first_name, job, last_name, password, telephone, username, language)
VALUES
  (15, 10415388, 0, 'MALE', 'code5', curdate(), curdate(), TRUE,  curdate(), FALSE, NULL, 'gg.gg@kk.kk', 'koko', NULL, 'KOKO',
   'koko31', NULL, 'koko31', 'fr');

INSERT INTO sppoti.user_roles (users_id, roles_id) VALUES (15, 1);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (15, 6);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (14, 1);

/* ------------------------------------
  INSERT - TEAMS && TEAM MEMBERS
  ------------------------------------ */

-- team1
INSERT INTO sppoti.team (id, uuid, version, cover_path, deleted, logo_path, name, sport_id)
VALUES (2, -2130226612, 0, 'mon_cover', FALSE, 'mon_logos', 'my host team name tsssss tmok tbok', 2);

INSERT INTO sppoti.team_member (id, uuid, version, admin, invitation_date, join_date, status, team_captain, x_position, y_position, team_id, user_id)
VALUES (1, -22613402, 0, TRUE, '2017-02-23 11:56:55', NULL, 'CONFIRMED', TRUE, 98765498, 876543, 2, 11);

INSERT INTO sppoti.team_member (id, uuid, version, admin, invitation_date, join_date, status, team_captain, x_position, y_position, team_id, user_id)
VALUES (2, -22684522, 0, FALSE, '2017-02-23 11:56:55', NULL, 'PENDING', FALSE, NULL, NULL, 2, 12);

-- team2
INSERT INTO sppoti.team (id, uuid, version, cover_path, deleted, logo_path, name, sport_id)
VALUES (3, -219854122, 0, NULL, FALSE, NULL, 'my adverse team', 2);

INSERT INTO sppoti.team_member (id, uuid, version, admin, invitation_date, join_date, status, team_captain, x_position, y_position, team_id, user_id)
VALUES (3, -15813402, 0, TRUE, '2017-02-23 11:56:55', NULL, 'CONFIRMED', TRUE, 98765498, 876543, 3, 12);

INSERT INTO sppoti.team_member (id, uuid, version, admin, invitation_date, join_date, status, team_captain, x_position, y_position, team_id, user_id)
VALUES (4, -16284522, 0, FALSE, '2017-02-23 11:56:55', NULL, 'PENDING', FALSE, NULL, NULL, 3, 14);

/* ------------------------------------
  INSERT - SPPOTI && SPPOTERS
  ------------------------------------ */

-- sppoti1
INSERT INTO sppoti.sppoti (id, uuid, version, date_time_start, datetime_created, deleted, description, location, max_members_count, tags, team_adverse_status, name, sport_id, team_adverse_id, team_host_id, user_id)
VALUES (1, -326614926, 0, '2016-01-18 22:32:00', '2017-03-10 16:16:18', FALSE, 'My FIRST fancy sppoti',
           'Ain el bia 31200 v - sh5', 25, '$mahrez, $Slimani', 'NO_CHALLENGE_YET', 'FOOTBALL IN the STREET', 1, NULL, 2, 11);

INSERT INTO sppoti.sppoti_member (id, uuid, version, acceptation_date, invitation_date, status, x_position, y_position, sppoti_id, team_member_id)
VALUES (1, 1750519340, 0, NULL, '2017-03-10 16:16:18', 'CONFIRMED', NULL, NULL, 1, 1);

INSERT INTO sppoti.sppoti_member (id, uuid, version, acceptation_date, invitation_date, status, x_position, y_position, sppoti_id, team_member_id)
VALUES (2, 1856519340, 0, NULL, '2017-03-10 16:16:18', 'PENDING', NULL, NULL, 1, 2);

-- Fix content utf8mb4 encoding

ALTER DATABASE sppoti CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;