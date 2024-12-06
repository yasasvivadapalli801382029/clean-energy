import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SigninComponent } from './signin/signin.component';
import { SignupComponent } from './signup/signup.component';
import { HomeComponent } from './home/home.component';
import { AuthguardService } from 'src/services/authguard/authguard.service';
import { SummaryComponent } from './summary/summary.component';
import { ReportsComponent } from './reports/reports.component';

const routes: Routes = [

    {path:'',component:HomeComponent ,canActivate: [AuthguardService]},
    {path:'dashboard',component:HomeComponent ,canActivate: [AuthguardService]},
    {path:'home',component:HomeComponent ,canActivate: [AuthguardService]},
    {path:'summary',component:SummaryComponent ,canActivate: [AuthguardService]},
    {path:'reports',component:ReportsComponent ,canActivate: [AuthguardService]},
    {path:'signin',component:SigninComponent},
    {path:'signup',component:SignupComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { 


}
