package cloud.mtech.roche.controller;

import cloud.mtech.roche.dto.BasicProduct;
import cloud.mtech.roche.dto.Product;
import cloud.mtech.roche.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService productService;
    @Mock
    private BasicProduct basicProduct;
    @Mock
    private Product product;

    private ProductController productController;

    @BeforeEach
    public void setup() {
        productController = new ProductController(productService);
    }

    @Test
    public void fetch() {
        when(productService.fetch(1234L)).thenReturn(product);

        Product actual = productController.fetch(1234L);

        assertThat(actual).isEqualTo(product);
    }

    @Test
    public void create() {
        when(productService.save(basicProduct)).thenReturn(product);

        Product actual = productController.create(basicProduct);

        assertThat(actual).isEqualTo(product);
    }

    @Test
    public void update() {
        when(productService.update(123L, basicProduct)).thenReturn(product);

        Product actual = productController.update(123L, basicProduct);

        assertThat(actual).isEqualTo(product);
    }
}