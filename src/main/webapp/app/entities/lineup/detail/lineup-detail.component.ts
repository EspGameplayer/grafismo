import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILineup } from '../lineup.model';

@Component({
  selector: 'jhi-lineup-detail',
  templateUrl: './lineup-detail.component.html',
})
export class LineupDetailComponent implements OnInit {
  lineup: ILineup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lineup }) => {
      this.lineup = lineup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
