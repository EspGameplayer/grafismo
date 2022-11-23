import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReferee } from '../referee.model';

@Component({
  selector: 'jhi-referee-detail',
  templateUrl: './referee-detail.component.html',
})
export class RefereeDetailComponent implements OnInit {
  referee: IReferee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ referee }) => {
      this.referee = referee;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
