package com.credito.webapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cliente getClienteSample1() {
        Cliente cliente = new Cliente();
        cliente.id = 1L;
        cliente.numeroCliente = 1L;
        cliente.nombre = "nombre1";
        cliente.apellido = "apellido1";
        cliente.numeroDocumento = "numeroDocumento1";
        cliente.email = "email1";
        cliente.telefono = "telefono1";
        return cliente;
    }

    public static Cliente getClienteSample2() {
        Cliente cliente = new Cliente();
        cliente.id = 2L;
        cliente.numeroCliente = 2L;
        cliente.nombre = "nombre2";
        cliente.apellido = "apellido2";
        cliente.numeroDocumento = "numeroDocumento2";
        cliente.email = "email2";
        cliente.telefono = "telefono2";
        return cliente;
    }

    public static Cliente getClienteRandomSampleGenerator() {
        Cliente cliente = new Cliente();
        cliente.id = longCount.incrementAndGet();
        cliente.numeroCliente = longCount.incrementAndGet();
        cliente.nombre = UUID.randomUUID().toString();
        cliente.apellido = UUID.randomUUID().toString();
        cliente.numeroDocumento = UUID.randomUUID().toString();
        cliente.email = UUID.randomUUID().toString();
        cliente.telefono = UUID.randomUUID().toString();
        return cliente;
    }
}
