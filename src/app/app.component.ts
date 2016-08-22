import { Component } from '@angular/core';
import { ROUTER_DIRECTIVES } from '@angular/router';

import { ApiService } from './shared';

import '../style/app.scss';

@Component({
    selector: 'my-app',
    providers: [ApiService],
    directives: [...ROUTER_DIRECTIVES],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
})
export class AppComponent {
    url = 'https://github.com/preboot/angular2-webpack';

    constructor(private api: ApiService) {
    }
}
