import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from "./header/header.component";
import { HttpClientModule, HttpClientXsrfModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule} from '@angular/forms';
import { HttpXSRFInterceptor } from './intecepter/http.xsrf.interceptor';


@NgModule({
    declarations: [
        AppComponent
    ],
    providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: HttpXSRFInterceptor,
        multi: true
      },],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        HttpClientModule,
        HttpClientXsrfModule,
        FormsModule,
        AppRoutingModule,
        NgbModule,
        HeaderComponent
    ]
})
export class AppModule { }
