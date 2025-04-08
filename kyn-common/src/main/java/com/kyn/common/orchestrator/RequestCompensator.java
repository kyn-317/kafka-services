package com.kyn.common.orchestrator;

import com.kyn.common.messages.Request;
import org.reactivestreams.Publisher;

import java.util.UUID;

public interface RequestCompensator {

    Publisher<Request> compensate(UUID id);

}
