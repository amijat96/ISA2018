export class MedicalRecord{
    constructor(
        public medcalRecordId: number = 0,
        public bloodTypeRh: string = '',
        public height: number = 0.0,
        public weight: number = 0.0,
        public race: string = ''
    ) {}

}