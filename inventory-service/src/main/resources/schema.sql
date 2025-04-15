-- warehouse 테이블 생성
CREATE TABLE IF NOT EXISTS warehouse (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    requester_id UUID,
    order_id UUID,
    storage_retrieval_type VARCHAR(20) NOT NULL,
    amount INT NOT NULL,
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
    history_id UUID PRIMARY KEY,
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