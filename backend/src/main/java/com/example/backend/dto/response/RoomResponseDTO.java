package com.example.backend.dto.response;

import com.example.backend.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {

    private Integer roomId;

    private Integer roomTypeId;

    private String number;

    private Integer floor;

    public RoomResponseDTO(Room room) {
        this.roomId = room.getRoomId();
        this.roomTypeId = room.getRoomType().getRoomTypeId();
        this.number = room.getNumber();
        this.floor = room.getFloor();
    }
}
