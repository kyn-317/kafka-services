package com.kyn.order.message.orchestrator;
import java.util.function.Function;

import com.kyn.common.dto.OrderSummaryDto;
import com.kyn.common.orchestrator.CartWorkFlowStep;

import reactor.core.publisher.Mono;


public class CartWorkFlow {
    
    private final CartWorkFlowStep<?> firstStep;
    private CartWorkFlowStep<?> lastStep;

    private CartWorkFlow(CartWorkFlowStep<?> firstStep) {
        this.firstStep = firstStep;
        this.lastStep = firstStep;
    }

    public static CartWorkFlow startWith(CartWorkFlowStep<?> step){
        return new CartWorkFlow(step);
    }


    public CartWorkFlow thenNext(CartWorkFlowStep<?> newStep){
        this.lastStep.setNextStep(newStep);
        newStep.setPreviousStep(this.lastStep);
        this.lastStep = newStep;
        return this;
    }

    public CartWorkFlow doOnSuccess(Function<OrderSummaryDto, Mono<Void>> function){
        this.lastStep.setNextStep(dto -> function.apply(dto).then(Mono.empty()));
        return this;
    }

    public CartWorkFlow doOnFailure(Function<OrderSummaryDto, Mono<Void>> function){
        this.firstStep.setPreviousStep(dto -> function.apply(dto).then(Mono.empty()));
        return this;
    }

    public CartWorkFlowStep<?> getFirstStep() {
        return firstStep;
    }
}
