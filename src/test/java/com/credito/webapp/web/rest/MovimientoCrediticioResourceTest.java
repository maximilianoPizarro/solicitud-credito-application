package com.credito.webapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.credito.webapp.TestUtil;
import com.credito.webapp.domain.enumeration.TipoMovimiento;
import com.credito.webapp.service.dto.MovimientoCrediticioDTO;
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
public class MovimientoCrediticioResourceTest {

    private static final TypeRef<MovimientoCrediticioDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<MovimientoCrediticioDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final ZonedDateTime DEFAULT_FECHA_MOVIMIENTO = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_FECHA_MOVIMIENTO = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS);

    private static final TipoMovimiento DEFAULT_TIPO = TipoMovimiento.PAGO;
    private static final TipoMovimiento UPDATED_TIPO = TipoMovimiento.CARGO;

    private static final BigDecimal DEFAULT_MONTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTO = new BigDecimal(2);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIA_EXTERNA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA_EXTERNA = "BBBBBBBBBB";

    String adminToken;

    MovimientoCrediticioDTO movimientoCrediticioDTO;

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
    public static MovimientoCrediticioDTO createEntity() {
        var movimientoCrediticioDTO = new MovimientoCrediticioDTO();
        movimientoCrediticioDTO.fechaMovimiento = DEFAULT_FECHA_MOVIMIENTO;
        movimientoCrediticioDTO.tipo = DEFAULT_TIPO;
        movimientoCrediticioDTO.monto = DEFAULT_MONTO;
        movimientoCrediticioDTO.descripcion = DEFAULT_DESCRIPCION;
        movimientoCrediticioDTO.referenciaExterna = DEFAULT_REFERENCIA_EXTERNA;
        return movimientoCrediticioDTO;
    }

    @BeforeEach
    public void initTest() {
        movimientoCrediticioDTO = createEntity();
    }

    @Test
    public void createMovimientoCrediticio() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the MovimientoCrediticio
        movimientoCrediticioDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the MovimientoCrediticio in the database
        var movimientoCrediticioDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(movimientoCrediticioDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testMovimientoCrediticioDTO = movimientoCrediticioDTOList
            .stream()
            .filter(it -> movimientoCrediticioDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testMovimientoCrediticioDTO.fechaMovimiento).isEqualTo(DEFAULT_FECHA_MOVIMIENTO);
        assertThat(testMovimientoCrediticioDTO.tipo).isEqualTo(DEFAULT_TIPO);
        assertThat(testMovimientoCrediticioDTO.monto).isEqualByComparingTo(DEFAULT_MONTO);
        assertThat(testMovimientoCrediticioDTO.descripcion).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMovimientoCrediticioDTO.referenciaExterna).isEqualTo(DEFAULT_REFERENCIA_EXTERNA);
    }

    @Test
    public void createMovimientoCrediticioWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the MovimientoCrediticio with an existing ID
        movimientoCrediticioDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the MovimientoCrediticio in the database
        var movimientoCrediticioDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(movimientoCrediticioDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkFechaMovimientoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        movimientoCrediticioDTO.fechaMovimiento = null;

        // Create the MovimientoCrediticio, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the MovimientoCrediticio in the database
        var movimientoCrediticioDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(movimientoCrediticioDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTipoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        movimientoCrediticioDTO.tipo = null;

        // Create the MovimientoCrediticio, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the MovimientoCrediticio in the database
        var movimientoCrediticioDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(movimientoCrediticioDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkMontoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        movimientoCrediticioDTO.monto = null;

        // Create the MovimientoCrediticio, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the MovimientoCrediticio in the database
        var movimientoCrediticioDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(movimientoCrediticioDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateMovimientoCrediticio() {
        // Initialize the database
        movimientoCrediticioDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
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
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the movimientoCrediticio
        var updatedMovimientoCrediticioDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios/{id}", movimientoCrediticioDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the movimientoCrediticio
        updatedMovimientoCrediticioDTO.fechaMovimiento = UPDATED_FECHA_MOVIMIENTO;
        updatedMovimientoCrediticioDTO.tipo = UPDATED_TIPO;
        updatedMovimientoCrediticioDTO.monto = UPDATED_MONTO;
        updatedMovimientoCrediticioDTO.descripcion = UPDATED_DESCRIPCION;
        updatedMovimientoCrediticioDTO.referenciaExterna = UPDATED_REFERENCIA_EXTERNA;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedMovimientoCrediticioDTO)
            .when()
            .put("/api/movimiento-crediticios/" + movimientoCrediticioDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the MovimientoCrediticio in the database
        var movimientoCrediticioDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(movimientoCrediticioDTOList).hasSize(databaseSizeBeforeUpdate);
        var testMovimientoCrediticioDTO = movimientoCrediticioDTOList
            .stream()
            .filter(it -> updatedMovimientoCrediticioDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testMovimientoCrediticioDTO.fechaMovimiento).isEqualTo(UPDATED_FECHA_MOVIMIENTO);
        assertThat(testMovimientoCrediticioDTO.tipo).isEqualTo(UPDATED_TIPO);
        assertThat(testMovimientoCrediticioDTO.monto).isEqualByComparingTo(UPDATED_MONTO);
        assertThat(testMovimientoCrediticioDTO.descripcion).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMovimientoCrediticioDTO.referenciaExterna).isEqualTo(UPDATED_REFERENCIA_EXTERNA);
    }

    @Test
    public void updateNonExistingMovimientoCrediticio() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
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
            .body(movimientoCrediticioDTO)
            .when()
            .put("/api/movimiento-crediticios/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the MovimientoCrediticio in the database
        var movimientoCrediticioDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(movimientoCrediticioDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteMovimientoCrediticio() {
        // Initialize the database
        movimientoCrediticioDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
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
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the movimientoCrediticio
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/movimiento-crediticios/{id}", movimientoCrediticioDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var movimientoCrediticioDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(movimientoCrediticioDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllMovimientoCrediticios() {
        // Initialize the database
        movimientoCrediticioDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the movimientoCrediticioList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(movimientoCrediticioDTO.id.intValue()))
            .body("fechaMovimiento", hasItem(TestUtil.formatDateTime(DEFAULT_FECHA_MOVIMIENTO)))
            .body("tipo", hasItem(DEFAULT_TIPO.toString()))
            .body("monto", hasItem(DEFAULT_MONTO.floatValue()))
            .body("descripcion", hasItem(DEFAULT_DESCRIPCION))
            .body("referenciaExterna", hasItem(DEFAULT_REFERENCIA_EXTERNA));
    }

    @Test
    public void getMovimientoCrediticio() {
        // Initialize the database
        movimientoCrediticioDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(movimientoCrediticioDTO)
            .when()
            .post("/api/movimiento-crediticios")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the movimientoCrediticio
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/movimiento-crediticios/{id}", movimientoCrediticioDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the movimientoCrediticio
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios/{id}", movimientoCrediticioDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(movimientoCrediticioDTO.id.intValue()))
            .body("fechaMovimiento", is(TestUtil.formatDateTime(DEFAULT_FECHA_MOVIMIENTO)))
            .body("tipo", is(DEFAULT_TIPO.toString()))
            .body("monto", comparesEqualTo(DEFAULT_MONTO.floatValue()))
            .body("descripcion", is(DEFAULT_DESCRIPCION))
            .body("referenciaExterna", is(DEFAULT_REFERENCIA_EXTERNA));
    }

    @Test
    public void getNonExistingMovimientoCrediticio() {
        // Get the movimientoCrediticio
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/movimiento-crediticios/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
