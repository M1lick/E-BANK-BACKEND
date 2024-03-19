package com.example.ebankbackend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class OperationsHistoryDTO {
    private  String id;
    private  double solde;
    private  int currenPage;
    private  int totalPages;
    private  int pageSize;
    private  List<OperationDTO> operationDTOList;
}
