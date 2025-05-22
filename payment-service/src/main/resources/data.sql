-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS payment_data;

CREATE TABLE IF NOT EXISTS payment_data.customer (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   email VARCHAR(50) NOT NULL,
   balance double precision,
   created_at timestamp default current_timestamp,
   updated_at timestamp default current_timestamp,
   created_by varchar(50),
   updated_by varchar(50)
);

CREATE TABLE IF NOT EXISTS payment_data.customer_payment (
   payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   order_id uuid,
   customer_id uuid,
   status VARCHAR(50),
   amount double precision,
   created_at timestamp default current_timestamp,
   updated_at timestamp default current_timestamp,
   created_by varchar(50),
   updated_by varchar(50),
   foreign key (customer_id) references payment_data.customer(id)
);

-- Initialize tables
TRUNCATE TABLE payment_data.customer CASCADE;
TRUNCATE TABLE payment_data.customer_payment CASCADE;

insert into payment_data.customer(email, balance, created_by, updated_by)
    values
        ('sam@gmail.com', 100, 'system', 'system'),
        ('mike@gmail.com', 100, 'system', 'system'),
        ('john@gmail.com', 100, 'system', 'system');