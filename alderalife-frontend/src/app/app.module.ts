import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ScrollToModule } from '@nicky-lenaers/ngx-scroll-to';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { HomepageComponent } from './homepage/homepage.component';
import { UserpageComponent } from './userpage/userpage.component';
import { LoginComponent } from './login/login.component';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule, MatPaginatorModule,
  MatSelectModule, MatTableModule
} from '@angular/material';
import {MatIconModule} from '@angular/material/icon';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { WhitelistComponent } from './whitelist/whitelist.component';
import {PopupComponent} from './popup/popup.component';
import { AdminComponent } from './admin/admin.component';
import { WhitelistValidationComponent } from './admin/whitelist-validation/whitelist-validation.component';
import { RulesComponent } from './rules/rules.component';
import { UserListComponent } from './admin/user-list/user-list.component';
import { ManageServerComponent } from './admin/manage-server/manage-server.component';
import { ConfirmEmailComponent } from './confirm-email/confirm-email.component';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    HomepageComponent,
    UserpageComponent,
    LoginComponent,
    WhitelistComponent,
    PopupComponent,
    AdminComponent,
    WhitelistValidationComponent,
    RulesComponent,
    UserListComponent,
    ManageServerComponent,
    ConfirmEmailComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    MatDialogModule,
    ScrollToModule.forRoot(),
    MatFormFieldModule,
    FormsModule,
    HttpClientModule,
    MatSelectModule,
    MatInputModule,
    MatCheckboxModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: [
    LoginComponent,
    PopupComponent
  ],
  entryComponents: [
    LoginComponent,
    PopupComponent
  ]
})
export class AppModule { }
