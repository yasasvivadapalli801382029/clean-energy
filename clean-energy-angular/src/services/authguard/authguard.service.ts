import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthguardService {

  private isUserAuthenticated: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private tokenKey: string = "JWTtoken";

  constructor(private router:Router,private jwtHelper:JwtHelperService) { }


  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean | UrlTree> {
    await this.checkTokenValidity(); // Wait for the asynchronous token validity check


    if (this.isAuthenticated()) {

      return true; // Allow access to the requested route

    } else {
      console.log('redirecting to signin');
      // User is not authenticated, redirect to the login page
      this.router.navigate(['/signin']);
      return false; // Deny access to the requested route
    }
  }


  public isAuthenticated(): boolean {
    return this.isUserAuthenticated.getValue();
  }
  public setAuthenticatedTrue(): void {
    this.isUserAuthenticated.next(true);
  }
  public setAuthenticatedFalse(): void {
    this.removeToken();
    this.isUserAuthenticated.next(false);
  }

  public async checkTokenValidity() {

    if (this.isTokenValid()) {
      console.log("Token is valid!");
      this.isUserAuthenticated.next(true);
    } else {
      console.log("Token is invalid!");
      this.isUserAuthenticated.next(false);
    }
  }

  getTokenValue(): string {
   
    return localStorage.getItem(this.tokenKey) || "";
  }

getTokenExpirationDate(): Date | null {
  const tokenValue = this.getTokenValue();

  if (!tokenValue) {
    return null;
  }

  const decoded: any = this.jwtHelper.decodeToken(tokenValue);

  if (!decoded || !decoded.exp) {
    console.log(decoded);
    return null;
  }

  const expirationDate = new Date(0);
  expirationDate.setUTCSeconds(decoded.exp);
  // console.log("exp:", expirationDate);
  return expirationDate;
}

isTokenExpired(): boolean {
  const expirationDate = this.getTokenExpirationDate();
  if (!expirationDate) {
    return true;
  }
  return expirationDate.valueOf() <= new Date().valueOf();
}


setToken(token: any): void {
  if (token) {
    const formattedToken = token.replace('Bearer ', '');

      localStorage.setItem(this.tokenKey, formattedToken);
      console.log("Token stored successfully in localStorage!");
    
  } else {
    console.log("Unable to store token!");
  }
}


removeToken(): void {
 
    localStorage.removeItem(this.tokenKey);
    console.log("Token removed from localStorage!");
  
}


isTokenValid(): boolean {
  return !this.isTokenExpired();
}




}
