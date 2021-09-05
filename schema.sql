
CREATE TABLE IF NOT EXISTS roles(
    id int auto_increment,
    description varchar(255) unique not null,
    CONSTRAINT pk_roles PRIMARY KEY(id)
    );

create table IF NOT EXISTS products(
    id int auto_increment,
    name varchar(255) unique not null,
    image varchar(255) not null,
    description varchar(255) not null,
    constraint pk_products primary key(id)
    );

CREATE TABLE IF NOT EXISTS users(
    id int auto_increment,
    name varchar(255),
    email varchar(255) unique,
    password varchar(255),
    city varchar(255),
    CONSTRAINT pk_users PRIMARY KEY(id)
    );

CREATE TABLE IF NOT EXISTS users_x_roles(
    user_id int,
    role_id int,
    CONSTRAINT pk_users_x_roles PRIMARY KEY(user_id, role_id),
    CONSTRAINT fk_users_x_roles_user_id FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_users_x_roles_role_id FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS cards(
    id int auto_increment,
    name varchar(255),
    issuing_bank varchar(255),
    CONSTRAINT pk_credit_cards PRIMARY KEY(id)
    );

CREATE TABLE IF NOT EXISTS accounts(
    id int auto_increment,
    user_id int,
    credit_card_id int,
    CONSTRAINT pk_accounts PRIMARY KEY(id),
    CONSTRAINT fk_accounts_user_id FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_accounts_credit_card_id FOREIGN KEY(credit_card_id) REFERENCES credit_cards(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS subscriptions(
    user_id int,
    product_id int,
    CONSTRAINT pk_subscriptions PRIMARY KEY(user_id, product_id),
    CONSTRAINT fk_subscriptions_user_id FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_subscriptions_product_id FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
    );

create table IF NOT EXISTS companies(
    id int auto_increment,
    name varchar(255),
    image varchar(255),
    constraint pk_companies primary key(id)
    );

create table IF NOT EXISTS branches(
    id int auto_increment,
    company_id int,
    location varchar(255),
    city varchar(255),
    constraint pk_branches primary key(id),
    constraint fk_branches_company_id foreign key(company_id)references companies(id) ON DELETE CASCADE
    );

create table IF NOT EXISTS offers(
    id int auto_increment,
    product_id int,
    company_id int,
    card_id int,
    price float,
    offer_type ENUM("discount","promotion","quantity"),
    from_date timestamp,
    to_date timestamp,
    avaliable boolean,
    description varchar(255),
    constraint pk_offers primary key(id),
    constraint fk_offers_product_id foreign key(product_id) references products(id) ,
    constraint fk_offers_company_id foreign key(company_id) references companies(id) ,
    constraint fk_offers_card_id foreign key (card_id) references credit_cards(id)
    );