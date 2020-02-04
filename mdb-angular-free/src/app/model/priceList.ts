export class PriceList {
    constructor(
        public priceListId: number = 0,
        public typeOfExaminationId: number = 0,
        public clinicId: number = 0,
        public typeOfExaminationName: string = '',
        public price: number = 0,
        public priceListExaminations?: number
    ) { }
}