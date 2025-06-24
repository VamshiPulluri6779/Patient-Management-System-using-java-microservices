package com.pm.billingservice.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LogManager.getLogger(BillingGrpcService.class);

    /*
        Unlike gRPC, REST is not capable of handling real-time data like live scorecard updates,
        chat applications etc

        The stream observer capability in gRPC is responsible for streaming requests and '
        responses continuously
     */
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
                                     StreamObserver<billing.BillingResponse> responseStreamObserver) {

        log.info("createBillingAccount request received {}", billingRequest.toString());

        // business logic - save to database, validations etc

        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("123")
                .setStatus("ACTIVE")
                .build();

        // send response from gRPC service back to client
        responseStreamObserver.onNext(billingResponse);

        // multiple responses can be sent before completing this cycle

        // Complete the response cycle
        responseStreamObserver.onCompleted();
    }
}
