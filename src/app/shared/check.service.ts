import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/Rx';

export class Check {

    constructor(
        public id: any,
        public name: string) { }
}

@Injectable()
export class CheckService {

    private _checks: BehaviorSubject<Check[]>;

    private dataStore: {
        checks: Check[]
    };

    constructor() {
        this.dataStore = { checks: [{ id: 10, name: 'test' }] };
        this._checks = <BehaviorSubject<Check[]>>new BehaviorSubject(this.dataStore.checks);
    }

    get checks() {
        return this._checks.asObservable();
    }

    addCheck(check: Check) {
        this.dataStore.checks.push(check);
        this._checks.next(this.dataStore.checks);
    }
}
