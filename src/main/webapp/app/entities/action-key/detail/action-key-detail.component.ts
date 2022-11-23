import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActionKey } from '../action-key.model';

@Component({
  selector: 'jhi-action-key-detail',
  templateUrl: './action-key-detail.component.html',
})
export class ActionKeyDetailComponent implements OnInit {
  actionKey: IActionKey | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ actionKey }) => {
      this.actionKey = actionKey;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
