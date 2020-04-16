package cloud.mtech.roche.service;

import cloud.mtech.roche.dto.BasicProduct;
import cloud.mtech.roche.dto.Product;
import cloud.mtech.roche.entity.ProductEntity;
import cloud.mtech.roche.repository.ProductRepository;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Validated
public class ProductService {

    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public Product fetch(@NonNull @NotNull Long sku) {
        return productRepository.findById(sku)
          .map(this::mapToDto)
          .orElseThrow(() -> entityNotFound(sku));
    }

    public Product save(@NonNull @Valid BasicProduct product) {
        return Optional.of(product)
          .map(this::mapToEntity)
          .map(productRepository::save)
          .map(this::mapToDto)
          .orElseThrow();
    }

    public Product update(@NonNull @NotNull Long sku, @NonNull @Valid BasicProduct product) {
        return Optional.of(sku)
          .flatMap(productRepository::findById)
          .map(entity -> mapToEntity(product, entity))
          .map(productRepository::save)
          .map(this::mapToDto)
          .orElseThrow(() -> entityNotFound(sku));
    }

    public void delete(@NonNull @NotNull Long sku) {
        productRepository.findById(sku)
          .ifPresentOrElse(productRepository::delete, () -> throwEntityNotFoundException(sku));
    }

    private ProductEntity mapToEntity(BasicProduct source, ProductEntity dest) {
        modelMapper.map(source, dest);
        return dest;
    }

    private ProductEntity mapToEntity(BasicProduct product) {
        return modelMapper.map(product, ProductEntity.class);
    }

    private Product mapToDto(ProductEntity product) {
        return modelMapper.map(product, Product.class);
    }

    private void throwEntityNotFoundException(Long sku) {
        throw entityNotFound(sku);
    }

    private EntityNotFoundException entityNotFound(Long sku) {
        return new EntityNotFoundException(format("Product %d not found", sku));
    }
}
