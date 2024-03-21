package com.e2x.bigcommerce.routinefinderservice.service;

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry;

public interface RoutineEnquiryService {
    RoutineEnquiry start(String storeId);
    RoutineEnquiry submit(String storeId, RoutineEnquiry routineEnquiry);
}
