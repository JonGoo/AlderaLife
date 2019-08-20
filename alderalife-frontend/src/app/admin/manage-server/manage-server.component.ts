import {Component, OnInit, ViewChild} from '@angular/core';
import {RestService} from '../../services/rest.service';
import {MatPaginator, MatSort, MatTableDataSource} from "@angular/material";

@Component({
  selector: 'app-manage-server',
  templateUrl: './manage-server.component.html',
  styleUrls: ['./manage-server.component.scss']
})
export class ManageServerComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'ping'];
  dataSource: any;

  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort: MatSort;

  message: string;

  servers: string[];
  server: string;

  serverOnline;

  steamid: string;
  announce: string;

  userid: number;
  usergroup: string;


  constructor(private rest: RestService) {
    this.rest.getServersList().subscribe((res) => {
      this.servers = Object.values(res);
    });
    this.server = 'AlderaLife';
    this.isOnline();
    this.dataSource = new MatTableDataSource();
    this.rest.getPlayers().subscribe((res) => {
        this.dataSource.data = Object.values(res);
    });
  }

  ngOnInit() {
  }

  startServer() {
    if (!this.serverOnline) {
      this.rest.startServer(this.server).subscribe(
        (val) => {
          this.message = 'Serveur démarré avec succes';
          this.isOnline();

        },
        response => {
          console.log(response);
          this.message = 'Erreur dans le démarrage du serveur';
        });
    } else {
      this.message = 'Le serveur est déjà démarré';
      this.isOnline();
    }
  }

  stopServer() {
    if (this.serverOnline) {
      this.rest.shutdownServer(this.server).subscribe(
        (val) => {
          this.message = 'Serveur arrêté avec succes';
          this.isOnline();
        },
        response => {
          console.log(response);
          this.message = 'Erreur dans l\'arrêt du serveur';
        });
    } else {
      this.message = 'Le serveur est déjà à l\'arrêt';
      this.isOnline();
    }
  }

  isOnline() {
    this.rest.isServerOnline(this.server).subscribe((res) => {
      this.serverOnline = res;
      console.log(res);
    },
      (err) => {
        console.log(err);
        this.serverOnline = false;


      });
  }

  addWhitelist() {
    const command = 'addwl hex ' + this.steamid;
    this.rest.sendCommand(command, this.server).subscribe((res) => {
      this.message = 'Joueur correctement ajouté à la whitelist';
    },
      (err) => {
        this.message = 'Erreur lors de l\'ajout';
      });
  }

  reloadWhitelist() {
    const command = 'reloadwl';
    this.rest.sendCommand(command, this.server).subscribe((res) => {
        this.message = 'Whitelist rechargée avec succès';
      },
      (err) => {
        this.message = 'Erreur lors du rechargement de la whitelist';
      });
  }

  announceServer() {
    const command = 'announce ' + this.announce;
    this.rest.sendCommand(command, this.server).subscribe((res) => {
        this.message = 'Joueur correctement ajouté à la whitelist';
      },
      (err) => {
        this.message = 'Erreur lors de l\'ajout';
      });
  }

  setgroup() {
    const command = 'setgroup ' + this.userid + ' ' + this.usergroup;
    this.rest.sendCommand(command, this.server).subscribe((res) => {
        this.message = 'Le joueur ' + this.userid + ' est bien devenu ' + this.usergroup;
      },
      (err) => {
        this.message = 'Erreur lors du setgroup';
      });
  }

}
