INSERT INTO `role` (`id`, `name`, `parent_id`, `remark`) VALUES (1, '超级管理员', 0, '最高权限且唯一');

INSERT INTO `permission` (`id`, `code`, `name`, `url_pattern`, `parent_id`, `remark`) VALUES (1, 'home', '设备概览', '/home/**', -1, NULL);
INSERT INTO `permission` (`id`, `code`, `name`, `url_pattern`, `parent_id`, `remark`) VALUES (2, 'monitor', '实时监控', '/monitor/**', -1, NULL);
INSERT INTO `permission` (`id`, `code`, `name`, `url_pattern`, `parent_id`, `remark`) VALUES (3, 'diagnosis', '故障预警', '/diagnosis/**', -1, NULL);
INSERT INTO `permission` (`id`, `code`, `name`, `url_pattern`, `parent_id`, `remark`) VALUES (4, 'information', '信息管理', '/information/**', -1, NULL);
INSERT INTO `permission` (`id`, `code`, `name`, `url_pattern`, `parent_id`, `remark`) VALUES (5, 'device', '设备管理', '/device/**', -1, NULL);
INSERT INTO `permission` (`id`, `code`, `name`, `url_pattern`, `parent_id`, `remark`) VALUES (6, 'configure', '系统设置', '/configure/**', -1, NULL);

INSERT INTO `role_permission_mid` (`rol_id`, `per_id`) VALUES (1, 1);
INSERT INTO `role_permission_mid` (`rol_id`, `per_id`) VALUES (1, 2);
INSERT INTO `role_permission_mid` (`rol_id`, `per_id`) VALUES (1, 3);
INSERT INTO `role_permission_mid` (`rol_id`, `per_id`) VALUES (1, 4);
INSERT INTO `role_permission_mid` (`rol_id`, `per_id`) VALUES (1, 5);
INSERT INTO `role_permission_mid` (`rol_id`, `per_id`) VALUES (1, 6);

INSERT INTO `user` (`id`, `role_id`, `name`, `username`, `password`,`remark`, `create_time`, `update_time`) VALUES (1, 1, '超级管理员', 'admin', '$2a$10$0Umk.u3RydARdARAcuVZWuf6kbX9g1pOxN/1TxHf3czApIG6Qg5Hy', '超级管理员', NOW(), NOW());

