package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.request.PriceListRequestDTO;
import com.example.backend.dto.response.PriceListResponseDTO;
import com.example.backend.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/priceList")
public class PriceListController {

    private final PriceListService priceListService;

    @Autowired
    public PriceListController(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<PriceListResponseDTO> getPriceList(@PathVariable Integer id) {
        return ResponseEntity.ok(new PriceListResponseDTO(priceListService.getPriceListById(id)));
    }

    @GetMapping
    public ResponseEntity<List<PriceListResponseDTO>> getPriceLists(@PathParam("id") Integer id) {
        return ResponseEntity.ok(priceListService.getPriceListByClinic(id)
                                                .stream()
                                                .map(PriceListResponseDTO::new)
                                                .collect(Collectors.toList()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<PriceListResponseDTO> createPriceList(@Valid @RequestBody PriceListRequestDTO priceListRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new PriceListResponseDTO(priceListService.createPriceList(priceListRequestDTO)));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<PriceListResponseDTO> updatePriceList(@PathVariable Integer id, @Valid @RequestBody PriceListRequestDTO priceListRequestDTO) {
        return ResponseEntity.ok(new PriceListResponseDTO(priceListService.updatePriceList(id, priceListRequestDTO)));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<ApiResponse> deletePriceList(@PathVariable Integer id) {
        if(priceListService.deletePriceList(id)) {
            return ResponseEntity.ok(new ApiResponse(true, "Price list deleted successfully.", new ArrayList<>()));
        }
        else {
            return ResponseEntity.ok(new ApiResponse(false, "Could not delete price list with id : " + id, new ArrayList<>()));
        }
    }
}
