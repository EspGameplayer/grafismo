import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatchStats } from '../match-stats.model';

@Component({
  selector: 'jhi-match-stats-detail',
  templateUrl: './match-stats-detail.component.html',
})
export class MatchStatsDetailComponent implements OnInit {
  matchStats: IMatchStats | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matchStats }) => {
      this.matchStats = matchStats;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
