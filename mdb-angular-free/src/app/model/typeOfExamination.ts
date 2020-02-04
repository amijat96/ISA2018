export class TypeOfExamination {
    constructor(
        public name: string = '',
        public description: string ='',
        public roomTypeId: number = 0,
        public typeName: string = '',
        public duration: string = '',
        public id: number = 0,
        public numberOfDoctorsOrPriceLists?: number
    ) { }
}