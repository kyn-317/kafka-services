package com.kyn.order.message.orchestrator;

import java.util.UUID;
import java.util.function.Function;

import com.kyn.common.orchestrator.WorkFlowStep;

import reactor.core.publisher.Mono;


public class WorkFlow {

    private final WorkFlowStep<?> firstStep;
    private WorkFlowStep<?> lastStep;

    private WorkFlow(WorkFlowStep<?> firstStep) {
        this.firstStep = firstStep;
        this.lastStep = firstStep;
    }

    public static WorkFlow startWith(WorkFlowStep<?> step){
        return new WorkFlow(step);
    }


    public WorkFlow thenNext(WorkFlowStep<?> newStep){
        this.lastStep.setNextStep(newStep);
        newStep.setPreviousStep(this.lastStep);
        this.lastStep = newStep;
        return this;
    }

    public WorkFlow doOnSuccess(Function<UUID, Mono<Void>> function){
        this.lastStep.setNextStep(id -> function.apply(id).then(Mono.empty()));
        return this;
    }

    public WorkFlow doOnFailure(Function<UUID, Mono<Void>> function){
        this.firstStep.setPreviousStep(id -> function.apply(id).then(Mono.empty()));
        return this;
    }

    public WorkFlowStep<?> getFirstStep() {
        return firstStep;
    }
}
