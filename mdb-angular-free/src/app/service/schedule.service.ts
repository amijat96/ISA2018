import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions} from './constants';
import { SchedulesByDate } from '../model/schedulesByDate';
import { Schedule } from '../model/schedule';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {

  constructor(private httpClient: HttpClient) { }

  getSchedules(schedulesRequest: SchedulesByDate) {
    schedulesRequest.clinicId = Number(localStorage.getItem('clinicId'));
    return this.httpClient.put<Schedule[]>(baseUrl + 'schedules', schedulesRequest, httpOptions);
  }

  createSchedule(schedule: Schedule) {
    return this.httpClient.post(baseUrl + 'schedules', schedule, httpOptions);
  }

  updateSchedule(schedule: Schedule) {
    return this.httpClient.put(baseUrl + 'schedules/' + schedule.scheduleId, schedule, httpOptions);
  }

  deleteSchedule(id: number) {
    return this.httpClient.delete(baseUrl + 'schedules/' + id, httpOptions);
  }
}
