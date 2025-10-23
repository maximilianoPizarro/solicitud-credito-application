package com.credito.webapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.credito.webapp.TestUtil;
import com.credito.webapp.domain.enumeration.TipoDocumento;
import com.credito.webapp.service.dto.ClienteDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class ClienteResourceTest {

    private static final TypeRef<ClienteDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<ClienteDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final Long DEFAULT_NUMERO_CLIENTE = 1L;
    private static final Long UPDATED_NUMERO_CLIENTE = 2L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final TipoDocumento DEFAULT_TIPO_DOCUMENTO = TipoDocumento.DNI;
    private static final TipoDocumento UPDATED_TIPO_DOCUMENTO = TipoDocumento.PASAPORTE;

    private static final String DEFAULT_NUMERO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    String adminToken;

    ClienteDTO clienteDTO;

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
    public static ClienteDTO createEntity() {
        var clienteDTO = new ClienteDTO();
        clienteDTO.numeroCliente = DEFAULT_NUMERO_CLIENTE;
        clienteDTO.nombre = DEFAULT_NOMBRE;
        clienteDTO.apellido = DEFAULT_APELLIDO;
        clienteDTO.fechaNacimiento = DEFAULT_FECHA_NACIMIENTO;
        clienteDTO.tipoDocumento = DEFAULT_TIPO_DOCUMENTO;
        clienteDTO.numeroDocumento = DEFAULT_NUMERO_DOCUMENTO;
        clienteDTO.email = DEFAULT_EMAIL;
        clienteDTO.telefono = DEFAULT_TELEFONO;
        return clienteDTO;
    }

    @BeforeEach
    public void initTest() {
        clienteDTO = createEntity();
    }

    @Test
    public void createCliente() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Cliente
        clienteDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testClienteDTO = clienteDTOList.stream().filter(it -> clienteDTO.id.equals(it.id)).findFirst().get();
        assertThat(testClienteDTO.numeroCliente).isEqualTo(DEFAULT_NUMERO_CLIENTE);
        assertThat(testClienteDTO.nombre).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testClienteDTO.apellido).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testClienteDTO.fechaNacimiento).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testClienteDTO.tipoDocumento).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testClienteDTO.numeroDocumento).isEqualTo(DEFAULT_NUMERO_DOCUMENTO);
        assertThat(testClienteDTO.email).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClienteDTO.telefono).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    public void createClienteWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Cliente with an existing ID
        clienteDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNumeroClienteIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clienteDTO.numeroCliente = null;

        // Create the Cliente, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNombreIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clienteDTO.nombre = null;

        // Create the Cliente, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkApellidoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clienteDTO.apellido = null;

        // Create the Cliente, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFechaNacimientoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clienteDTO.fechaNacimiento = null;

        // Create the Cliente, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTipoDocumentoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clienteDTO.tipoDocumento = null;

        // Create the Cliente, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNumeroDocumentoIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clienteDTO.numeroDocumento = null;

        // Create the Cliente, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateCliente() {
        // Initialize the database
        clienteDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
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
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the cliente
        var updatedClienteDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes/{id}", clienteDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the cliente
        updatedClienteDTO.numeroCliente = UPDATED_NUMERO_CLIENTE;
        updatedClienteDTO.nombre = UPDATED_NOMBRE;
        updatedClienteDTO.apellido = UPDATED_APELLIDO;
        updatedClienteDTO.fechaNacimiento = UPDATED_FECHA_NACIMIENTO;
        updatedClienteDTO.tipoDocumento = UPDATED_TIPO_DOCUMENTO;
        updatedClienteDTO.numeroDocumento = UPDATED_NUMERO_DOCUMENTO;
        updatedClienteDTO.email = UPDATED_EMAIL;
        updatedClienteDTO.telefono = UPDATED_TELEFONO;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedClienteDTO)
            .when()
            .put("/api/clientes/" + clienteDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeUpdate);
        var testClienteDTO = clienteDTOList.stream().filter(it -> updatedClienteDTO.id.equals(it.id)).findFirst().get();
        assertThat(testClienteDTO.numeroCliente).isEqualTo(UPDATED_NUMERO_CLIENTE);
        assertThat(testClienteDTO.nombre).isEqualTo(UPDATED_NOMBRE);
        assertThat(testClienteDTO.apellido).isEqualTo(UPDATED_APELLIDO);
        assertThat(testClienteDTO.fechaNacimiento).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testClienteDTO.tipoDocumento).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testClienteDTO.numeroDocumento).isEqualTo(UPDATED_NUMERO_DOCUMENTO);
        assertThat(testClienteDTO.email).isEqualTo(UPDATED_EMAIL);
        assertThat(testClienteDTO.telefono).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    public void updateNonExistingCliente() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
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
            .body(clienteDTO)
            .when()
            .put("/api/clientes/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Cliente in the database
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCliente() {
        // Initialize the database
        clienteDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
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
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the cliente
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/clientes/{id}", clienteDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var clienteDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(clienteDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllClientes() {
        // Initialize the database
        clienteDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the clienteList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(clienteDTO.id.intValue()))
            .body("numeroCliente", hasItem(DEFAULT_NUMERO_CLIENTE.intValue()))
            .body("nombre", hasItem(DEFAULT_NOMBRE))
            .body("apellido", hasItem(DEFAULT_APELLIDO))
            .body("fechaNacimiento", hasItem(TestUtil.formatDateTime(DEFAULT_FECHA_NACIMIENTO)))
            .body("tipoDocumento", hasItem(DEFAULT_TIPO_DOCUMENTO.toString()))
            .body("numeroDocumento", hasItem(DEFAULT_NUMERO_DOCUMENTO))
            .body("email", hasItem(DEFAULT_EMAIL))
            .body("telefono", hasItem(DEFAULT_TELEFONO));
    }

    @Test
    public void getCliente() {
        // Initialize the database
        clienteDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clienteDTO)
            .when()
            .post("/api/clientes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the cliente
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/clientes/{id}", clienteDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the cliente
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes/{id}", clienteDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(clienteDTO.id.intValue()))
            .body("numeroCliente", is(DEFAULT_NUMERO_CLIENTE.intValue()))
            .body("nombre", is(DEFAULT_NOMBRE))
            .body("apellido", is(DEFAULT_APELLIDO))
            .body("fechaNacimiento", is(TestUtil.formatDateTime(DEFAULT_FECHA_NACIMIENTO)))
            .body("tipoDocumento", is(DEFAULT_TIPO_DOCUMENTO.toString()))
            .body("numeroDocumento", is(DEFAULT_NUMERO_DOCUMENTO))
            .body("email", is(DEFAULT_EMAIL))
            .body("telefono", is(DEFAULT_TELEFONO));
    }

    @Test
    public void getNonExistingCliente() {
        // Get the cliente
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clientes/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
