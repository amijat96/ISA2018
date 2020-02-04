export class Clinic {
    constructor(
        public clinicId: number = 0,
        public name: string = '',
        public countryId: number = 0,
        public cityId: number = 0,
        public street: string = '',
        public workTimeStart: string = '',
        public workTimeEnd: string = '',
        public description: string = ''
    ) {}
}