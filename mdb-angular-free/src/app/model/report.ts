import { Diagnosis } from './diagnosis';
import { Medicine } from './medicine';

export class Report {
    constructor(
        public reportId: number = 0,
        public nurseId: number = 0,
        public description: string = '',
        public certified: boolean = false,
        public diagnoses: Diagnosis[] = [],
        public medicines: Medicine[] = []
    ) {}
}