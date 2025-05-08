-- warehouse 테이블 생성
CREATE TABLE IF NOT EXISTS warehouse (
    id UUID default random_uuid() primary key,
    product_id UUID NOT NULL,
    requester_id UUID,
    order_id UUID,
    storage_retrieval_type VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    snapshot_date VARCHAR(6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);

-- warehouse 인덱스 추가
CREATE INDEX IF NOT EXISTS idx_warehouse_product_id ON warehouse(product_id);
CREATE INDEX IF NOT EXISTS idx_warehouse_snapshot_date ON warehouse(snapshot_date);
CREATE INDEX IF NOT EXISTS idx_warehouse_storage_retrieval_type ON warehouse(storage_retrieval_type);

-- warehouse_history 테이블 생성
CREATE TABLE IF NOT EXISTS warehouse_history (
    history_id UUID default random_uuid() primary key,
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

-- warehouse_history 인덱스 추가
CREATE INDEX IF NOT EXISTS idx_warehouse_history_product_id ON warehouse_history(product_id);
CREATE INDEX IF NOT EXISTS idx_warehouse_history_snapshot_date ON warehouse_history(snapshot_date);
CREATE INDEX IF NOT EXISTS idx_warehouse_history_storage_retrieval_type ON warehouse_history(storage_retrieval_type); 

truncate table warehouse;
truncate table warehouse_history;
-- Insert sample data into warehouse table
INSERT INTO warehouse (id, product_id, requester_id, order_id, storage_retrieval_type, quantity, snapshot_date, created_by)
VALUES 
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111122', '22222222-2222-2222-2222-222222222211','22222222-2222-2222-2222-222222222222', 'BASE', 100, '250508', 'SYSTEM'),
    ('33333333-3333-3333-3333-333333333333', '33333333-3333-3333-3333-333333333344', '44444444-4444-4444-4444-444444444433','44444444-4444-4444-4444-444444444444', 'BASE', 50, '250508', 'SYSTEM'),
    ('55555555-5555-5555-5555-555555555555', '55555555-5555-5555-5555-555555555566', '66666666-6666-6666-6666-666666666655','66666666-6666-6666-6666-666666666666', 'BASE', 75, '250508', 'SYSTEM'),
    ('77777777-7777-7777-7777-777777777777', '77777777-7777-7777-7777-777777777788', '88888888-8888-8888-8888-888888888877','88888888-8888-8888-8888-888888888888', 'BASE', 200, '250508', 'SYSTEM'),
    ('99999999-9999-9999-9999-999999999999', '99999999-9999-9999-9999-999999999977', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa99','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'BASE', 150, '250508', 'SYSTEM');
