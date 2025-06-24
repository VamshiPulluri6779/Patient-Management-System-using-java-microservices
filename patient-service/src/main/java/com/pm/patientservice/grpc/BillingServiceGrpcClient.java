package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {

    private static final Logger log = LogManager.getLogger(BillingServiceGrpcClient.class);

    // allows synchronous calls to grpc server - REST like
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    // localhost:9091/BillingService/CreatePatientAccount - local
    // aws.grpc.123123/BillingService/CreatePatientAccount - deployed on AWS

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9091}") int serverPort
    ) {

        log.info("Connecting to Billing Service GRPC at " +
                "{}:{}", serverAddress, serverPort);

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(serverAddress, serverPort).usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(managedChannel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {

        BillingRequest billingRequest = BillingRequest.newBuilder().
                setPatientId(patientId).
                setName(name).
                setEmail(email).build();
        BillingResponse billingResponse = blockingStub.createBillingAccount(billingRequest);
        log.info("Received response from GRPC server {}", billingResponse);

        return billingResponse;
    }
}
