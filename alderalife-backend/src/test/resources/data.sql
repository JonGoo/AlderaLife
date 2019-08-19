INSERT INTO `roles`  (`id`, `name`)
SELECT * FROM (SELECT 1, 'ROLE_USER') as test WHERE NOT EXISTS(SELECT id from `roles` where `id` = 1);
INSERT INTO `roles`  (`id`, `name`)
SELECT * FROM (SELECT 2, 'ROLE_WHITELIST') as test WHERE NOT EXISTS(SELECT id from `roles` where `id` = 2);
INSERT INTO `roles`  (`id`, `name`)
SELECT * FROM (SELECT 3, 'ROLE_MOD') as test WHERE NOT EXISTS(SELECT id from `roles` where `id` = 3);
INSERT INTO `roles`  (`id`, `name`)
SELECT * FROM (SELECT 4, 'ROLE_ADMIN') as test WHERE NOT EXISTS(SELECT id from `roles` where `id` = 4);
