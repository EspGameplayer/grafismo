import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocalAssociationRegion } from '../local-association-region.model';

@Component({
  selector: 'jhi-local-association-region-detail',
  templateUrl: './local-association-region-detail.component.html',
})
export class LocalAssociationRegionDetailComponent implements OnInit {
  localAssociationRegion: ILocalAssociationRegion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localAssociationRegion }) => {
      this.localAssociationRegion = localAssociationRegion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
