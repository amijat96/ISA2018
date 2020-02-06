export class DoctorReportRequest{
    constructor(
        public description: string = '',
        public diagnoses: number[] = [],
        public medicines: number[] = []
    ) {}
}