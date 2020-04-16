package cloud.mtech.roche.service;

import cloud.mtech.roche.dto.BasicProduct;
import cloud.mtech.roche.dto.Product;
import cloud.mtech.roche.entity.ProductEntity;
import cloud.mtech.roche.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProductEntity productEntity;
    @Mock
    private BasicProduct basicProduct;
    @Mock
    private Product product;

    private ProductService productService;

    @BeforeEach
    public void setup() {
        productService = new ProductService(productRepository, modelMapper);
    }

    @Test
    public void saveWithNullProductThrowsNPE() {
        assertThrows(
          NullPointerException.class,
          () -> productService.save(null)
        );
    }

    @Test
    public void save() {
        when(modelMapper.map(basicProduct, ProductEntity.class)).thenReturn(productEntity);
        when(modelMapper.map(productEntity, Product.class)).thenReturn(product);
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        Product actual = productService.save(basicProduct);

        assertThat(actual).isEqualTo(product);
    }
}