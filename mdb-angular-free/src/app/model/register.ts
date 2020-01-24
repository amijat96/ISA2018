export class Register {
    constructor(
        public email: string = "",
        public gender: number = 0,
        public name: string  = "",
        public lastName: string = "",
        public phoneNumber: string = "",
        public username: string = "",
        public password: string = "",
        public cityId: number = 0,
        public street: string = "",
        public jbo: string = "",
        public dateOfBirth: string = "",
    ) { }
}