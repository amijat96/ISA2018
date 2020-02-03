export class FreeTerm {
    constructor(
        public roomId: number = 0,
        public startDateTime: Date = new Date(),
        public endDateTime: Date = new Date()
    ) {}
}