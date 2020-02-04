export class Vacation{
    constructor(
        public vacationId: number = 0,
        public userId: number = 0,
        public userUsername: string = '',
        public startDate: string = '',
        public endDate: string = '',
        public description: string = '',
        public accepted: string = ''
    ) {}
}