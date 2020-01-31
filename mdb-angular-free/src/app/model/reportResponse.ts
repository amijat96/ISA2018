import { ReportByFrequencyResponse } from './reportByFrequencyResponse';

export class ReportResponse {
    constructor(
        public revenues: number,
        public reports: ReportByFrequencyResponse[]
    ) { }
}