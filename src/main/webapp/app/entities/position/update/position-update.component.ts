import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPosition, Position } from '../position.model';
import { PositionService } from '../service/position.service';

@Component({
  selector: 'jhi-position-update',
  templateUrl: './position-update.component.html',
})
export class PositionUpdateComponent implements OnInit {
  isSaving = false;

  positionsSharedCollection: IPosition[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    abb: [null, [Validators.required]],
    parents: [],
  });

  constructor(protected positionService: PositionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ position }) => {
      this.updateForm(position);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const position = this.createFromForm();
    if (position.id !== undefined) {
      this.subscribeToSaveResponse(this.positionService.update(position));
    } else {
      this.subscribeToSaveResponse(this.positionService.create(position));
    }
  }

  trackPositionById(_index: number, item: IPosition): number {
    return item.id!;
  }

  getSelectedPosition(option: IPosition, selectedVals?: IPosition[]): IPosition {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPosition>>): void {
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

  protected updateForm(position: IPosition): void {
    this.editForm.patchValue({
      id: position.id,
      name: position.name,
      abb: position.abb,
      parents: position.parents,
    });

    this.positionsSharedCollection = this.positionService.addPositionToCollectionIfMissing(
      this.positionsSharedCollection,
      ...(position.parents ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.positionService
      .query()
      .pipe(map((res: HttpResponse<IPosition[]>) => res.body ?? []))
      .pipe(
        map((positions: IPosition[]) =>
          this.positionService.addPositionToCollectionIfMissing(positions, ...(this.editForm.get('parents')!.value ?? []))
        )
      )
      .subscribe((positions: IPosition[]) => (this.positionsSharedCollection = positions));
  }

  protected createFromForm(): IPosition {
    return {
      ...new Position(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      abb: this.editForm.get(['abb'])!.value,
      parents: this.editForm.get(['parents'])!.value,
    };
  }
}
