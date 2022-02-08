package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.service.LevelService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(value = LevelController.class)
class LevelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LevelService service;

    private static final String EXPECTED_OBJ = "{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"topics\":[]}";
    public static final String EXPECTED_ARR = "[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"topics\":[]}]";

    @SneakyThrows
    @Test
    void createLevel() {
        Level mockLevel = new Level(1L, "testName", "testDesc", List.of());

        when(service.createLevel(Mockito.any(Level.class))).thenReturn(mockLevel);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/level").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"topics\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void getLevels() {
        when(service.findAllLevels()).thenReturn(List.of(new Level(1L, "testName", "testDesc", List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/level")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        JSONAssert.assertEquals(EXPECTED_ARR, mvcResult.getResponse().getContentAsString(), false);

    }

    @SneakyThrows
    @Test
    void getOneLevel() {
        when(service.findLevelById(anyLong())).thenReturn(Optional.of(new Level(1L, "testName", "testDesc", List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/level/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());


        JSONAssert.assertEquals(EXPECTED_OBJ, mvcResult.getResponse().getContentAsString(), false);

    }

    @SneakyThrows
    @Test
    void updateLevel() {
        Level mockLevel = new Level(1L, "testName", "testDesc", List.of());

        when(service.updateLevel(anyLong(), Mockito.any(Level.class))).thenReturn(mockLevel);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/level/1").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"topics\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void removeLevel() {
        when(service.deleteLevel(anyLong())).thenReturn(anyBoolean());

        MockHttpServletRequestBuilder requestBuilder = delete("/level/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @SneakyThrows
    @Test
    void addTopicToLevel() {
        when(service.addTopicToLevel(anyLong(),anyLong()))
                .thenReturn(new Level(1L, "testName", "testDesc", List.of(new Topic(1L, "testName", "testDesc", false, Priority.LOW, any(Category.class), List.of(), List.of()))));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/level/1/add/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"topics\":[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void deleteTopicFromLevel() {
        when(service.removeTopicFromLevel(anyLong(),anyLong()))
                .thenReturn(new Level(1L, "testName", "testDesc", List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/level/1/delete/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_OBJ,
                mvcResult.getResponse().getContentAsString(), false);
    }
}