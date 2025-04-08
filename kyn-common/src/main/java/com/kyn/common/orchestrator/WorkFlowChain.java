package com.kyn.common.orchestrator;

public interface WorkFlowChain {

    void setPreviousStep(RequestCompensator previousStep);

    void setNextStep(RequestSender nextStep);

}
