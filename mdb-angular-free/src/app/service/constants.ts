import { HttpHeaders } from "@angular/common/http";

export const baseUrl= "http://localhost:8081/";

export const TOKEN_KEY = "Bearer";

export const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};