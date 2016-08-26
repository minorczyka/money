import { Component } from '@angular/core';
import { ROUTER_DIRECTIVES } from '@angular/router';

import { ApiService, CheckService } from './shared';

import '../style/app.scss';

@Component({
    selector: 'my-app',
    providers: [ApiService, CheckService],
    directives: [...ROUTER_DIRECTIVES],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
})
export class AppComponent {

    constructor(private api: ApiService, private checkService: CheckService) { }
}
