package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.ItemFile;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

public class ItemFileDTO {

    @Getter
    @ToString
    public static class Response {
        //private String filePath;
        private String fileName;
    }

    public static ItemFileDTO.Response entityToDTO(ItemFile itemFile){
        ItemFileDTO.Response response = new ItemFileDTO.Response();
        //response.filePath = itemFile.getFilePath();
        response.fileName = itemFile.getFileName();
        return response;
    }

    public static List<ItemFileDTO.Response> entityListToDtoList(List<ItemFile> fileList) {
        return fileList.stream()
                .map(file -> ItemFileDTO.entityToDTO(file))
                .collect(Collectors.toList());
    }
}
