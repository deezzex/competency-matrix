package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.TopicProgress;
import com.nerdysoft.competencymatrix.entity.User;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.service.UserService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(value = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    private static final String EXPECTED_OBJ = "{\"id\":1,\"firstName\":\"first\",\"lastName\":\"last\",\"matrices\":[],\"topicProgressList\":[]}";

    @SneakyThrows
    @Test
    void createUser() {
        User mockUser = new User(1L, "first", "last", List.of(), List.of());

        when(service.createUser(Mockito.any(User.class))).thenReturn(mockUser);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"first\",\"lastName\":\"last\",\"matrices\":[],\"topicProgressList\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void getOneUser() {
        when(service.findUserById(anyLong())).thenReturn(Optional.of(new User(1L, "first", "last", List.of(), List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/user/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        JSONAssert.assertEquals(EXPECTED_OBJ, mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void addMatrixToUser() {
        when(service.addMatrixToUser(anyLong(),anyLong()))
                .thenReturn(new User(1L, "first", "last", List.of(new Matrix(1L, "testName", "testDesc", List.of(), List.of())), List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/1/add/matrix/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"firstName\":\"first\",\"lastName\":\"last\",\"matrices\":[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"competencies\":[],\"levels\":[]}],\"topicProgressList\":[]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void addProgressToUser() {
        when(service.addProgressToUser(anyLong(),anyLong()))
                .thenReturn(new User(1L, "first", "last", List.of(), List.of(new TopicProgress(1L, "testComment", 10, false, new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of())))));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/1/add/progress/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"firstName\":\"first\",\"lastName\":\"last\",\"matrices\":[],\"topicProgressList\":[{\"id\":1,\"comment\":\"testComment\",\"mark\":10,\"finished\":false,\"topic\":{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}}]}",
                mvcResult.getResponse().getContentAsString(), false);
    }
}