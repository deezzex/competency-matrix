package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.service.ResourceService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(value = ResourceController.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceService service;

    private static final String EXPECTED_OBJ = "{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"url\":\"testUrl\"}";
    private static final String EXPECTED_ARR = "[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"url\":\"testUrl\"}]";


    @SneakyThrows
    @Test
    void createResource() {
        Resource mockResource = new Resource(1L, "testName", "testDesc", "testUrl");

        when(service.createResource(Mockito.any(Resource.class))).thenReturn(mockResource);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/resource").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"url\":\"testUrl\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void getResource() {
        when(service.findAllResources()).thenReturn(List.of(new Resource(1L, "testName", "testDesc", "testUrl")));

        MockHttpServletRequestBuilder requestBuilder = get("/resource")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse().getStatus());


        JSONAssert.assertEquals(EXPECTED_ARR, mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void getOneResource() {
        when(service.findResourceById(anyLong())).thenReturn(Optional.of(new Resource(1L, "testName", "testDesc", "testUrl")));

        MockHttpServletRequestBuilder requestBuilder = get("/resource/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());


        JSONAssert.assertEquals(EXPECTED_OBJ, mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void updateResource() {
        Resource mockResource = new Resource(1L, "testName", "testDesc", "testUrl");

        when(service.updateResource(anyLong(), Mockito.any(Resource.class))).thenReturn(mockResource);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/resource/1").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"url\":\"testUrl\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void removeResource() {
        when(service.deleteResource(anyLong())).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = delete("/resource/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }
}