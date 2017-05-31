insert into ait.ait_param values (1,'REDIRECT_URL', 'https://localhost:4200');
insert into ait.ait_menu (ID, IS_ENABLED, TITLE, DESCRIPTION, MENU_ORDER, ICON, PATH, ROLES, PARENT_ID) values (1,1,'Menu Principal', 'Titulo principal', 1, 'home',null,null,null);
insert into ait.ait_menu (ID, IS_ENABLED, TITLE, DESCRIPTION, MENU_ORDER, ICON, PATH, ROLES, PARENT_ID) values (2,1,'Mi Cuenta', 'Titulo principal', 2, 'person',null,null,null);
insert into ait.ait_menu (ID, IS_ENABLED, TITLE, DESCRIPTION, MENU_ORDER, ICON, PATH, ROLES, PARENT_ID) values (3,1,'Sub menu 1.1', 'opcion 2', 1, 'android','/about','AIT_COMMON',1);
insert into ait.ait_menu (ID, IS_ENABLED, TITLE, DESCRIPTION, MENU_ORDER, ICON, PATH, ROLES, PARENT_ID) values (4,1,'Sub menu 1.2', 'opcion 3', 2, 'backup','/rte','ROLE_RTE',1);
insert into ait.ait_menu (ID, IS_ENABLED, TITLE, DESCRIPTION, MENU_ORDER, ICON, PATH, ROLES, PARENT_ID) values (5,1,'Sub menu 2.1', 'opcion 2', 1, 'book',null,null,2);
insert into ait.ait_menu (ID, IS_ENABLED, TITLE, DESCRIPTION, MENU_ORDER, ICON, PATH, ROLES, PARENT_ID) values (6,1,'Régimen de Transformación y Ensamble', 'RTE', 2, 'eject','/rte','ROLE_RTE',5);