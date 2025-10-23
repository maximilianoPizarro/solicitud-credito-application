package com.credito.webapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PlanDeCreditoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PlanDeCredito getPlanDeCreditoSample1() {
        PlanDeCredito planDeCredito = new PlanDeCredito();
        planDeCredito.id = 1L;
        planDeCredito.nombre = "nombre1";
        planDeCredito.plazoMaximo = 1;
        return planDeCredito;
    }

    public static PlanDeCredito getPlanDeCreditoSample2() {
        PlanDeCredito planDeCredito = new PlanDeCredito();
        planDeCredito.id = 2L;
        planDeCredito.nombre = "nombre2";
        planDeCredito.plazoMaximo = 2;
        return planDeCredito;
    }

    public static PlanDeCredito getPlanDeCreditoRandomSampleGenerator() {
        PlanDeCredito planDeCredito = new PlanDeCredito();
        planDeCredito.id = longCount.incrementAndGet();
        planDeCredito.nombre = UUID.randomUUID().toString();
        planDeCredito.plazoMaximo = intCount.incrementAndGet();
        return planDeCredito;
    }
}
