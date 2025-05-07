-- 스키마가 없는 경우 생성
CREATE SCHEMA IF NOT EXISTS order_data;

-- 테이블이 없는 경우 생성
-- order by cart
CREATE TABLE IF NOT EXISTS order_data.order (
    order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

-- order default
CREATE TABLE IF NOT EXISTS order_data.purchase_order (
    purchase_order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INT NOT NULL,
    unit_price INT NOT NULL,
    amount INT NOT NULL,
    final_amount INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    delivery_date TIMESTAMP,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

-- order detail
CREATE TABLE IF NOT EXISTS order_data.order_detail (
    order_detail_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES order_data.order(order_id),
    product_id UUID NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

-- order workflow action
CREATE TABLE IF NOT EXISTS order_data.order_workflow_action (
    order_workflow_action_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES order_data.order(order_id),
    action VARCHAR(255) NOT NULL,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

-- truncate table
TRUNCATE TABLE order_data.order_workflow_action CASCADE;
TRUNCATE TABLE order_data.order_detail CASCADE;
TRUNCATE TABLE order_data.purchase_order CASCADE; 
TRUNCATE TABLE order_data.order CASCADE;