package com.kyn.common.messages;

import java.util.Map;

public interface  MapRequest extends BaseRequest<Map<String,String>>{
    
    String userId();
}
