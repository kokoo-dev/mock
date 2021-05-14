package com.example.mock.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/mock")
public class MockTestController {
    Logger logger = LogManager.getLogger(MockTestController.class);

    @RequestMapping("/callBasic")
    public void callBasic(HttpServletRequest request){
        logger.info("callBasic method:: " + request.getMethod());
    }

    @GetMapping("/callCheckReturnStr")
    public String callCheckReturnStr(String data){
        String returnData = data.concat("_return");
        logger.info("callCheckReturn returnData:: " + returnData);

        return returnData;
    }

    @GetMapping("/callCheckReturnModel")
    public ModelAndView callCheckReturnModel(){
        logger.info("callCheckReturnModel");

        ModelAndView mav = new ModelAndView();
        mav.addObject("key", "value");

        return mav;
    }

    @GetMapping("/callWithParam")
    public void callWithParam(String data, String[] arrayData){
        logger.info("callWithParam data:: " + data);

        for(String s : arrayData){
            logger.info("callWithParam arrayData:: " + s);
        }
    }

    @PostMapping("/callWithOneFile")
    public void callWithOneFile(MultipartFile file){
        logger.info("callWithOneFile fileName:: " + file.getOriginalFilename());
        logger.info("callWithOneFile fileSize:: " + file.getSize());
    }

    @PostMapping("/callWithMultiFile")
    public void callWithMultiFile(MultipartHttpServletRequest mtfRequest){
        List<MultipartFile> files = mtfRequest.getFiles("file");

        for(MultipartFile file : files){
            logger.info("callWithMultiFile fileName:: " + file.getOriginalFilename());
        }
    }

}
