import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompetitionPlayer } from '../competition-player.model';

@Component({
  selector: 'jhi-competition-player-detail',
  templateUrl: './competition-player-detail.component.html',
})
export class CompetitionPlayerDetailComponent implements OnInit {
  competitionPlayer: ICompetitionPlayer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competitionPlayer }) => {
      this.competitionPlayer = competitionPlayer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
