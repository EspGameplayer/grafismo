import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStadium } from '../stadium.model';

@Component({
  selector: 'jhi-stadium-detail',
  templateUrl: './stadium-detail.component.html',
})
export class StadiumDetailComponent implements OnInit {
  stadium: IStadium | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stadium }) => {
      this.stadium = stadium;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
