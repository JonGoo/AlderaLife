import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from "@angular/material";
import {User} from "../../model/user";
import {RestService} from "../../services/rest.service";

export interface Role {
  id: number;
  name: string;
}

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'username', 'email', 'roles', 'enabled'];
  dataSource: any;

  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort: MatSort;
  user: User = new User();
  showBottom = false;
  role: Role;
  roles: Role[];
  message: string;

  constructor(private rest: RestService) {
    this.dataSource = new MatTableDataSource();
    this.rest.getUserList().subscribe((res) => {
      this.dataSource.data = Object.values(res);
    });
    this.rest.getRoleList().subscribe((res) => {
      this.roles = Object.values(res);
    });
    this.dataSource.filterPredicate = (data, filter) => {
      const dataStr = data.id + data.username + data.email;
      return dataStr.indexOf(filter) !== -1;
    };
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  setUser(user) {
    console.log(this.user);
    this.user = user;
    this.showBottom = true;
    console.log(this.user);
  }

  setUserRole() {
    this.rest.setUserRole(this.user.id, this.role).subscribe(
      (val) => {
        this.message = 'Role correctement modifié';
        this.rest.getUserList().subscribe((res) => {
          this.dataSource.data = Object.values(res);
        });
      },
      response => {
        this.message = 'Erreur dans la modification de role';
      });
  }

}
