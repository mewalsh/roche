package cloud.mtech.roche.controller;

import cloud.mtech.roche.dto.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ProductControllerIT {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createProduct() throws Exception {
        Product product = Product.builder()
          .name("test")
          .price(BigDecimal.TEN)
          .build();

        String expected = mapper.writeValueAsString(product);

        mockMvc.perform(post("/api/products")
          .content(mapper.writeValueAsBytes(product)))
          .andExpect(status().isCreated())
          .andExpect(content().json(expected))
          .andExpect(jsonPath("$.sku", notNullValue()))
          .andExpect(jsonPath("$.created", notNullValue()));
    }
}
