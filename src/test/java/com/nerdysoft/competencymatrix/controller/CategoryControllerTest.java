package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.service.CategoryService;
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
@WebMvcTest(value = CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService service;

    private static final String EXPECTED_OBJ = "{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"type\":\"HARD_SKILL\",\"priority\":\"LOW\",\"topics\":[]}";
    public static final String EXPECTED_ARR = "[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"type\":\"HARD_SKILL\",\"priority\":\"LOW\",\"topics\":[]}]";

    @SneakyThrows
    @Test
    void createCategory() {
        Category mockCategory = new Category(1L, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of());

        when(service.createCategory(Mockito.any(Category.class))).thenReturn(mockCategory);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"type\":\"HARD_SKILL\",\"priority\":\"LOW\",\"topics\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void getCategories() {
        when(service.findAllCategories()).thenReturn(List.of(new Category(1L, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/category")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_ARR, mvcResult.getResponse().getContentAsString(), false);

    }

    @SneakyThrows
    @Test
    void getOneCategory() {
        when(service.findCategoryById(anyLong())).thenReturn(Optional.of(new Category(1L, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/category/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());


        JSONAssert.assertEquals(EXPECTED_OBJ, mvcResult.getResponse().getContentAsString(), false);

    }

    @SneakyThrows
    @Test
    void updateCategory() {
        Category mockCategory = new Category(1L, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of());

        when(service.updateCategory(anyLong(), Mockito.any(Category.class))).thenReturn(mockCategory);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/category/1").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"type\":\"HARD_SKILL\",\"priority\":\"LOW\",\"topics\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void removeCategory() {
        when(service.deleteCategory(anyLong())).thenReturn(anyBoolean());

        MockHttpServletRequestBuilder requestBuilder = delete("/category/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @SneakyThrows
    @Test
    void addTopicToCategory() {
        when(service.addTopicToCategory(anyLong(),anyLong()))
                .thenReturn(new Category(1L, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of(new Topic(1L, "testName", "testDesc", false, Priority.LOW, List.of(), List.of()))));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category/1/add/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"type\":\"HARD_SKILL\",\"priority\":\"LOW\",\"topics\":[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"required\":false,\"priority\":\"LOW\",\"items\":[],\"resources\":[]}]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void deleteTopicFromCategory() {
        when(service.removeTopicFromCategory(anyLong(),anyLong()))
                .thenReturn(new Category(1L, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/category/1/delete/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_OBJ,
                mvcResult.getResponse().getContentAsString(), false);
    }
}