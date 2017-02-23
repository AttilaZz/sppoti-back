/* ------------------------------------
  INSERT - USERS
  ------------------------------------ */
/*
FIRST USER
 */
INSERT INTO sppoti.user (id, uuid, version, confirmation_code, confirmed, date_born, deleted, description, email, first_name, job, last_name, password, sexe, telephone, username)
VALUES
  (11, 12345678, 1, 'code', TRUE, curdate(), FALSE, NULL, 'wail.djenane@gmail.com', 'wail', NULL, 'djenane', 'dje123',
   'H', NULL, 'piratusse');

INSERT INTO sppoti.user_roles (users_id, roles_id) VALUES (11, 1);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (11, 2);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (11, 3);

/*
SECOND USER
 */
INSERT INTO sppoti.user (id, uuid, version, confirmation_code, confirmed, date_born, deleted, description, email, first_name, job, last_name, password, sexe, telephone, username)
VALUES (12, 12377778, 2, 'code1', TRUE, curdate(), FALSE, NULL, 'wail.djenane@gmail.co', 'bachir', NULL, 'bouacheria',
        'dje123', 'H', NULL, 'bachir31');

INSERT INTO sppoti.user_roles (users_id, roles_id) VALUES (12, 1);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (12, 2);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (12, 3);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (12, 4);

/*
THIRD USER
 */
INSERT INTO sppoti.user (id, uuid, version, confirmation_code, confirmed, date_born, deleted, description, email, first_name, job, last_name, password, sexe, telephone, username)
VALUES
  (13, 12999678, 3, 'code2', TRUE, curdate(), FALSE, NULL, 'wail.djenane@gmail.c', 'amar', NULL, 'sebaa', 'dje123', 'H',
   NULL, 'amar31');

INSERT INTO sppoti.user_roles (users_id, roles_id) VALUES (13, 1);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (13, 4);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (13, 3);
INSERT INTO sppoti.user_related_sports (subscribed_users_id, related_sports_id) VALUES (13, 5);

/* ------------------------------------
  INSERT - TEAMS
  ------------------------------------ */

