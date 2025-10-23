package com.credito.webapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.credito.webapp.TestUtil;
import com.credito.webapp.domain.enumeration.TipoPlan;
import com.credito.webapp.service.dto.PlanDeCreditoDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class PlanDeCreditoResourceTest {

    private static final TypeRef<PlanDeCreditoDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<PlanDeCreditoDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final TipoPlan DEFAULT_TIPO = TipoPlan.HIPOTECARIO;
    private static final TipoPlan UPDATED_TIPO = TipoPlan.PERSONAL;

    private static final BigDecimal DEFAULT_TASA_INTERES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TASA_INTERES = new BigDecimal(2);

    private static final Integer DEFAULT_PLAZO_MAXIMO = 1;
    private static final Integer UPDATED_PLAZO_MAXIMO = 2;

    String adminToken;

    PlanDeCreditoDTO planDeCreditoDTO;

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
    public static PlanDeCreditoDTO createEntity() {
        var planDeCreditoDTO = new PlanDeCreditoDTO();
        planDeCreditoDTO.nombre = DEFAULT_NOMBRE;
        planDeCreditoDTO.descripcion = DEFAULT_DESCRIPCION;
        planDeCreditoDTO.tipo = DEFAULT_TIPO;
        planDeCreditoDTO.tasaInteres = DEFAULT_TASA_INTERES;
        planDeCreditoDTO.plazoMaximo = DEFAULT_PLAZO_MAXIMO;
        return planDeCreditoDTO;
    }

    @BeforeEach
    public void initTest() {
        planDeCreditoDTO = createEntity();
    }

    @Test
    public void createPlanDeCredito() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the PlanDeCredito
        planDeCreditoDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the PlanDeCredito in the database
        var planDeCreditoDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(planDeCreditoDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testPlanDeCreditoDTO = planDeCreditoDTOList.stream().filter(it -> planDeCreditoDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPlanDeCreditoDTO.nombre).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPlanDeCreditoDTO.descripcion).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPlanDeCreditoDTO.tipo).isEqualTo(DEFAULT_TIPO);
        assertThat(testPlanDeCreditoDTO.tasaInteres).isEqualByComparingTo(DEFAULT_TASA_INTERES);
        assertThat(testPlanDeCreditoDTO.plazoMaximo).isEqualTo(DEFAULT_PLAZO_MAXIMO);
    }

    @Test
    public void createPlanDeCreditoWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the PlanDeCredito with an existing ID
        planDeCreditoDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PlanDeCredito in the database
        var planDeCreditoDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(planDeCreditoDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNombreIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        planDeCreditoDTO.nombre = null;

        // Create the PlanDeCredito, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PlanDeCredito in the database
        var planDeCreditoDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(planDeCreditoDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTipoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        planDeCreditoDTO.tipo = null;

        // Create the PlanDeCredito, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PlanDeCredito in the database
        var planDeCreditoDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(planDeCreditoDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTasaInteresIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        planDeCreditoDTO.tasaInteres = null;

        // Create the PlanDeCredito, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PlanDeCredito in the database
        var planDeCreditoDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(planDeCreditoDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updatePlanDeCredito() {
        // Initialize the database
        planDeCreditoDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
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
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the planDeCredito
        var updatedPlanDeCreditoDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos/{id}", planDeCreditoDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the planDeCredito
        updatedPlanDeCreditoDTO.nombre = UPDATED_NOMBRE;
        updatedPlanDeCreditoDTO.descripcion = UPDATED_DESCRIPCION;
        updatedPlanDeCreditoDTO.tipo = UPDATED_TIPO;
        updatedPlanDeCreditoDTO.tasaInteres = UPDATED_TASA_INTERES;
        updatedPlanDeCreditoDTO.plazoMaximo = UPDATED_PLAZO_MAXIMO;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedPlanDeCreditoDTO)
            .when()
            .put("/api/plan-de-creditos/" + planDeCreditoDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the PlanDeCredito in the database
        var planDeCreditoDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(planDeCreditoDTOList).hasSize(databaseSizeBeforeUpdate);
        var testPlanDeCreditoDTO = planDeCreditoDTOList.stream().filter(it -> updatedPlanDeCreditoDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPlanDeCreditoDTO.nombre).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPlanDeCreditoDTO.descripcion).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPlanDeCreditoDTO.tipo).isEqualTo(UPDATED_TIPO);
        assertThat(testPlanDeCreditoDTO.tasaInteres).isEqualByComparingTo(UPDATED_TASA_INTERES);
        assertThat(testPlanDeCreditoDTO.plazoMaximo).isEqualTo(UPDATED_PLAZO_MAXIMO);
    }

    @Test
    public void updateNonExistingPlanDeCredito() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
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
            .body(planDeCreditoDTO)
            .when()
            .put("/api/plan-de-creditos/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PlanDeCredito in the database
        var planDeCreditoDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(planDeCreditoDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePlanDeCredito() {
        // Initialize the database
        planDeCreditoDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
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
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the planDeCredito
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/plan-de-creditos/{id}", planDeCreditoDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var planDeCreditoDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(planDeCreditoDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllPlanDeCreditos() {
        // Initialize the database
        planDeCreditoDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the planDeCreditoList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(planDeCreditoDTO.id.intValue()))
            .body("nombre", hasItem(DEFAULT_NOMBRE))
            .body("descripcion", hasItem(DEFAULT_DESCRIPCION.toString()))
            .body("tipo", hasItem(DEFAULT_TIPO.toString()))
            .body("tasaInteres", hasItem(DEFAULT_TASA_INTERES.floatValue()))
            .body("plazoMaximo", hasItem(DEFAULT_PLAZO_MAXIMO.intValue()));
    }

    @Test
    public void getPlanDeCredito() {
        // Initialize the database
        planDeCreditoDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(planDeCreditoDTO)
            .when()
            .post("/api/plan-de-creditos")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the planDeCredito
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/plan-de-creditos/{id}", planDeCreditoDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the planDeCredito
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos/{id}", planDeCreditoDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(planDeCreditoDTO.id.intValue()))
            .body("nombre", is(DEFAULT_NOMBRE))
            .body("descripcion", is(DEFAULT_DESCRIPCION.toString()))
            .body("tipo", is(DEFAULT_TIPO.toString()))
            .body("tasaInteres", comparesEqualTo(DEFAULT_TASA_INTERES.floatValue()))
            .body("plazoMaximo", is(DEFAULT_PLAZO_MAXIMO.intValue()));
    }

    @Test
    public void getNonExistingPlanDeCredito() {
        // Get the planDeCredito
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/plan-de-creditos/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
