package com.kyn.common.orchestrator;

import com.kyn.common.messages.Response;

public interface WorkFlowStep<T extends Response> extends
                                                        RequestSender,
                                                        RequestCompensator,
                                                        ResponseProcessor<T>,
                                                        WorkFlowChain {


}
