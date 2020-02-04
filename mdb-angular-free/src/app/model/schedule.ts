export class Schedule{
    constructor(
        public scheduleId: number = 0,
        public doctorId: number = 0,
        public doctorUsername: string = '',
        public doctorName: string = '',
        public doctorLastName: string = '',
        public role: string = '',
        public startDate: string = '',
        public endDate: string = '',
        public shiftStartTime: string = '',
        public shiftEndTime: string = '',
        public numberOfExaminations: number = 0
    ) {}
}