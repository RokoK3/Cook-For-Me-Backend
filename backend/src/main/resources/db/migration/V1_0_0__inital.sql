CREATE TABLE user_data (
    id          SERIAL          PRIMARY KEY             ,
    username    VARCHAR(20)     NOT NULL                ,
    email       VARCHAR(50)     NOT NULL                ,
    password    VARCHAR(1024)                           ,
    name        VARCHAR(20)     NOT NULL                ,
    lastname    VARCHAR(20)     NOT NULL                ,
    location    VARCHAR(50)     NOT NULL                ,
    is_admin    BOOLEAN         NOT NULL DEFAULT false
);

CREATE TABLE cook (
    id          SERIAL          PRIMARY KEY         ,
    username    VARCHAR(20)     NOT NULL            ,
    email       VARCHAR(50)     NOT NULL            ,
    password    VARCHAR(1024)                       ,
    name        VARCHAR(20)     NOT NULL            ,
    lastname    VARCHAR(20)     NOT NULL            ,
    location    VARCHAR(50)     NOT NULL            ,
    rating      DECIMAL(3,2)    NOT NULL DEFAULT 0  ,
    cuisine     SMALLINT        NOT NULL
);

CREATE TABLE dish (
    id                  SERIAL          PRIMARY KEY ,
    cook_id             BIGINT                      ,
    name                VARCHAR(20)     NOT NULL    ,
    image               VARCHAR(200)                ,
    order_deadline      TIME            NOT NULL    ,
    min_orders          SMALLINT                    ,
    max_orders          SMALLINT        NOT NULL    ,
    preparation_time    INT             NOT NULL    ,
    FOREIGN KEY (cook_id) REFERENCES cook(id)
);

CREATE TABLE ingredient_list (
    dish_id      BIGINT      REFERENCES dish(id) ,
    ingredient  SMALLINT                        ,
    PRIMARY KEY (dish_id, ingredient)
);

CREATE TABLE dish_day (
    dish_id      BIGINT      REFERENCES dish(id) ,
    day_of_week   SMALLINT                        ,
    start_time  TIME        NOT NULL            ,
    end_time    TIME        NOT NULL            ,
    PRIMARY KEY (dish_id, day_of_week)
);

CREATE TABLE "order" (
    id          SERIAL      PRIMARY KEY             ,
    user_id     BIGINT      REFERENCES user_data(id),
    cook_rating SMALLINT
);

CREATE TABLE order_list (
    order_id     BIGINT  REFERENCES "order"(id)  ,
    dish_id      BIGINT  REFERENCES dish(id)     ,
    PRIMARY KEY (order_id, dish_id)
);