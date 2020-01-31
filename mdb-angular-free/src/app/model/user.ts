export class User {
    constructor(
        public email: string = '',
        public gender: number = 0,
        public firstName: string = '',
        public lastName: string = '',
        public phoneNumber: string = '',
        public username: string = '',
        public street: string = '',
        public id: number = 0,
        public roleId: number = 0,
        public dateOfBirth: string = '',
        public jbo: string = '',
        public doctorGrade: number = 0,
        public cityId: number = 0,
        public countryId: number = 0,
        public numberOfSchedules: number = 0,
        public passwordChanged: boolean = true,    
        public clinicId?: number
    ) { }
}