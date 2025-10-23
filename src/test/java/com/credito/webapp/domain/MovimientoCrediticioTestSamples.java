package com.credito.webapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MovimientoCrediticioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MovimientoCrediticio getMovimientoCrediticioSample1() {
        MovimientoCrediticio movimientoCrediticio = new MovimientoCrediticio();
        movimientoCrediticio.id = 1L;
        movimientoCrediticio.descripcion = "descripcion1";
        movimientoCrediticio.referenciaExterna = "referenciaExterna1";
        return movimientoCrediticio;
    }

    public static MovimientoCrediticio getMovimientoCrediticioSample2() {
        MovimientoCrediticio movimientoCrediticio = new MovimientoCrediticio();
        movimientoCrediticio.id = 2L;
        movimientoCrediticio.descripcion = "descripcion2";
        movimientoCrediticio.referenciaExterna = "referenciaExterna2";
        return movimientoCrediticio;
    }

    public static MovimientoCrediticio getMovimientoCrediticioRandomSampleGenerator() {
        MovimientoCrediticio movimientoCrediticio = new MovimientoCrediticio();
        movimientoCrediticio.id = longCount.incrementAndGet();
        movimientoCrediticio.descripcion = UUID.randomUUID().toString();
        movimientoCrediticio.referenciaExterna = UUID.randomUUID().toString();
        return movimientoCrediticio;
    }
}
