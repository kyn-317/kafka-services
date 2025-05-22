package com.kyn.common.orchestrator;

public interface CartWorkFlowChain {
    void setPreviousStep(CartRequestCompensator previousStep);

    void setNextStep(CartRequestSender nextStep);
}
