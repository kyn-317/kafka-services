package com.kyn.inventory.messaging.mapper;

import java.util.function.Function;

import com.kyn.common.messages.inventory.CartInventoryRequest;
import com.kyn.common.messages.inventory.CartInventoryResponse;

import reactor.core.publisher.Mono;

public class MessageDtoMapper {
    
    public static Function<Throwable, Mono<CartInventoryResponse>> toInventoryDeclinedResponse(CartInventoryRequest.Deduct request) {
        return ex -> Mono.fromSupplier(() -> CartInventoryResponse.Declined.builder()
                                                                       .responseItem(request.requestItem())
                                                                       .message(ex.getMessage())
                                                                       .build()
        );
    }
}
