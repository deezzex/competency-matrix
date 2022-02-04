package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.service.ItemService;
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
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(value = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService service;

    private static final String EXPECTED_ARR = "[{\"id\":1,\"label\":\"testLabel\",\"resources\":[]}]";
    private static final String EXPECTED_OBJ = "{\"id\":1,\"label\":\"testLabel\",\"resources\":[]}";


    @SneakyThrows
    @Test
    void createItem() {
        Item mockItem = new Item(1L, "testLabel", List.of());

        when(service.createItem(Mockito.any(Item.class))).thenReturn(mockItem);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/item").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"label\":\"testLabel\",\"resources\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void getItem() {
        when(service.findAllItems()).thenReturn(List.of(new Item(1L, "testLabel", List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/item")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_ARR, mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void getOneItem() {
        when(service.findItemById(anyLong())).thenReturn(Optional.of(new Item(1L, "testLabel", List.of())));

        MockHttpServletRequestBuilder requestBuilder = get("/item/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());


        JSONAssert.assertEquals(EXPECTED_OBJ, mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        Item mockItem = new Item(1L, "testLabel", List.of());

        when(service.updateItem(anyLong(), Mockito.any(Item.class))).thenReturn(mockItem);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/item/1").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"label\":\"testLabel\",\"resources\":[]}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(EXPECTED_OBJ, response.getContentAsString());
    }

    @SneakyThrows
    @Test
    void removeItem() {
        when(service.deleteItem(anyLong())).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = delete("/item/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResult.getResponse());

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @SneakyThrows
    @Test
    void addResourceToItem() {
        when(service.addResourceToItem(anyLong(),anyLong()))
                .thenReturn(new Item(1L, "testLabel", List.of(new Resource(1L, "testName", "testDesc", "testUrl"))));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/item/1/add/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("{\"id\":1,\"label\":\"testLabel\",\"resources\":[{\"id\":1,\"name\":\"testName\",\"description\":\"testDesc\",\"url\":\"testUrl\"}]}",
                mvcResult.getResponse().getContentAsString(), false);
    }

    @SneakyThrows
    @Test
    void removeResourceFromItem() {
        when(service.removeResourceFromItem(anyLong(),anyLong()))
                .thenReturn(new Item(1L, "testLabel", List.of()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/item/1/delete/2").accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(EXPECTED_OBJ,
                mvcResult.getResponse().getContentAsString(), false);
    }
}