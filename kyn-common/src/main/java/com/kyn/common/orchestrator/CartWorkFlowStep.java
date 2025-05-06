package com.kyn.common.orchestrator;

import com.kyn.common.messages.CartResponse;

public interface CartWorkFlowStep<T extends CartResponse> extends
CartRequestSender,
CartRequestCompensator,
CartResponseProcessor<T>,
CartWorkFlowChain {
}
