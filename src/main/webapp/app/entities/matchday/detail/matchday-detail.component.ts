import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatchday } from '../matchday.model';

@Component({
  selector: 'jhi-matchday-detail',
  templateUrl: './matchday-detail.component.html',
})
export class MatchdayDetailComponent implements OnInit {
  matchday: IMatchday | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matchday }) => {
      this.matchday = matchday;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
