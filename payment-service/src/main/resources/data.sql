DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS customer_payment;

CREATE TABLE customer (
   id uuid default random_uuid() primary key,
   email VARCHAR(50) NOT NULL,
   balance int,
   created_at timestamp default current_timestamp,
   updated_at timestamp default current_timestamp,
   created_by varchar(50),
   updated_by varchar(50)
);

CREATE TABLE customer_payment (
   payment_id uuid default random_uuid() primary key,
   order_id uuid,
   customer_id uuid,
   status VARCHAR(50),
   amount int,
   created_at timestamp default current_timestamp,
   updated_at timestamp default current_timestamp,
   created_by varchar(50),
   updated_by varchar(50),
   foreign key (customer_id) references customer(id)
);

insert into customer(email, balance, created_by, updated_by)
    values
        ('sam@gmail.com', 100, 'system', 'system'),
        ('mike@gmail.com', 100, 'system', 'system'),
        ('john@gmail.com', 100, 'system', 'system');