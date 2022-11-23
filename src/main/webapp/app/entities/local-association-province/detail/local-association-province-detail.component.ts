import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocalAssociationProvince } from '../local-association-province.model';

@Component({
  selector: 'jhi-local-association-province-detail',
  templateUrl: './local-association-province-detail.component.html',
})
export class LocalAssociationProvinceDetailComponent implements OnInit {
  localAssociationProvince: ILocalAssociationProvince | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localAssociationProvince }) => {
      this.localAssociationProvince = localAssociationProvince;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
