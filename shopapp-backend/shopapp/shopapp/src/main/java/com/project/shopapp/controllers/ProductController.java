package com.project.shopapp.controllers;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.services.iProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@AllArgsConstructor

public class ProductController {
    private final iProductService productService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //POST: http://localhost:8088/api/v1/products
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            //@RequestPart("file") MultipartFile file,
            BindingResult result
    ){
        try{
            //Save the product
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null? new ArrayList<MultipartFile>() : files;
            for (MultipartFile file : files){
                if(file.getSize() == 0){
                    continue;
                }
                //Kiem tra kich thuoc file va dinh dang
                if(file.getSize() > 10 * 1024 * 1024){
                    //Kich thuoc phai lon hon 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                //Luu file va cap nhat thumbnail trong DTO
                String filename = storeFile(file); //Thay the ham nay voi code cua ban de luu file
                //Luu vao product trong database
                ProductImage productImage = productService.createProductImage(
                        newProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build()
                );
            }
            return ResponseEntity.ok("Product created successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException{
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        //Them UUID vao truoc ten file de dam bao file la duy nhat
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //Duong dan den thu muc ban muon luu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        //Kiem tra va tao thu muc neu no khong ton tai
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //Duong dan day du den file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        //Sao chep file vao thu muc dich neu trung thi ghi de
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping("") //http://localhost:8088/api/v1/products?page=1&limit=12
    public ResponseEntity<String> getProducts(
            @RequestParam("page")   int page,
            @RequestParam("limit")  int limit
    ){
        return ResponseEntity.ok("get products here");
    }
    //http://localhost:8088/api/v1/products/6
    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") String productId){
        return ResponseEntity.ok("Product with ID: " + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", id));
    }
}
