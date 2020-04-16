package cloud.mtech.roche.controller;

import cloud.mtech.roche.dto.BasicProduct;
import cloud.mtech.roche.dto.Product;
import cloud.mtech.roche.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{sku}")
    public Product fetch(@PathVariable("sku") Long sku) {
        return productService.fetch(sku);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody BasicProduct basicProduct) {
        return productService.save(basicProduct);
    }

    @PutMapping("/{sku}")
    public Product update(@PathVariable("sku") Long sku, @RequestBody BasicProduct basicProduct) {
        return productService.update(sku, basicProduct);
    }

    @DeleteMapping("/{sku}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("sku") Long sku) {
        productService.delete(sku);
    }
}
