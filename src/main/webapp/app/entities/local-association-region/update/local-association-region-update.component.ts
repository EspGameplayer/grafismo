import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILocalAssociationRegion, LocalAssociationRegion } from '../local-association-region.model';
import { LocalAssociationRegionService } from '../service/local-association-region.service';
import { IAssociation } from 'app/entities/association/association.model';
import { AssociationService } from 'app/entities/association/service/association.service';

@Component({
  selector: 'jhi-local-association-region-update',
  templateUrl: './local-association-region-update.component.html',
})
export class LocalAssociationRegionUpdateComponent implements OnInit {
  isSaving = false;

  associationsSharedCollection: IAssociation[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    association: [],
  });

  constructor(
    protected localAssociationRegionService: LocalAssociationRegionService,
    protected associationService: AssociationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localAssociationRegion }) => {
      this.updateForm(localAssociationRegion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const localAssociationRegion = this.createFromForm();
    if (localAssociationRegion.id !== undefined) {
      this.subscribeToSaveResponse(this.localAssociationRegionService.update(localAssociationRegion));
    } else {
      this.subscribeToSaveResponse(this.localAssociationRegionService.create(localAssociationRegion));
    }
  }

  trackAssociationById(_index: number, item: IAssociation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalAssociationRegion>>): void {
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

  protected updateForm(localAssociationRegion: ILocalAssociationRegion): void {
    this.editForm.patchValue({
      id: localAssociationRegion.id,
      name: localAssociationRegion.name,
      association: localAssociationRegion.association,
    });

    this.associationsSharedCollection = this.associationService.addAssociationToCollectionIfMissing(
      this.associationsSharedCollection,
      localAssociationRegion.association
    );
  }

  protected loadRelationshipsOptions(): void {
    this.associationService
      .query()
      .pipe(map((res: HttpResponse<IAssociation[]>) => res.body ?? []))
      .pipe(
        map((associations: IAssociation[]) =>
          this.associationService.addAssociationToCollectionIfMissing(associations, this.editForm.get('association')!.value)
        )
      )
      .subscribe((associations: IAssociation[]) => (this.associationsSharedCollection = associations));
  }

  protected createFromForm(): ILocalAssociationRegion {
    return {
      ...new LocalAssociationRegion(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      association: this.editForm.get(['association'])!.value,
    };
  }
}
