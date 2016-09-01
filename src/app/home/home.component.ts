import { Component, OnInit } from '@angular/core';
import { CheckService, Check } from '../shared/check.service';
import { Observable } from 'rxjs/Rx';

@Component({
    selector: 'my-home',
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
