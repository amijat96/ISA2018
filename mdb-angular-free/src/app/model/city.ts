import { Country } from "./country";

export class City {
  constructor(
      public cityId: number, 
      public name: String, 
      public country: Country
      ) {}
}