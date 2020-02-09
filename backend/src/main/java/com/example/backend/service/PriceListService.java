package com.example.backend.service;

import com.example.backend.dto.request.PriceListRequestDTO;
import com.example.backend.model.PriceList;

import java.util.List;

public interface PriceListService{

    PriceList getPriceListById(Integer priceListId);

    List<PriceList> getPriceListByClinic(Integer clinicId);

    PriceList createPriceList(PriceListRequestDTO priceListRequestDTO);

    PriceList updatePriceList(Integer priceListId, PriceListRequestDTO priceListRequestDTO);

    boolean deletePriceList(Integer priceListId);
}
