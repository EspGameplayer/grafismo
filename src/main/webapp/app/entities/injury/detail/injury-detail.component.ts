import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInjury } from '../injury.model';

@Component({
  selector: 'jhi-injury-detail',
  templateUrl: './injury-detail.component.html',
})
export class InjuryDetailComponent implements OnInit {
  injury: IInjury | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ injury }) => {
      this.injury = injury;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
