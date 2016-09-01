import { Component } from '@angular/core';

import { CheckService } from './shared/check.service';

import '../style/app.scss';

@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
})
export class AppComponent {

    constructor(private checkService: CheckService) { }
}

