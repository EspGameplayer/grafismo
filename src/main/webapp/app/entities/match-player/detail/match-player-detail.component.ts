import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatchPlayer } from '../match-player.model';

@Component({
  selector: 'jhi-match-player-detail',
  templateUrl: './match-player-detail.component.html',
})
export class MatchPlayerDetailComponent implements OnInit {
  matchPlayer: IMatchPlayer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matchPlayer }) => {
      this.matchPlayer = matchPlayer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
