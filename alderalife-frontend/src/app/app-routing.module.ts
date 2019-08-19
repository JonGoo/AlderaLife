import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomepageComponent} from './homepage/homepage.component';
import {UserpageComponent} from './userpage/userpage.component';
import {WhitelistComponent} from './whitelist/whitelist.component';
import {AdminComponent} from './admin/admin.component';
import {WhitelistValidationComponent} from './admin/whitelist-validation/whitelist-validation.component';
import {RulesComponent} from './rules/rules.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomepageComponent },
  { path: 'rules', component: RulesComponent },
  { path: 'user', component: UserpageComponent, children: [
      {path: 'create', component: HomepageComponent },
    ]
  },
  { path: 'whitelist', component: WhitelistComponent},
  { path: 'admin', component: AdminComponent, children: [
      { path: 'whitelist', component: WhitelistValidationComponent },
      {path: '', pathMatch: 'full', redirectTo: 'whitelist'}
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
