import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions} from './constants';
import { PriceList } from '../model/priceList';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PriceListService {

  constructor(private httpClient: HttpClient) { }

  getPriceList():Observable<PriceList[]> {
    return this.httpClient.get<PriceList[]>(baseUrl + 'price-list/byClinic/' + localStorage.getItem('clinicId'), httpOptions);
  }

  createPriceListItem(priceListItem: PriceList) {
    priceListItem.clinicId = Number(localStorage.getItem('clinicId'));
    return this.httpClient.post(baseUrl + 'price-list', priceListItem, httpOptions);
  }

  updatePriceListItem(priceListItem: PriceList) {
    return this.httpClient.put(baseUrl + 'price-list/' + priceListItem.priceListId, priceListItem, httpOptions);
  }

  deletePriceListItem(id: number) {
    return this.httpClient.delete(baseUrl + 'price-list/' + id, httpOptions);
  }

 
}