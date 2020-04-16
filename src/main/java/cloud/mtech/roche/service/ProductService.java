package cloud.mtech.roche.service;

import cloud.mtech.roche.dto.BasicProduct;
import cloud.mtech.roche.dto.Product;
import cloud.mtech.roche.entity.ProductEntity;
import cloud.mtech.roche.repository.ProductRepository;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

@Service
@Validated
public class ProductService {

    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public Product save(@NonNull @Valid BasicProduct product) {
        return Optional.of(product)
          .map(this::mapToEntity)
          .map(productRepository::save)
          .map(this::mapToDto)
          .orElseThrow();
    }

    private ProductEntity mapToEntity(BasicProduct product) {
        return modelMapper.map(product, ProductEntity.class);
    }

    private Product mapToDto(ProductEntity product) {
        return modelMapper.map(product, Product.class);
    }
}
