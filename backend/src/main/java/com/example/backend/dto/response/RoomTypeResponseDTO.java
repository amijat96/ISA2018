package com.example.backend.dto.response;

import com.example.backend.model.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeResponseDTO {

    private Integer roomTypeId;

    private String name;

    public RoomTypeResponseDTO(RoomType roomType) {
        this.roomTypeId = roomType.getRoomTypeId();
        this.name = roomType.getName();
    }
}
