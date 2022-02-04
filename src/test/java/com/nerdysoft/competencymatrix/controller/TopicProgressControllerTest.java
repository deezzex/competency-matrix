package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.TopicProgress;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.service.TopicProgressService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(value = TopicProgressController.class)
class TopicProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicProgressService service;

    private static final String EXPECTED_OBJ = "{\"id\":1,\"comment\":\"testComment\",\"mark\":10,\"finished\":false,\"topic\":{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}}";
    public static final String EXPECTED_ARR = "[{\"id\":1,\"comment\":\"testComment\",\"mark\":10,\"finished\":false,\"topic\":{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}}]";


    @SneakyThrows
    @Test
    void createProgress() {
        TopicProgress mockProgress = new TopicProgress(1L, "testComment", 10, false, new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of()));

        when(service.createProgress(Mockito.any(TopicProgress.class))).thenReturn(mockProgress);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/progress").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"comment\":\"testComment\",\"mark\":10,\"finished\":false,\"topic\":{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void addTopicToProgress() {
        when(service.addTopicToProgress(anyLong(),anyLong()))
                .thenReturn(new TopicProgress(1L, "testComment", 10, false, new Topic(1L, "testName", "testDescNEW", false, Priority.LOW, List.of(), List.of())));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/progress/1/add/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"comment\":\"testComment\",\"mark\":10,\"finished\":false,\"topic\":{\"id\":1,\"name\":\"testName\",\"description\":\"testDescNEW\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void checkEnd() {
        when(service.isSuccessfullyEndedLevel()).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = get("/progress/end")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @SneakyThrows
    @Test
    void evaluateProgress() {
        when(service.setCommentAndMarkToProgress(anyLong(), any(TopicProgress.class)))
                .thenReturn(new TopicProgress(1L, "testCommentNEW", 5, false, new Topic(1L, "testName", "testDescNEW", false, Priority.LOW, List.of(), List.of())));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/progress/evaluate/1").accept(MediaType.APPLICATION_JSON)
                .content("{\"comment\":\"testCommentNEW\",\"mark\":5}")
                .contentType(MediaType.APPLICATION_JSON);;

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"comment\":\"testCommentNEW\",\"mark\":5,\"finished\":false,\"topic\":{\"id\":1,\"name\":\"testName\",\"description\":\"testDescNEW\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void finishProgress() {
        when(service.finishProgress(anyLong()))
                .thenReturn(new TopicProgress(1L, "testCommentNEW", 5, true, new Topic(1L, "testName", "testDescNEW", false, Priority.LOW, List.of(), List.of())));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/progress/finish/1").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"comment\":\"testCommentNEW\",\"mark\":5,\"finished\":true,\"topic\":{\"id\":1,\"name\":\"testName\",\"description\":\"testDescNEW\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}}",
                mvcResult.getResponse().getContentAsString(), false);
    }
}