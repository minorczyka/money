import { Component, OnInit } from '@angular/core';
import { CheckService } from '../shared';
import { Check } from '../shared';
import { Observable } from 'rxjs/Rx';

@Component({
    selector: 'my-home',
    providers: [CheckService],
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    private checks: Observable<Check[]>;

    constructor(private checkService: CheckService) { }

    ngOnInit() {
        this.checks = this.checkService.checks;
    }
}
