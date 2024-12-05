import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthguardService } from '../authguard/authguard.service';
import { environment } from 'src/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class UserauthenticationService {

  baseUrl:string = environment.apiUrl;

  constructor(private httpClient:HttpClient,private authguardService:AuthguardService) { }



  login(userEmail: string, userPassword: string): Promise<any> {

    const payload = {
      userEmail: userEmail,
      userPassword: userPassword
    };



    return new Promise<any>((resolve, reject) => {

      this.httpClient.post<any>(`${this.baseUrl}/login`, payload, { observe: 'response' })
        .pipe(
          tap((response: HttpResponse<any>) => {
            const token = response.headers.get('Authorization');
            this.authguardService.setToken(token);
            this.authguardService.setAuthenticatedTrue();
            
          })
        ).subscribe(
          (response) => {
            resolve(response);
          }, (error) => {
            console.log("Error!");
            reject(error);
          });
    });

  }

  signup(userLoginRequest:any):Observable<any>{

    return this.httpClient.post(`${this.baseUrl}/register`,userLoginRequest);
  }


}
