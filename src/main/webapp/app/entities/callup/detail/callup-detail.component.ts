import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICallup } from '../callup.model';

@Component({
  selector: 'jhi-callup-detail',
  templateUrl: './callup-detail.component.html',
})
export class CallupDetailComponent implements OnInit {
  callup: ICallup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ callup }) => {
      this.callup = callup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
