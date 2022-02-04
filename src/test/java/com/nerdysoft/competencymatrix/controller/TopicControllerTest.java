package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.service.TopicService;
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
@WebMvcTest(value = TopicController.class)
class TopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService service;

    private static final String EXPECTED_ARR = "[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}]";
    private static final String EXPECTED_OBJ = "{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}";


    @SneakyThrows
    @Test
    void createTopic() {
        Topic mockTopic = new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of());

        when(service.createTopic(Mockito.any(Topic.class))).thenReturn(mockTopic);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/topic").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void getTopics() {
        when(service.findAllTopics()).thenReturn(List.of(new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/topic")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_ARR, mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void getOneTopic() {
        when(service.findTopicById(anyLong())).thenReturn(Optional.of(new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/topic/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());


        JSONAssert.assertEquals(EXPECTED_OBJ, mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void updateTopic() {
        Topic mockTopic = new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of());

        when(service.updateTopic(anyLong(), Mockito.any(Topic.class))).thenReturn(mockTopic);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/topic/1").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void removeTopic() {
        when(service.deleteTopic(anyLong())).thenReturn(anyBoolean());

        MockHttpServletRequestBuilder requestBuilder = delete("/topic/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @SneakyThrows
    @Test
    void addItemToTopic() {
        when(service.addItemToTopic(anyLong(),anyLong()))
                .thenReturn(new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(new Item(1L, "testLabel", List.of())), List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/topic/1/add/item/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[{\"id\":1,\"label\":\"testLabel\",\"resources\":[]}],\"resources\":[]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void deleteItemFromTopic() {
        when(service.removeItemFromTopic(anyLong(),anyLong()))
                .thenReturn(new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/topic/1/delete/item/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_OBJ,
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void addResourceToTopic() {
        when(service.addResourceToTopic(anyLong(),anyLong()))
                .thenReturn(new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of(new Resource(1L, "testName", "testDesc", "testUrl"))));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/topic/1/add/resource/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"url\":\"testUrl\"}]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void deleteResourceFromTopic() {
        when(service.removeResourceFromTopic(anyLong(),anyLong()))
                .thenReturn(new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/topic/1/delete/resource/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_OBJ,
                mvcResult.getResponse().getContentAsString(), false);
    }
}