package com.credito.webapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CuentaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cuenta getCuentaSample1() {
        Cuenta cuenta = new Cuenta();
        cuenta.id = 1L;
        cuenta.numeroCuenta = "numeroCuenta1";
        return cuenta;
    }

    public static Cuenta getCuentaSample2() {
        Cuenta cuenta = new Cuenta();
        cuenta.id = 2L;
        cuenta.numeroCuenta = "numeroCuenta2";
        return cuenta;
    }

    public static Cuenta getCuentaRandomSampleGenerator() {
        Cuenta cuenta = new Cuenta();
        cuenta.id = longCount.incrementAndGet();
        cuenta.numeroCuenta = UUID.randomUUID().toString();
        return cuenta;
    }
}
