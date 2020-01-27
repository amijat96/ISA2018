export class Room {
    constructor(
        public number: string ="",
        public floor: number = 0,
        public roomType: string ="",
        public clinicId: number = 0,
        public roomId: number = 0,
        public numberOfExaminations: number = 0,
        public roomTypeId?: number
    ) {}
}