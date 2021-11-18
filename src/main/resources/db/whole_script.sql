drop schema if exists board_game_1;
create schema board_game_1;
use board_game_1;

CREATE TABLE users (
    id int(10) PRIMARY KEY AUTO_INCREMENT,
    active boolean not null,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(60) not null
);

create table user_roles (
    user_id int(10),
    role varchar(100),
    constraint user_roles_user_id_fk foreign key(user_id) references users(id)
);

create table card_descriptions (
    id int(10) PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null,
    type varchar(50) not null,
    points int(2) not null default 0,
    coins int(2) not null default 0,
    color varchar(50),
    houses int(1) not null default 0,
    crosses int(1) not null default 0,
    anchors int(1) not null default 0,
    swords int(2) not null default 0,
    amount int(2) not null default 0
);

create table games (
    id int(10) PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null,
    admin_id int(10) not null,
    status varchar(50) not null,
    active_player_id int(10) not null,
    constraint games_admin_id_fk foreign key(admin_id) references users(id)
);

create table players (
    id int(10) PRIMARY KEY AUTO_INCREMENT,
    game_id int(10) not null,
    user_id int(10) not null,
    order int(2) not null default 0,
    coins int(2) not null default 0,
    points int(2) not null default 0,
    swords int(2) not null default 0,
    crosses int(2) not null default 0,
    houses int(2) not null default 0,
    anchors int(2) not null default 0,
    constraint players_game_id_fk foreign key(game_id) references games(id),
    constraint players_user_id_fk foreign key(user_id) references users(id),
    index players_game_id_indx (game_id),
    index players_user_id_indx (user_id)
);

create table cards (
    id int(10) PRIMARY KEY AUTO_INCREMENT,
    game_id int(10) not null,
    player_id int(10) not null,
    card_description_id int(10) not null,
    coin boolean not null default true,
    status varchar(50) not null,
    constraint cards_game_id_fk foreign key(game_id) references games(id),
    constraint cards_player_id_fk foreign key(player_id) references players(id),
    index cards_game_id_indx (game_id),
    index cards_player_id_indx (player_id)
);