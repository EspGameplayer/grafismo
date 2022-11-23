import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISeason, Season } from '../season.model';
import { SeasonService } from '../service/season.service';

@Component({
  selector: 'jhi-season-update',
  templateUrl: './season-update.component.html',
})
export class SeasonUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    startDate: [],
    endDate: [],
  });

  constructor(protected seasonService: SeasonService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ season }) => {
      this.updateForm(season);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const season = this.createFromForm();
    if (season.id !== undefined) {
      this.subscribeToSaveResponse(this.seasonService.update(season));
    } else {
      this.subscribeToSaveResponse(this.seasonService.create(season));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeason>>): void {
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

  protected updateForm(season: ISeason): void {
    this.editForm.patchValue({
      id: season.id,
      name: season.name,
      startDate: season.startDate,
      endDate: season.endDate,
    });
  }

  protected createFromForm(): ISeason {
    return {
      ...new Season(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
    };
  }
}
