package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.service.CompetencyService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(value = CompetencyController.class)
class CompetencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompetencyService service;

    private static final String EXPECTED_OBJ = "{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"categories\":[]}";
    public static final String EXPECTED_ARR = "[" + EXPECTED_OBJ + "]";

    @SneakyThrows
    @Test
    void createCompetency() {
        Competency mockCompetency = new Competency(1L, "testName", "testDesc", List.of());

        when(service.createCompetency(Mockito.any(Competency.class))).thenReturn(mockCompetency);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/competency").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"categories\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());

    }

    @SneakyThrows
    @Test
    void getCompetencies() {
        when(service.findAllCompetencies()).thenReturn(List.of(new Competency(1L, "testName", "testDesc", List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/competency")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_ARR, mvcResult.getResponse().getContentAsString(), false);

    }

    @SneakyThrows
    @Test
    void getOneCompetency() {
        when(service.findCompetencyById(anyLong())).thenReturn(Optional.of(new Competency(1L, "testName", "testDesc", List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/competency/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());


        JSONAssert.assertEquals(EXPECTED_OBJ, mvcResult.getResponse().getContentAsString(), false);

    }

    @SneakyThrows
    @Test
    void updateCompetency() {
        Competency mockCompetency = new Competency(1L, "testName", "testDesc", List.of());

        when(service.updateCompetency(anyLong(), Mockito.any(Competency.class))).thenReturn(mockCompetency);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/competency/1").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"categories\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void removeCompetency() {
        when(service.deleteCompetency(anyLong())).thenReturn(anyBoolean());

        MockHttpServletRequestBuilder requestBuilder = delete("/competency/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @SneakyThrows
    @Test
    void addCategoryToCompetency() {
        when(service.addCategoryToCompetency(anyLong(),anyLong()))
                .thenReturn(new Competency(1L, "testName", "testDesc", List.of(new Category(1L, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of()))));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/competency/1/add/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"categories\":[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"type\":\"HARD_SKILL\",\"priority\":\"LOW\",\"topics\":[]}]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void deleteCategoryFromCompetency() {
        when(service.removeCategoryFromCompetency(anyLong(),anyLong()))
                .thenReturn(new Competency(1L, "testName", "testDesc", List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/competency/1/delete/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_OBJ,
                mvcResult.getResponse().getContentAsString(), false);
    }
}