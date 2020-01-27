export class User {
    constructor(
        public email: string,
        public gender: number,
        public firstName: string,
        public lastName: string,
        public passportNumber: string,
        public phoneNumber: string,
        public username: string,
        public street: string,
        public id: number,
        public roleId: number,
        public dateOfBirth: string,
        public jbo: string,
        public doctorGrade: number,
        public cityId: number,
        public clinicId?: number,
        
    ) { }
}