import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssociation } from '../association.model';

@Component({
  selector: 'jhi-association-detail',
  templateUrl: './association-detail.component.html',
})
export class AssociationDetailComponent implements OnInit {
  association: IAssociation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ association }) => {
      this.association = association;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
