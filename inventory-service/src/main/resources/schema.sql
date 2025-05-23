-- Create warehouse_data schema
CREATE SCHEMA IF NOT EXISTS warehouse_data;

-- Create warehouse table
CREATE TABLE IF NOT EXISTS warehouse_data.warehouse (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL,
    requester_id UUID,
    order_id UUID,
    storage_retrieval_type VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    snapshot_date VARCHAR(6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);

-- Add warehouse indexes
CREATE INDEX IF NOT EXISTS idx_warehouse_product_id ON warehouse_data.warehouse(product_id);
CREATE INDEX IF NOT EXISTS idx_warehouse_snapshot_date ON warehouse_data.warehouse(snapshot_date);
CREATE INDEX IF NOT EXISTS idx_warehouse_storage_retrieval_type ON warehouse_data.warehouse(storage_retrieval_type);

-- Create warehouse_history table
CREATE TABLE IF NOT EXISTS warehouse_data.warehouse_history (
    history_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id UUID NOT NULL,
    product_id UUID NOT NULL,
    requester_id UUID,
    order_id UUID,
    storage_retrieval_type VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    snapshot_date VARCHAR(6) NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(50),
    archived_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add warehouse_history indexes
CREATE INDEX IF NOT EXISTS idx_warehouse_history_product_id ON warehouse_data.warehouse_history(product_id);
CREATE INDEX IF NOT EXISTS idx_warehouse_history_snapshot_date ON warehouse_data.warehouse_history(snapshot_date);
CREATE INDEX IF NOT EXISTS idx_warehouse_history_storage_retrieval_type ON warehouse_data.warehouse_history(storage_retrieval_type); 

TRUNCATE TABLE warehouse_data.warehouse CASCADE;
TRUNCATE TABLE warehouse_data.warehouse_history CASCADE;

-- Insert sample data into warehouse table
INSERT INTO warehouse_data.warehouse (id, product_id, requester_id, order_id, storage_retrieval_type, quantity, snapshot_date, created_by)
VALUES 
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111122', '22222222-2222-2222-2222-222222222211','22222222-2222-2222-2222-222222222222', 'BASE', 100, TO_CHAR(CURRENT_DATE, 'YYMMDD'), 'SYSTEM'),
    ('33333333-3333-3333-3333-333333333333', '33333333-3333-3333-3333-333333333344', '44444444-4444-4444-4444-444444444433','44444444-4444-4444-4444-444444444444', 'BASE', 50, TO_CHAR(CURRENT_DATE, 'YYMMDD'), 'SYSTEM'),
    ('55555555-5555-5555-5555-555555555555', '55555555-5555-5555-5555-555555555566', '66666666-6666-6666-6666-666666666655','66666666-6666-6666-6666-666666666666', 'BASE', 75, TO_CHAR(CURRENT_DATE, 'YYMMDD'), 'SYSTEM'),
    ('77777777-7777-7777-7777-777777777777', '77777777-7777-7777-7777-777777777788', '88888888-8888-8888-8888-888888888877','88888888-8888-8888-8888-888888888888', 'BASE', 200, TO_CHAR(CURRENT_DATE, 'YYMMDD'), 'SYSTEM'),
    ('99999999-9999-9999-9999-999999999999', '99999999-9999-9999-9999-999999999977', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa99','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'BASE', 150, TO_CHAR(CURRENT_DATE, 'YYMMDD'), 'SYSTEM');
