package com.cloudcomputing.cloudcomputing.product;


import com.cloudcomputing.cloudcomputing.ExceptionHandler.NotFoundException;
import com.cloudcomputing.cloudcomputing.ExceptionHandler.UnAuthorizedException;
import com.cloudcomputing.cloudcomputing.imageService.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ProductImageService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;
    public String addImage(MultipartFile imgFile) throws IOException {

        try {
            String fileName = imageService.save(imgFile);
            return  imageService.getImageUrl(fileName);
        } catch (IOException e) {
            log.info("image service error: " + e);
            throw new UnAuthorizedException("Images are not saved, something went wrong !");
        }
    }


    public boolean deleteImg(String imgUrl) throws IOException {
        try{
            imageService.delete(imgUrl);
            return true;
        }catch (Exception e){
            log.info("image service error: " + e);
            throw new NotFoundException("the file is not be found");
        }
    }
}
