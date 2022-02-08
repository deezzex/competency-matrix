package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.service.MatrixService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(value = MatrixController.class)
class MatrixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatrixService service;

    private static final String EXPECTED_OBJ = "{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"competencies\":[],\"levels\":[]}";
    public static final String EXPECTED_ARR = "["+ EXPECTED_OBJ +"]";

    @SneakyThrows
    @Test
    void createMatrix() {
        Matrix mockMatrix = new Matrix(1L, "testName", "testDesc", List.of(), List.of());

        when(service.createMatrix(Mockito.any(Matrix.class))).thenReturn(mockMatrix);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/matrix").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"competencies\":[],\"levels\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void getMatrices() {
        when(service.findAllMatrices()).thenReturn(List.of(new Matrix(1L, "testName", "testDesc", List.of(), List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/matrix")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_ARR, mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void getOneMatrix() {
        when(service.findMatrixById(anyLong())).thenReturn(Optional.of(new Matrix(1L, "testName", "testDesc", List.of(), List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/matrix/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());


        JSONAssert.assertEquals(EXPECTED_OBJ, mvcResult.getResponse().getContentAsString(), false);

    }

    @SneakyThrows
    @Test
    void updateMatrix() {
        Matrix mockMatrix = new Matrix(1L, "testName", "testDesc", List.of(), List.of());

        when(service.updateMatrix(anyLong(), Mockito.any(Matrix.class))).thenReturn(mockMatrix);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/matrix/1").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"competencies\":[],\"levels\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void removeMatrix() {
        when(service.deleteMatrix(anyLong())).thenReturn(anyBoolean());

        MockHttpServletRequestBuilder requestBuilder = delete("/matrix/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @SneakyThrows
    @Test
    void addLevelToMatrix() {
        when(service.addLevelToMatrix(anyLong(),anyLong()))
                .thenReturn(new Matrix(1L, "testName", "testDesc", List.of(), List.of(new Level(1L, "testName", "testDesc", List.of()))));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/matrix/1/add/level/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"competencies\":[],\"levels\":[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"topics\":[]}]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void deleteLevelFromMatrix() {
        when(service.removeLevelFromMatrix(anyLong(),anyLong()))
                .thenReturn(new Matrix(1L, "testName", "testDesc", List.of(), List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/matrix/1/delete/level/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_OBJ,
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void addCompetencyToMatrix() {
        when(service.addCompetencyToMatrix(anyLong(),anyLong(), any(UserDetails.class)))
                .thenReturn(new Matrix(1L, "testName", "testDesc", List.of(new Competency(1L, "testName", "testDesc", List.of())), List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/matrix/1/add/competency/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"competencies\":[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"categories\":[]}],\"levels\":[]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void deleteCompetencyFromMatrix() {
        when(service.removeCompetencyFromMatrix(anyLong(),anyLong()))
                .thenReturn(new Matrix(1L, "testName", "testDesc", List.of(), List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/matrix/1/delete/competency/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_OBJ,
                mvcResult.getResponse().getContentAsString(), false);
    }
}