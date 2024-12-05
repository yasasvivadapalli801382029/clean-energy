import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthguardService } from '../authguard/authguard.service';
import { tap } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SalesService {

  
  baseUrl:string = environment.apiUrl;

  constructor(private httpClient:HttpClient,private authguardService:AuthguardService) { }


  fetchSalesData(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const token = this.authguardService.getTokenValue();  // Retrieve the token from your auth service
  
      // Create headers with the token
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
      this.httpClient.get<any>(`${this.baseUrl}/sales`, { headers })
        .subscribe(
          (response) => {
            resolve(response);  // Resolve the response
          },
          (error) => {
            console.error("Error fetching sales data:", error);  // Log error
            reject(error);  // Reject the promise with the error
          }
        );
    });
  }

  fetchProductCategories(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const token = this.authguardService.getTokenValue();  // Retrieve the token from your auth service
  
      // Create headers with the token
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
      this.httpClient.get<any>(`${this.baseUrl}/product_categories`, { headers })
        .subscribe(
          (response) => {
            resolve(response);  // Resolve the response
          },
          (error) => {
            console.error("Error fetching product categories:", error);  // Log error
            reject(error);  // Reject the promise with the error
          }
        );
    });
  }

  fetchSalesDatawithProductCategory(productCategory:string): Promise<any> {

    return new Promise<any>((resolve, reject) => {
      const token = this.authguardService.getTokenValue();  // Retrieve the token from your auth service
  
      // Create headers with the token
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
      this.httpClient.get<any>(`${this.baseUrl}/sales_by_product?productCategory=${productCategory}`, { headers })
        .subscribe(
          (response) => {
            resolve(response);  // Resolve the response
          },
          (error) => {
            console.error("Error fetching sales data:", error);  // Log error
            reject(error);  // Reject the promise with the error
          }
        );
    });

      

  }
  
  
  
}
