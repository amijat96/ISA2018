export class Examination{
    constructor(
        public examinationId: number = 0,
        public userId: number = 0,
        public userUsername: string = '',
        public roomId: number = 0,
        public roomNumber: string = '',
        public roomFloor: number = 0,
        public typeName: string = '',
        public priceListId: number = 0,
        public price: number = 0,
        public predefined: boolean = false,
        public finished: boolean = true,
        public discount: number = 0,
        public gradeClinic: number = 0,
        public gradeDoctor: number = 0,
        public accepted: boolean = true,
        public dateTime: Date = new Date(),
        public doctorId: number = 0,
        public doctorUsername: string = '',
        public roomType: string = '',
        public typeId: number = 0,
        public duration: string = '',
        public reportId: number = 0
    ) {}
}