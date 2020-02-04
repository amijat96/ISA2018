import { AbstractControl } from "@angular/forms";

export class PasswordValidator {
  static MatchPassword(abstractControl: AbstractControl) {
    let password = abstractControl.get('passwordControl').value; // to get value in input tag
    let confirmPassword = abstractControl.get('confirmPasswordControl').value; // to get value in input tag
     if(password != confirmPassword) {
         abstractControl.get('confirmPasswordControl').setErrors({ MatchPassword: true });
     } else {
         return null;
     }
 }
}