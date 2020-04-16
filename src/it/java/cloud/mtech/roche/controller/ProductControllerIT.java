package cloud.mtech.roche.controller;

import cloud.mtech.roche.dto.BasicProduct;
import cloud.mtech.roche.dto.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ProductControllerIT {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    private BasicProduct product;

    @BeforeEach
    public void setup() {
        product = BasicProduct.builder()
          .name("test")
          .price(BigDecimal.TEN)
          .build();
    }

    @Test
    public void createProduct() throws Exception {
        String expected = mapper.writeValueAsString(product);

        mockMvc.perform(post("/api/products")
          .contentType(MediaType.APPLICATION_JSON)
          .content(expected))
          .andExpect(status().isCreated())
          .andExpect(content().json(expected))
          .andExpect(jsonPath("$.sku", notNullValue()))
          .andExpect(jsonPath("$.created", notNullValue()));
    }

    @Test
    public void createInvalidProduct() throws Exception {
        BasicProduct product = BasicProduct.builder().build();

        String expected = "{" +
          "\"status\":400," +
          "\"errorMessage\":\"Validation failed. 2 error(s)\"," +
          "\"errors\":[" +
          "  \"save.product.name: must not be empty\"," +
          "  \"save.product.price: must not be null\"" +
          "]}";

        mockMvc.perform(post("/api/products")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsBytes(product)))
          .andExpect(status().isBadRequest())
          .andExpect(content().json(expected));
    }

    @Test
    public void createInvalidJson() throws Exception {

        mockMvc.perform(post("/api/products")
          .contentType(MediaType.APPLICATION_JSON)
          .content("bad_json"))
          .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchProduct() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/products")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsBytes(product)))
          .andExpect(status().isCreated())
          .andReturn();

        String response = result.getResponse().getContentAsString();
        Product product = mapper.readValue(response, Product.class);

        mockMvc.perform(get("/api/products/{sku}", product.getSku()))
          .andExpect(status().isOk())
          .andExpect(content().json(response));
    }

    @Test
    public void fetchUnknownProduct() throws Exception {
        String expected = "{" +
          "\"status\":404," +
          "\"errorMessage\":\"Product 9999 not found\"" +
          "}";

        mockMvc.perform(get("/api/products/9999"))
          .andExpect(status().isNotFound())
          .andExpect(content().json(expected));
    }

    @Test
    public void updateExistingProduct() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/products")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsBytes(product)))
          .andExpect(status().isCreated())
          .andReturn();

        String response = result.getResponse().getContentAsString();
        Product product = mapper.readValue(response, Product.class);

        product.setName("updated");
        String expected = mapper.writeValueAsString(product);

        mockMvc.perform(put("/api/products/{sku}", product.getSku())
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsBytes(product)))
          .andExpect(status().isOk())
          .andExpect(content().json(expected));

        mockMvc.perform(get("/api/products/{sku}", product.getSku()))
          .andExpect(status().isOk())
          .andExpect(content().json(expected));
    }

    @Test
    public void updateUnknownProduct() throws Exception {
        Product product = Product.builder()
          .sku(9999L)
          .name("existing_sku")
          .price(BigDecimal.ONE)
          .build();

        String expected = "{" +
          "\"status\":404," +
          "\"errorMessage\":\"Product 9999 not found\"" +
          "}";

        mockMvc.perform(put("/api/products/{sku}", product.getSku())
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsBytes(product)))
          .andExpect(status().isNotFound())
          .andExpect(content().json(expected));

    }

    @Test
    public void deleteExistingProduct() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/products")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsBytes(product)))
          .andExpect(status().isCreated())
          .andReturn();

        String response = result.getResponse().getContentAsString();
        Product product = mapper.readValue(response, Product.class);

        mockMvc.perform(delete("/api/products/{sku}", product.getSku()))
          .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/{sku}", product.getSku()))
          .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUnknownProduct() throws Exception {
        String expected = "{" +
          "\"status\":404," +
          "\"errorMessage\":\"Product 9999 not found\"" +
          "}";

        mockMvc.perform(delete("/api/products/9999"))
          .andExpect(status().isNotFound())
          .andExpect(content().json(expected));

    }
}
