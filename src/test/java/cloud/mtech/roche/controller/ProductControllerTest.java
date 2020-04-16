package cloud.mtech.roche.controller;

import cloud.mtech.roche.dto.BasicProduct;
import cloud.mtech.roche.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService productService;
    @Mock
    private BasicProduct basicProduct;

    private ProductController productController;

    @BeforeEach
    public void setup() {
        productController = new ProductController(productService);
    }

    @Test
    public void create() {
        productController.create(basicProduct);

        verify(productService).save(basicProduct);
    }
}