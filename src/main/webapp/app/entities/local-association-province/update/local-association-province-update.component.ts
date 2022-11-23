import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILocalAssociationProvince, LocalAssociationProvince } from '../local-association-province.model';
import { LocalAssociationProvinceService } from '../service/local-association-province.service';
import { ILocalAssociationRegion } from 'app/entities/local-association-region/local-association-region.model';
import { LocalAssociationRegionService } from 'app/entities/local-association-region/service/local-association-region.service';

@Component({
  selector: 'jhi-local-association-province-update',
  templateUrl: './local-association-province-update.component.html',
})
export class LocalAssociationProvinceUpdateComponent implements OnInit {
  isSaving = false;

  localAssociationRegionsSharedCollection: ILocalAssociationRegion[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    association: [],
  });

  constructor(
    protected localAssociationProvinceService: LocalAssociationProvinceService,
    protected localAssociationRegionService: LocalAssociationRegionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localAssociationProvince }) => {
      this.updateForm(localAssociationProvince);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const localAssociationProvince = this.createFromForm();
    if (localAssociationProvince.id !== undefined) {
      this.subscribeToSaveResponse(this.localAssociationProvinceService.update(localAssociationProvince));
    } else {
      this.subscribeToSaveResponse(this.localAssociationProvinceService.create(localAssociationProvince));
    }
  }

  trackLocalAssociationRegionById(_index: number, item: ILocalAssociationRegion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalAssociationProvince>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(localAssociationProvince: ILocalAssociationProvince): void {
    this.editForm.patchValue({
      id: localAssociationProvince.id,
      name: localAssociationProvince.name,
      association: localAssociationProvince.association,
    });

    this.localAssociationRegionsSharedCollection = this.localAssociationRegionService.addLocalAssociationRegionToCollectionIfMissing(
      this.localAssociationRegionsSharedCollection,
      localAssociationProvince.association
    );
  }

  protected loadRelationshipsOptions(): void {
    this.localAssociationRegionService
      .query()
      .pipe(map((res: HttpResponse<ILocalAssociationRegion[]>) => res.body ?? []))
      .pipe(
        map((localAssociationRegions: ILocalAssociationRegion[]) =>
          this.localAssociationRegionService.addLocalAssociationRegionToCollectionIfMissing(
            localAssociationRegions,
            this.editForm.get('association')!.value
          )
        )
      )
      .subscribe(
        (localAssociationRegions: ILocalAssociationRegion[]) => (this.localAssociationRegionsSharedCollection = localAssociationRegions)
      );
  }

  protected createFromForm(): ILocalAssociationProvince {
    return {
      ...new LocalAssociationProvince(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      association: this.editForm.get(['association'])!.value,
    };
  }
}
