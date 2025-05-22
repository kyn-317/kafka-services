-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS payment_data;

CREATE TABLE IF NOT EXISTS payment_data.account (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   customer_id UUID NOT NULL,
   balance double precision,
   created_at timestamp default current_timestamp,
   updated_at timestamp default current_timestamp,
   created_by varchar(50),
   updated_by varchar(50)
);

CREATE TABLE IF NOT EXISTS payment_data.payment (
   payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   order_id uuid,
   account_id uuid,
   status VARCHAR(50),
   amount double precision,
   created_at timestamp default current_timestamp,
   updated_at timestamp default current_timestamp,
   created_by varchar(50),
   updated_by varchar(50),
   foreign key (account_id) references payment_data.account(id)
);

-- Initialize tables
TRUNCATE TABLE payment_data.account CASCADE;
TRUNCATE TABLE payment_data.payment CASCADE;

insert into payment_data.account(customer_id, balance, created_by, updated_by)
    values
        ('123e4567-e89b-12d3-a456-426614174000', 100, 'system', 'system'),
        ('123e4567-e89b-12d3-a456-426614174001', 100, 'system', 'system'),
        ('123e4567-e89b-12d3-a456-426614174002', 100, 'system', 'system');