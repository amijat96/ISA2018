export class ReportRequest {
    constructor(
        public fromDate: string = '',
        public toDate: string = '',
        public frequency: number = 0
    ) { }
}