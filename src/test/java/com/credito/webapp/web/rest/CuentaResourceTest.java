package com.credito.webapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.credito.webapp.TestUtil;
import com.credito.webapp.domain.enumeration.EstadoCuenta;
import com.credito.webapp.service.dto.CuentaDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class CuentaResourceTest {

    private static final TypeRef<CuentaDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<CuentaDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_NUMERO_CUENTA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CUENTA = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_APERTURA = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_FECHA_APERTURA = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS);

    private static final BigDecimal DEFAULT_MONTO_OTORGADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTO_OTORGADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SALDO_PENDIENTE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALDO_PENDIENTE = new BigDecimal(2);

    private static final EstadoCuenta DEFAULT_ESTADO = EstadoCuenta.ACTIVA;
    private static final EstadoCuenta UPDATED_ESTADO = EstadoCuenta.VENCIDA;

    private static final ZonedDateTime DEFAULT_FECHA_CIERRE = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_FECHA_CIERRE = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS);

    String adminToken;

    CuentaDTO cuentaDTO;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config = RestAssured.config()
            .objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuentaDTO createEntity() {
        var cuentaDTO = new CuentaDTO();
        cuentaDTO.numeroCuenta = DEFAULT_NUMERO_CUENTA;
        cuentaDTO.fechaApertura = DEFAULT_FECHA_APERTURA;
        cuentaDTO.montoOtorgado = DEFAULT_MONTO_OTORGADO;
        cuentaDTO.saldoPendiente = DEFAULT_SALDO_PENDIENTE;
        cuentaDTO.estado = DEFAULT_ESTADO;
        cuentaDTO.fechaCierre = DEFAULT_FECHA_CIERRE;
        return cuentaDTO;
    }

    @BeforeEach
    public void initTest() {
        cuentaDTO = createEntity();
    }

    @Test
    public void createCuenta() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Cuenta
        cuentaDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Cuenta in the database
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testCuentaDTO = cuentaDTOList.stream().filter(it -> cuentaDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCuentaDTO.numeroCuenta).isEqualTo(DEFAULT_NUMERO_CUENTA);
        assertThat(testCuentaDTO.fechaApertura).isEqualTo(DEFAULT_FECHA_APERTURA);
        assertThat(testCuentaDTO.montoOtorgado).isEqualByComparingTo(DEFAULT_MONTO_OTORGADO);
        assertThat(testCuentaDTO.saldoPendiente).isEqualByComparingTo(DEFAULT_SALDO_PENDIENTE);
        assertThat(testCuentaDTO.estado).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCuentaDTO.fechaCierre).isEqualTo(DEFAULT_FECHA_CIERRE);
    }

    @Test
    public void createCuentaWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Cuenta with an existing ID
        cuentaDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cuenta in the database
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNumeroCuentaIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        cuentaDTO.numeroCuenta = null;

        // Create the Cuenta, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cuenta in the database
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFechaAperturaIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        cuentaDTO.fechaApertura = null;

        // Create the Cuenta, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cuenta in the database
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkMontoOtorgadoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        cuentaDTO.montoOtorgado = null;

        // Create the Cuenta, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cuenta in the database
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkEstadoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        cuentaDTO.estado = null;

        // Create the Cuenta, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cuenta in the database
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateCuenta() {
        // Initialize the database
        cuentaDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the cuenta
        var updatedCuentaDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas/{id}", cuentaDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the cuenta
        updatedCuentaDTO.numeroCuenta = UPDATED_NUMERO_CUENTA;
        updatedCuentaDTO.fechaApertura = UPDATED_FECHA_APERTURA;
        updatedCuentaDTO.montoOtorgado = UPDATED_MONTO_OTORGADO;
        updatedCuentaDTO.saldoPendiente = UPDATED_SALDO_PENDIENTE;
        updatedCuentaDTO.estado = UPDATED_ESTADO;
        updatedCuentaDTO.fechaCierre = UPDATED_FECHA_CIERRE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedCuentaDTO)
            .when()
            .put("/api/cuentas/" + cuentaDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Cuenta in the database
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeUpdate);
        var testCuentaDTO = cuentaDTOList.stream().filter(it -> updatedCuentaDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCuentaDTO.numeroCuenta).isEqualTo(UPDATED_NUMERO_CUENTA);
        assertThat(testCuentaDTO.fechaApertura).isEqualTo(UPDATED_FECHA_APERTURA);
        assertThat(testCuentaDTO.montoOtorgado).isEqualByComparingTo(UPDATED_MONTO_OTORGADO);
        assertThat(testCuentaDTO.saldoPendiente).isEqualByComparingTo(UPDATED_SALDO_PENDIENTE);
        assertThat(testCuentaDTO.estado).isEqualTo(UPDATED_ESTADO);
        assertThat(testCuentaDTO.fechaCierre).isEqualTo(UPDATED_FECHA_CIERRE);
    }

    @Test
    public void updateNonExistingCuenta() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .put("/api/cuentas/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cuenta in the database
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCuenta() {
        // Initialize the database
        cuentaDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the cuenta
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/cuentas/{id}", cuentaDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var cuentaDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(cuentaDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllCuentas() {
        // Initialize the database
        cuentaDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the cuentaList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(cuentaDTO.id.intValue()))
            .body("numeroCuenta", hasItem(DEFAULT_NUMERO_CUENTA))
            .body("fechaApertura", hasItem(TestUtil.formatDateTime(DEFAULT_FECHA_APERTURA)))
            .body("montoOtorgado", hasItem(DEFAULT_MONTO_OTORGADO.floatValue()))
            .body("saldoPendiente", hasItem(DEFAULT_SALDO_PENDIENTE.floatValue()))
            .body("estado", hasItem(DEFAULT_ESTADO.toString()))
            .body("fechaCierre", hasItem(TestUtil.formatDateTime(DEFAULT_FECHA_CIERRE)));
    }

    @Test
    public void getCuenta() {
        // Initialize the database
        cuentaDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(cuentaDTO)
            .when()
            .post("/api/cuentas")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the cuenta
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/cuentas/{id}", cuentaDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the cuenta
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas/{id}", cuentaDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(cuentaDTO.id.intValue()))
            .body("numeroCuenta", is(DEFAULT_NUMERO_CUENTA))
            .body("fechaApertura", is(TestUtil.formatDateTime(DEFAULT_FECHA_APERTURA)))
            .body("montoOtorgado", comparesEqualTo(DEFAULT_MONTO_OTORGADO.floatValue()))
            .body("saldoPendiente", comparesEqualTo(DEFAULT_SALDO_PENDIENTE.floatValue()))
            .body("estado", is(DEFAULT_ESTADO.toString()))
            .body("fechaCierre", is(TestUtil.formatDateTime(DEFAULT_FECHA_CIERRE)));
    }

    @Test
    public void getNonExistingCuenta() {
        // Get the cuenta
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/cuentas/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
