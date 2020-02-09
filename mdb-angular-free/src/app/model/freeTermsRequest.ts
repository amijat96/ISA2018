export class FreeTermsRequest{
    constructor(
        public clinicId: number = 0,
        public roomTypeId: number = 0,
        public doctorId: number = 0,
        public dateTime: Date = new Date(),
        public duration: string = ''
    ) {}
}