package com.kyn.inventory;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.messages.inventory.CartInventoryRequest;

public class TestDataUtil {
    
    public static CartInventoryRequest createDeductRequest(OrderSummaryDto orderSummary) {
        return CartInventoryRequest.Deduct.builder()
                                      .requestItem(orderSummary)
                                      .build();
    }

    public static CartInventoryRequest createRestoreRequest(OrderSummaryDto orderSummary) {
        return CartInventoryRequest.Restore.builder()
                                     .requestItem(orderSummary)
                                     .build();
    }
}
