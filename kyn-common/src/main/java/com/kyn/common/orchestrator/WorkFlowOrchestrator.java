package com.kyn.common.orchestrator;

import com.kyn.common.messages.Request;
import com.kyn.common.messages.Response;
import org.reactivestreams.Publisher;

public interface WorkFlowOrchestrator {

    Publisher<Request> orchestrate(Response response);

}
