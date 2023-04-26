package com.ssafy.semes.transition.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.semes.common.SuccessCode;
import com.ssafy.semes.common.dto.ApiResponse;
import com.ssafy.semes.image.model.ImageListResponseDto;
import com.ssafy.semes.transition.model.TransitionDeleteRequestDto;
import com.ssafy.semes.transition.model.TransitionUpdateRequestDto;
import com.ssafy.semes.transition.model.service.TransitionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transition")
@Slf4j
public class TransitionController {
    @Autowired
    private TransitionService transitionService;

    @GetMapping()
    public ApiResponse<?> findAllBolt(){
        log.info("ImageController getImages start");
        List<ImageListResponseDto> responseDto = transitionService.findAll();
        return 	ApiResponse.success(SuccessCode.CREATE_FILE,responseDto);

    }
    @DeleteMapping
    public ApiResponse<?> deleteBolt(@RequestBody TransitionDeleteRequestDto dto){
        StringBuilder sb = new StringBuilder();
        for (int id:
             dto.getFileIds()) {
            sb.append(id).append(",");
        }
        sb.append("볼트 삭제 성공");
        return ApiResponse.success(SuccessCode.DELETE_IMG,sb.toString());
    }
    @PutMapping
    public ApiResponse<?> updateBolt(@RequestBody TransitionUpdateRequestDto files){

        return ApiResponse.success(SuccessCode.UPDATE_IMG,"볼트 이동 성공");
    }

}
