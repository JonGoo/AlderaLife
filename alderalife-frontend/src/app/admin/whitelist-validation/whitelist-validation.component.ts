import {Component, OnInit, ViewChild} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {RestService} from '../../services/rest.service';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-whitelist-validation',
  templateUrl: './whitelist-validation.component.html',
  styleUrls: ['./whitelist-validation.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ])
  ]
})
export class WhitelistValidationComponent implements OnInit {

  dataSource: any;
  columnsToDisplay = ['id', 'pseudo', 'age', 'steamid', 'firstNameRp', 'lastNameRp', 'avenirRp'];
  message: string;


  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort: MatSort;

  constructor(private rest: RestService) {
    this.dataSource = new MatTableDataSource();
    this.rest.getWhitelistList().subscribe((res) => {
      this.dataSource.data = Object.values(res);
    });
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  accept(id: number) {
    this.rest.acceptWhitelist(id).subscribe(
      (val) => {
        this.message = 'Candidature acceptée';
        this.rest.getWhitelistList().subscribe((res) => {
          this.dataSource.data = Object.values(res);
        });
      },
      response => {
        this.message = 'Erreur';
      });
  }

  refuse(id: number) {
    this.rest.refuseWhitelist(id).subscribe(
      (val) => {
        this.message = 'Candidature refusée';
        this.rest.getWhitelistList().subscribe((res) => {
          this.dataSource.data = Object.values(res);
        });
      },
      response => {
        this.message = 'Erreur';
      });
  }

}
